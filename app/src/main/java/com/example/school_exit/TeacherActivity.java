package com.example.school_exit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TeacherActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ArrayList<Request> requestsByFilter, requestsByStatus, currentRequestsList;
    RequestAdapter requestAdapter;
    Dialog dialogFilter ,dialogRequest;
    Spinner spFilter;
    ListView lv;
    EditText dialogFilterEtFiled;
    Button dialogFilterBtnSubmit, dialogFilterBtnCancel, dialogRequestBtnApprove, dialogRequestBtnReject, dialogRequestBtnCancel;
    DatabaseTools databaseTools;
    String dialogFilterForWhat;
    TextView tvNoRequests, dialogFilterTvNotExist, dialogFilterTvEnter, dialogRequestTvChildName, dialogRequestTvParentName,
            dialogRequestTvDate, dialogRequestTvTime, dialogRequestTvReason, dialogRequestTvNote;
    Request currentRequest;
    Teacher teacher;
    int requestedStatus, spinnerIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_requests);

        tvNoRequests = findViewById(R.id.watchRequestsTvNoRequests);
        spFilter = findViewById(R.id.watchRequestsSpFilter);
        databaseTools = new DatabaseTools(this);

        Intent intent = getIntent();
        String teacherId = (String) intent.getExtras().get("Id");

        databaseTools.open();
        teacher = databaseTools.getTeacherById(teacherId);
        databaseTools.close();

        //teacherRequests = teacher.getRequests();

        lv = findViewById(R.id.watchRequestsLvRequests);
        requestAdapter = new RequestAdapter(this, R.layout.watch_requests, teacher.getRequests());
        lv.setAdapter(requestAdapter);

        if (teacher.getRequests().size() == 0) {
            tvNoRequests.setVisibility(View.VISIBLE);
        }
        else {
            tvNoRequests.setVisibility(View.INVISIBLE);
        }

        requestsByFilter = teacher.getRequests();
        currentRequestsList = teacher.getRequests();

        List<String> arrayListFilters = new ArrayList<String>();
        arrayListFilters.add("Waiting to approve");
        arrayListFilters.add("Approved");
        arrayListFilters.add("Denied");

        ArrayAdapter<String> dataAdapterFilters = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListFilters);

        spFilter.setAdapter(dataAdapterFilters);
        spFilter.setOnItemSelectedListener(this);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentRequest = teacher.getRequests().get(position);
                createDialogRequest();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.requests_filter_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // ------------------------------------------------------------------------- ON OPTIONS ITEM SELECTED

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        databaseTools.open();

        switch (item.getItemId()) {
            case R.id.action_by_child:
                Toast.makeText(this, "you selected: by child" , Toast.LENGTH_LONG).show();
                dialogFilterForWhat = "Child";
                createDialogFilter();
                dialogFilterTvNotExist.setText("The filed name doesn't exist");
                dialogFilterTvEnter.setText("Please enter the name of the child");
                dialogFilterEtFiled.setHint("Name of child");
                break;

            case R.id.action_by_parent:
                Toast.makeText(this, "you selected: by parent" , Toast.LENGTH_LONG).show();
                dialogFilterForWhat = "Parent";
                createDialogFilter();
                dialogFilterTvNotExist.setText("The filed name doesn't exist");
                dialogFilterTvEnter.setText("Please enter the name of the parent");
                dialogFilterEtFiled.setHint("Name of parent");
                break;

            case R.id.action_by_day :
                Toast.makeText(this, "you selected: by day" , Toast.LENGTH_LONG).show();
                Calendar systemCalendar = Calendar.getInstance();
                int year = systemCalendar.get(Calendar.YEAR);
                int month = systemCalendar.get(Calendar.MONTH);
                int day = systemCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new SetDate(), year, month, day);
                datePickerDialog.show();
                break;

            case R.id.action_by_month:
                Toast.makeText(this, "you selected: by month" , Toast.LENGTH_LONG).show();
                dialogFilterForWhat = "Month";
                createDialogFilter();
                dialogFilterTvNotExist.setText("The filed month doesn't exist");
                dialogFilterTvEnter.setText("Please enter the number of the month");
                dialogFilterEtFiled.setHint("Number of month");
                dialogFilterEtFiled.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;

            case R.id.action_by_reason:
                Toast.makeText(this, "you selected: by reason" , Toast.LENGTH_LONG).show();
                dialogFilterForWhat = "Reason";
                createDialogFilter();
                dialogFilterTvNotExist.setText("The filed reason doesn't exist");
                dialogFilterTvEnter.setText("Please enter the Reason");
                dialogFilterEtFiled.setHint("Reason");
                break;

            case R.id.action_exit:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert!");
                builder.setMessage("Are you sure you want to exit?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new ClassDialogAlert());
                builder.setNegativeButton("No", new ClassDialogAlert());
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.action_all_requests:

                requestsByFilter = teacher.getRequests();
                refreshMyAdapter();
                break;

            default:
                break;
        }
        databaseTools.close();

        return super.onOptionsItemSelected(item);
    }

    public void refreshMyAdapter()
    {
        currentRequestsList = changeArrayByStatus(requestsByFilter, requestedStatus);
        if (currentRequestsList.size() == 0)
            tvNoRequests.setVisibility(View.VISIBLE);
        else
            tvNoRequests.setVisibility(View.INVISIBLE);
        requestAdapter=new RequestAdapter(this,0, currentRequestsList);
        lv.setAdapter(requestAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.watchRequestsSpFilter)
        {
            spinnerIndex = position;
            if (position == 0 || position == 1) {
                requestedStatus = position;
            }
            else {
                requestedStatus = -1;
            }
            refreshMyAdapter();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class ClassDialogAlert implements DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which) {

            if (which == -1) {
                setResult(RESULT_CANCELED);
                finish();
            }

        }
    }

    // ------------------------------------------------------------------------- ON CLICK
    @Override
    public void onClick(View v) {

        if (v == dialogFilterBtnCancel) // cancel the dialog
            dialogFilter.dismiss();
        else if (v == dialogFilterBtnSubmit) // filter the array
        {
            if (dialogFilterEtFiled.getText().toString().equals("")) // witch means an empty
                dialogFilterTvNotExist.setVisibility(View.VISIBLE);
            else if (validInput()) // checking if the input is valid
            {
                if (dialogFilterForWhat.equals("Child"))
                    requestsByFilter = changeArrayListByChild (teacher.getRequests(), dialogFilterEtFiled.getText().toString()); // filter the array to the filed child
                else if (dialogFilterForWhat.equals("Parent"))
                    requestsByFilter = changeArrayListByParent (teacher.getRequests(), dialogFilterEtFiled.getText().toString()); // filter the array to the filed parent
                else if (dialogFilterForWhat.equals("Month"))
                    requestsByFilter = changeArrayListByMonth(teacher.getRequests(), Integer.valueOf(dialogFilterEtFiled.getText().toString())); // filter the array to the filed month
                else if (dialogFilterForWhat.equals("Reason"))
                    requestsByFilter = changeArrayListByReason(teacher.getRequests(), dialogFilterEtFiled.getText().toString()); // filter the array to the filed reason
                refreshMyAdapter();
                dialogFilter.dismiss();

            }
        }
        else if (v == dialogRequestBtnCancel)
            dialogRequest.dismiss();
        else if (v == dialogRequestBtnApprove)
        {
            Toast.makeText(this, "Approved", Toast.LENGTH_LONG).show();
            currentRequest.setStatus(1);
            refreshMyAdapter();
            databaseTools.open();
            databaseTools.updateRequestStatus(currentRequest, currentRequest.getStatus());
            databaseTools.close();
            dialogRequest.dismiss();
        }
        else if (v == dialogRequestBtnReject)
        {
            Toast.makeText(this, "Reject", Toast.LENGTH_LONG).show();
            currentRequest.setStatus(-1);
            refreshMyAdapter();
            databaseTools.open();
            databaseTools.updateRequestStatus(currentRequest, currentRequest.getStatus());
            databaseTools.close();
            dialogRequest.dismiss();
        }
    }




    private class SetDate implements DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int month, int day) {

            month++; // instead of index 0-11
            Toast.makeText(TeacherActivity.this, "You selected the date: " + day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            requestsByFilter = changeArrayListByDay(teacher.getRequests(), day, month, year);
            refreshMyAdapter();
        }
    }

    // ------------------------------------------------------------------------- CREATE DIALOG

    private void createDialogFilter() {

        dialogFilter = new Dialog(this);
        dialogFilter.setContentView(R.layout.dialog_by_filter);
        dialogFilter.setCancelable(false);

        // tracking all the relevant views
        dialogFilterEtFiled = dialogFilter.findViewById(R.id.childOrParentDialogScreenEtName);
        dialogFilterBtnSubmit = dialogFilter.findViewById(R.id.childOrParentDialogScreenBtnSubmit);
        dialogFilterBtnCancel = dialogFilter.findViewById(R.id.childOrParentDialogScreenBtnCancel);
        dialogFilterTvNotExist = dialogFilter.findViewById(R.id.childOrParentDialogScreenTvNotExist);
        dialogFilterTvEnter = dialogFilter.findViewById(R.id.childOrParentDialogScreenTvEnter);

        dialogFilterTvNotExist.setVisibility(View.INVISIBLE);

        // on click listener
        dialogFilterBtnSubmit.setOnClickListener(this);
        dialogFilterBtnCancel.setOnClickListener(this);

        dialogFilter.show();

    }

    // ------------------------------------------------------------------------- VALID INPUT

    private boolean validInput() // checking if the input is valid
    {
        boolean isOk = true; // is the input valid
        String filed = dialogFilterEtFiled.getText().toString(); // what the user filed in the empty spot

        if (dialogFilterForWhat.equals("Child")) // filter by child
        {
            if (filed.length() < 5) { // full name can not be under than 5 digits
                dialogFilterTvNotExist.setVisibility(View.VISIBLE);
                isOk = false;
            }
            else
                dialogFilterTvNotExist.setVisibility(View.INVISIBLE);
        }
        else if (dialogFilterForWhat.equals("Parent"))
        {
            if (filed.length() < 5){
                dialogFilterTvNotExist.setVisibility(View.VISIBLE);
                isOk = false;
            }
            else
                dialogFilterTvNotExist.setVisibility(View.INVISIBLE);
        }
        else if (dialogFilterForWhat.equals("Month"))
        {
            if (Long.valueOf(filed) > 12 || Long.valueOf(filed) < 1){
                dialogFilterTvNotExist.setVisibility(View.VISIBLE);
                isOk = false;
            }
            else
                dialogFilterTvNotExist.setVisibility(View.INVISIBLE);
        }
        else if (dialogFilterForWhat.equals("Reason"))
        {

        }

        return isOk;
    }

    // ------------------------------------------------------------------------- CHANGE LIST BY SOMETHING

    private ArrayList<Request> changeArrayListByParent(ArrayList<Request> arrayList, String name) {

        databaseTools.open();
        ArrayList<Request> newArray = new ArrayList<>();

        for (int i = 0; i<arrayList.size(); i++)
        {
            Parent parent = databaseTools.getParentByChildId(arrayList.get(i).getChildPersonalId());
                if ((parent.getFirstName() + " " + parent.getLastName()).equals(name))
                    newArray.add(arrayList.get(i));
        }

        databaseTools.close();
        return newArray;
    }

    private ArrayList<Request> changeArrayListByDay (ArrayList<Request> arrayList, int day, int month, int year)
    {
        ArrayList<Request> newArray = new ArrayList<>();

        for (int i = 0; i<arrayList.size(); i++)
        {
            if ((arrayList.get(i).getDate().getDayOfMonth() == day) && (arrayList.get(i).getDate().getMonthValue() == month) && (arrayList.get(i).getDate().getYear() == year))
                newArray.add(arrayList.get(i));
        }
        return newArray;
    }

    private ArrayList<Request> changeArrayListByChild(ArrayList<Request> arrayList, String name)
    {
        databaseTools.open();
        ArrayList<Request> newArray = new ArrayList<>();

        for (int i = 0; i<arrayList.size(); i++)
        {
            Child child = databaseTools.getChildByPersonalId(arrayList.get(i).getChildPersonalId());
            if ((child.getFirstName() + " " + child.getLastName()).equals(name))
                newArray.add(arrayList.get(i));
        }

        databaseTools.close();
        return newArray;
    }

    private ArrayList<Request> changeArrayListByMonth(ArrayList<Request> arrayList, int month)
    {
        ArrayList<Request> newArray = new ArrayList<>();

        for (int i = 0; i<arrayList.size(); i++)
        {
            if (arrayList.get(i).getDate().getMonthValue() == month)
                newArray.add(arrayList.get(i));
        }

        return newArray;
    }

    private ArrayList<Request> changeArrayListByReason(ArrayList<Request> arrayList, String reason)
    {
        ArrayList<Request> newArray = new ArrayList<>();

        for (int i = 0; i<arrayList.size(); i++)
        {
            if (arrayList.get(i).getReason().equals(reason))
                newArray.add(arrayList.get(i));
        }

        return newArray;
    }

    private void createDialogRequest ()
    {
        dialogRequest = new Dialog(this);
        dialogRequest.setContentView(R.layout.dialog_request);
        dialogRequest.setCancelable(false);

        dialogRequestTvChildName = dialogRequest.findViewById(R.id.teacherDialogRequestTvChildName);
        dialogRequestTvParentName = dialogRequest.findViewById(R.id.teacherDialogRequestTvParentName);
        dialogRequestTvDate = dialogRequest.findViewById(R.id.teacherDialogRequestTvDate);
        dialogRequestTvTime = dialogRequest.findViewById(R.id.teacherDialogRequestTvTime);
        dialogRequestTvReason = dialogRequest.findViewById(R.id.teacherDialogRequestTvReason);
        dialogRequestTvNote = dialogRequest.findViewById(R.id.teacherDialogRequestTvNote);
        dialogRequestBtnApprove = dialogRequest.findViewById(R.id.teacherDialogRequestBtnApprove);
        dialogRequestBtnReject = dialogRequest.findViewById(R.id.teacherDialogRequestBtnDeny);
        dialogRequestBtnCancel = dialogRequest.findViewById(R.id.teacherDialogRequestBtnCancel);

        databaseTools.open();
        Child child = databaseTools.getChildByPersonalId(currentRequest.getChildPersonalId());
        Parent parent = databaseTools.getParentByPersonalId(child.getParentPersonalId());
        databaseTools.close();

        dialogRequestTvChildName.setText(child.getFirstName() + " " + child.getLastName());
        dialogRequestTvParentName.setText(parent.getFirstName() + " " + parent.getLastName());
        LocalDateTime localDateTime = currentRequest.getDate();
        dialogRequestTvDate.setText(localDateTime.getDayOfMonth() + "/" + localDateTime.getMonthValue() + "/" + localDateTime.getYear());
        Log.d("data1", "Hour is: " + localDateTime.getHour() + " and minitue is: " + localDateTime.getMinute());
        dialogRequestTvTime.setText(localDateTime.getHour() + ":" + localDateTime.getMinute());
        dialogRequestTvReason.setText(currentRequest.getReason());
        dialogRequestTvNote.setText(currentRequest.getNote());

        dialogRequestBtnCancel.setOnClickListener(this);

        System.out.println(currentRequest);

        // currentRequest.getStatus() == 0
        if (spinnerIndex == 0)
        {
            dialogRequestBtnApprove.setOnClickListener(this);
            dialogRequestBtnReject.setOnClickListener(this);
        }
        else
        {
            dialogRequestBtnReject.setVisibility(View.INVISIBLE);
            dialogRequestBtnApprove.setVisibility(View.INVISIBLE);
        }

        dialogRequest.show();
    }

    private ArrayList<Request> changeArrayByStatus (ArrayList<Request> requests, int status)
    {
        ArrayList<Request> newArray = new ArrayList<Request>();
        for (int i = 0; i<requests.size(); i++)
        {
            Request request = requests.get(i);
            if (request.getStatus() == status)
                newArray.add(request);
        }
        return newArray;
    }

}