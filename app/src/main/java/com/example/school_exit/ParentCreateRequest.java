package com.example.school_exit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ParentCreateRequest extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Button btnSendRequest, btnCancel;
    EditText etNote;
    Spinner spinnerChooseChild, spinnerChooseReason;
    TextView tvChooseDate, tvChooseTime, tvNotValid;
    DatabaseTools databaseTools;
    String requestReason;
    int requestDay, requestMonth, requestYear, requestHour, requestMinute, indexChild;
    Parent classParent;
    int howManyTimesChecked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_create_request);

        btnSendRequest = findViewById(R.id.parentCreateRequestScreenBtnSendRequest);
        btnCancel = findViewById(R.id.parentCreateRequestScreenBtnCancel);
        etNote = findViewById(R.id.parentCreateRequestScreenEtNote);
        spinnerChooseChild = findViewById(R.id.parentCreateRequestScreenSpinnerChild);
        spinnerChooseReason = findViewById(R.id.parentCreateRequestScreenSpinnerReason);

        tvChooseDate = findViewById(R.id.parentCreateRequestScreenTvChooseDate);
        tvChooseTime = findViewById(R.id.parentCreateRequestScreenTvChooseTime);
        tvNotValid = findViewById(R.id.parentCreateRequestScreenTvNotValid);
        databaseTools = new DatabaseTools(this);

        Intent intent = getIntent();
        classParent = (Parent) intent.getExtras().get("Parent");

        /*
        String parentId = (String) intent.getExtras().get("Id");
        databaseTools.open();
        parent = databaseTools.getParentByPersonalId(parentId);
        databaseTools.close();
         */

        requestDay = -1;
        requestMonth = -1;
        requestYear = -1;
        requestHour = -1;
        requestMinute = -1;

        btnSendRequest.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tvChooseDate.setOnClickListener(this);
        tvChooseTime.setOnClickListener(this);

        List<String> arrayListChildrenName = new ArrayList<String>();
        List<String> arrayListReasons = new ArrayList<String>();
        arrayListReasons.add("Sick");
        arrayListReasons.add("Family event");
        arrayListReasons.add("School event");
        arrayListReasons.add("Else");

        for (int i = 0; i< classParent.getChildren().size(); i++)
        {
            arrayListChildrenName.add(classParent.getChildren().get(i).getFirstName());
        }

        ArrayAdapter<String> dataAdapterChildren = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListChildrenName);
        ArrayAdapter<String> dataAdapterReasons = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListReasons);

        spinnerChooseChild.setAdapter(dataAdapterChildren);
        spinnerChooseReason.setAdapter(dataAdapterReasons);

        spinnerChooseReason.setOnItemSelectedListener(this);
        spinnerChooseChild.setOnItemSelectedListener(this);

        tvNotValid.setVisibility(View.INVISIBLE);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.parentCreateRequestScreenSpinnerChild)
        {
            indexChild = position;
            if (howManyTimesChecked < 2)
                howManyTimesChecked++;
            else
                Toast.makeText(this, "child chosen is: " + classParent.getChildren().get(indexChild).getFirstName() , Toast.LENGTH_LONG).show();
        }
        else if(parent.getId() == R.id.parentCreateRequestScreenSpinnerReason)
        {
            requestReason = parent.getItemAtPosition(position).toString();
            if (howManyTimesChecked < 2)
                howManyTimesChecked++;
            else
                Toast.makeText(this, "reason chosen is: " + requestReason, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        if (v == tvChooseDate)
        {
            Calendar systemCalendar = Calendar.getInstance();
            int year = systemCalendar.get(Calendar.YEAR);
            int month = systemCalendar.get(Calendar.MONTH);
            int day = systemCalendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new SetDate(), year, month, day);
            datePickerDialog.show();
        }
        else if (v == tvChooseTime)
        {
            Calendar systemCalendar = Calendar.getInstance();
            int hour = systemCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = systemCalendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,new SetYourTime(),hour,minute,true);
            timePickerDialog.show();
        }
        else if (v == btnSendRequest)
        {
            if (validInput())
            {
                String note = etNote.getText().toString();
                if (note.equals(""))
                    note = "Non";
                Request request = new Request(LocalDateTime.of(requestYear, requestMonth, requestDay, requestHour,
                        requestMinute), classParent.getChildren().get(indexChild).getPersonalId(), requestReason, note, 0);
                databaseTools.open();
                databaseTools.insertRequest(request);
                databaseTools.close();
                classParent.addRequest(request);
                databaseTools.open();
                Teacher teacher = databaseTools.getTeacherByClass(classParent.getChildren().get(indexChild).getNumberClass());
                databaseTools.close();
                Log.d("data1", "CreateRequest, teacher that gonna get a messege is " + teacher);
                if (teacher != null)
                    sendSMSMessage(teacher.getPhoneNumber());
                Log.d("data1", "Create Request, time chosen is- " + request.getDate().getHour() + ":" + request.getDate().getMinute());

                Intent intent = getIntent();
                intent.putExtra("Parent", classParent);
                Toast.makeText(this, "Request sent successfully", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        else if (v == btnCancel)
        {
            finish();
        }
    }

    private boolean validInput() {

        boolean isOk = true;

        if (tvChooseDate.getText().toString().equals("Press to choose date")){
            isOk = false;
            tvNotValid.setVisibility(View.VISIBLE);
        }
        else
            tvNotValid.setVisibility(View.INVISIBLE);
        if (tvChooseTime.getText().toString().equals("Press to choose time")){
            isOk = false;
            tvNotValid.setVisibility(View.VISIBLE);
        }
        else
            tvNotValid.setVisibility(View.INVISIBLE);

        return isOk;

    }

    private class SetDate implements DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int month, int day) {

            month++; // instead of index 0-11
            Toast.makeText(ParentCreateRequest.this, "You selected the date: " + day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

            tvChooseDate.setText(day + "/" + month + "/" + year);
            requestDay = day;
            requestMonth = month;
            requestYear = year;
        }
    }

    private class  SetYourTime implements TimePickerDialog.OnTimeSetListener
    {
        public void onTimeSet(TimePicker view, int hour, int minute) {

            Toast.makeText(ParentCreateRequest.this, "You selected: " + hour + ":" + minute, Toast.LENGTH_LONG).show();
            tvChooseTime.setText(hour + ":" + minute);
            requestHour = hour;
            requestMinute = minute;
        }
    }

    protected void sendSMSMessage(String phoneNumber) {

        Log.d("data1", "message sent");
        String message = "SchoolExit: You got a new request!";

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        0);
            }
        }
        else{
            sendSMS(phoneNumber, message);
        }
    }

    private void sendSMS(String phoneNumber, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

}