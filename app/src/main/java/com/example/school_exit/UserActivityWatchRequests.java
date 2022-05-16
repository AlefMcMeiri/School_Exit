package com.example.school_exit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

public class UserActivityWatchRequests extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ArrayList<Request> requestsByFilterList, currentRequestsList;
    RequestAdapter requestAdapter;
    Dialog dialogFilter ,dialogRequest;
    Spinner spStatus;
    ListView lv;
    EditText dialogFilterEtFiled;
    Button btnBack, dialogFilterBtnSubmit, dialogFilterBtnCancel, dialogRequestBtnApprove, dialogRequestBtnDeny, dialogRequestBtnCancel;
    DatabaseTools databaseTools;
    String dialogFilterForWhat;
    TextView tvNoRequests, dialogFilterTvNotExist, dialogFilterTvEnter, dialogRequestTvNumber, dialogRequestTvChildName, dialogRequestTvParentName,
            dialogRequestTvDate, dialogRequestTvTime, dialogRequestTvReason, dialogRequestTvNote;
    Request currentRequest;
    User user;
    int requestedStatus, spinnerIndex;

    public static final int SCREEN_MANAGE_PROFILE = 49;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_requests);

        // tracking the relevant views
        btnBack = findViewById(R.id.watchRequestsBtnBack);
        tvNoRequests = findViewById(R.id.watchRequestsTvNoRequests);
        spStatus = findViewById(R.id.watchRequestsSpFilter);
        lv = findViewById(R.id.watchRequestsLvRequests);
        databaseTools = new DatabaseTools(this);

        btnBack.setOnClickListener(this);

        Intent intent = getIntent();
        user = (User) intent.getExtras().get("User");

        /*
        String userId = (String) intent.getExtras().get("Id"); // get User ID from the intent
        databaseTools.open();
        user = databaseTools.getUserById(userId); // tracking the User from the data base
        databaseTools.close();
        */

        Log.d("data1", "UserRequests, User is: " + user.toString());

        requestAdapter = new RequestAdapter(this, R.layout.watch_requests, user.getRequests());
        lv.setAdapter(requestAdapter);

        if (user.getRequests() != null)
        {
            if (user.getRequests().size() == 0) { // means there is no Requests
                Log.d("data1", "UserRequests, user.getRequests.size equals to 0");
                tvNoRequests.setVisibility(View.VISIBLE);
            }
            else {
                Log.d("data1", "UserRequests, user.getRequests.size equals to " + user.getRequests().size());
                tvNoRequests.setVisibility(View.INVISIBLE); // there is Requests
            }
        }
        else
        {
            Log.d("data1", "UserRequests, user.getRequests is null");
            tvNoRequests.setVisibility(View.VISIBLE);
        }


        requestsByFilterList = user.getRequests();
        currentRequestsList = user.getRequests();

        List<String> arrayListFilters = new ArrayList<String>(); // the options in the Spinner
        arrayListFilters.add("Pending");
        arrayListFilters.add("Approved");
        arrayListFilters.add("Denied");

        ArrayAdapter<String> dataAdapterFilters = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListFilters);

        spStatus.setAdapter(dataAdapterFilters);
        spStatus.setOnItemSelectedListener(this);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentRequest = currentRequestsList.get(position); // get the current request that clicked
                createDialogRequest(); // shows the Dialog of approving/denying Request
            }
        });

        if (user instanceof Teacher)
        {
            btnBack.setVisibility(View.INVISIBLE);
            Log.d("data1", "user is instanceof a teacher");
        }
        else if (user instanceof Parent)
        {
            Log.d("data1", "user is instanceof a parent");
        }


    }

    private void createDialogRequest() {

        dialogRequest = new Dialog(this);
        dialogRequest.setContentView(R.layout.dialog_request);
        dialogRequest.setCancelable(false);

        Log.d("data1", "time is: " + currentRequest.getDate().getHour() + ":" + currentRequest.getDate().getMinute());
        dialogRequestTvNumber = dialogRequest.findViewById(R.id.dialogRequestTvNumber);
        dialogRequestTvChildName = dialogRequest.findViewById(R.id.teacherDialogRequestTvChildName);
        dialogRequestTvParentName = dialogRequest.findViewById(R.id.teacherDialogRequestTvParentName);
        dialogRequestTvDate = dialogRequest.findViewById(R.id.teacherDialogRequestTvDate);
        dialogRequestTvTime = dialogRequest.findViewById(R.id.teacherDialogRequestTvTime);
        dialogRequestTvReason = dialogRequest.findViewById(R.id.teacherDialogRequestTvReason);
        dialogRequestTvNote = dialogRequest.findViewById(R.id.teacherDialogRequestTvNote);
        dialogRequestBtnApprove = dialogRequest.findViewById(R.id.teacherDialogRequestBtnApprove);
        dialogRequestBtnDeny = dialogRequest.findViewById(R.id.teacherDialogRequestBtnDeny);
        dialogRequestBtnCancel = dialogRequest.findViewById(R.id.teacherDialogRequestBtnCancel);

        databaseTools.open();
        Child child = databaseTools.getChildByPersonalId(currentRequest.getChildPersonalId());
        databaseTools.close();
        Parent parent = user.getParentByPersonalId(child.getParentPersonalId(), databaseTools);

        dialogRequestTvNumber.setText("Request number " + currentRequest.getId());
        dialogRequestTvChildName.setText(child.getFirstName() + " " + child.getLastName());
        dialogRequestTvParentName.setText(parent.getFirstName() + " " + parent.getLastName());
        LocalDateTime localDateTime = currentRequest.getDate();
        dialogRequestTvDate.setText(localDateTime.getDayOfMonth() + "/" + localDateTime.getMonthValue() + "/" + localDateTime.getYear());
        Log.d("data1", "Hour is: " + localDateTime.getHour() + " and minitue is: " + localDateTime.getMinute());
        dialogRequestTvTime.setText(localDateTime.getHour() + ":" + localDateTime.getMinute());
        dialogRequestTvReason.setText(currentRequest.getReason());
        dialogRequestTvNote.setText(currentRequest.getNote());

        // currentRequest.getStatus() == 0
        if (spinnerIndex == 0)
        {
            if (user instanceof Teacher)
            {
                dialogRequestBtnApprove.setOnClickListener(this);
                dialogRequestBtnDeny.setOnClickListener(this);
                dialogRequestBtnCancel.setOnClickListener(this);
            }
            else
            {
                dialogRequestBtnDeny.setText("DELETE");
                dialogRequestBtnApprove.setText("OK");
                dialogRequestBtnCancel.setVisibility(View.INVISIBLE);
                dialogRequestBtnApprove.setOnClickListener(this);
                dialogRequestBtnDeny.setOnClickListener(this);
            }
        }
        else
        {
            dialogRequestBtnCancel.setOnClickListener(this);
            dialogRequestBtnDeny.setVisibility(View.INVISIBLE);
            dialogRequestBtnApprove.setVisibility(View.INVISIBLE);
        }

        dialogRequest.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { // when the spinner changing
        if(parent.getId() == R.id.watchRequestsSpFilter)
        {
            spinnerIndex = position;
            if (spinnerIndex == 0 || spinnerIndex == 1) {
                requestedStatus = position;
            }
            else {
                requestedStatus = -1;
            }
            refreshMyAdapter();
        }
    }

    private void refreshMyAdapter() {
        currentRequestsList = changeArrayByStatus(requestsByFilterList, requestedStatus);
        if (currentRequestsList.size() == 0) // means its empty
            tvNoRequests.setVisibility(View.VISIBLE);
        else
            tvNoRequests.setVisibility(View.INVISIBLE); // means that there is Requests in the list
        requestAdapter=new RequestAdapter(this,0, currentRequestsList);
        lv.setAdapter(requestAdapter); // reset the adapter
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { // don't do nothing

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //String type = user.getType();
        getMenuInflater().inflate(R.menu.requests_filter_menu, menu);
        menu = user.removeItemsIfNecessary(menu);
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onClick(View v) {

        if (v == dialogFilterBtnCancel) // cancel the dialog
            dialogFilter.dismiss();
        else if (v == dialogFilterBtnSubmit) // filter the array
        {
            if (validInput()) // checking if the input is valid
            {
                if (dialogFilterForWhat.equals("Child"))
                    requestsByFilterList = changeArrayListByChild (user.getRequests(), dialogFilterEtFiled.getText().toString()); // filter the array to the filed child
                else if (dialogFilterForWhat.equals("Parent"))
                    requestsByFilterList = changeArrayListByParent (user.getRequests(), dialogFilterEtFiled.getText().toString()); // filter the array to the filed parent
                else if (dialogFilterForWhat.equals("Month"))
                    requestsByFilterList = changeArrayListByMonth(user.getRequests(), Integer.valueOf(dialogFilterEtFiled.getText().toString())); // filter the array to the filed month
                else if (dialogFilterForWhat.equals("Reason"))
                    requestsByFilterList = changeArrayListByReason(user.getRequests(), dialogFilterEtFiled.getText().toString()); // filter the array to the filed reason
                refreshMyAdapter(); // refresh adapter
                dialogFilter.dismiss();
            }
        }
        else if (v == dialogRequestBtnCancel)
            dialogRequest.dismiss();
        else if (v == dialogRequestBtnApprove)
        {
            if (user instanceof Teacher)
            {
                Toast.makeText(this, "Approved", Toast.LENGTH_LONG).show();
                currentRequest.setStatus(1);
                updateAndRefresh();
            }
            else
                dialogRequest.dismiss();
        }
        else if (v == dialogRequestBtnDeny)
        {
            if (user instanceof Teacher)
            {
                Toast.makeText(this, "Reject", Toast.LENGTH_LONG).show();
                currentRequest.setStatus(-1);
                updateAndRefresh();
            }

            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert!");
                builder.setMessage("Are you sure you want to delete request number " +currentRequest.getId()  + " from requests list?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new ClassDialogAlertDelete());
                builder.setNegativeButton("No", new ClassDialogAlertDelete());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        else if (v == btnBack)
        {
            Intent intent = getIntent();
            intent.putExtra("User", user);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

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

                if (user instanceof Teacher)
                    createAlertDialog();
                else
                    finish();
                break;

            case R.id.action_all_requests:

                requestsByFilterList = user.getRequests();
                refreshMyAdapter();
                break;

            case R.id.action_manage_profile:

                Intent intent = new Intent(this, ManageProfile.class);
                intent.putExtra("User", user);
                startActivityForResult(intent, SCREEN_MANAGE_PROFILE);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

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

    private class SetDate implements DatePickerDialog.OnDateSetListener {

        public void onDateSet(DatePicker view, int year, int month, int day) {

            month++; // instead of index 0-11
            Toast.makeText(UserActivityWatchRequests.this, "You selected the date: " + day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            requestsByFilterList = changeArrayListByDay(user.getRequests(), day, month, year); // change the array by the filed day
            refreshMyAdapter(); // refresh adapter
        }
    }

    private ArrayList<Request> changeArrayListByDay (ArrayList<Request> arrayList, int day, int month, int year) // changing array by selected day
    {
        ArrayList<Request> newArray = new ArrayList<>();

        for (int i = 0; i<arrayList.size(); i++) // goes over the filed array
        {
            if ((arrayList.get(i).getDate().getDayOfMonth() == day) && (arrayList.get(i).getDate().getMonthValue() == month) && (arrayList.get(i).getDate().getYear() == year))
                newArray.add(arrayList.get(i)); // adding to the new array the Requests that there date is the filed day
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

    public class ClassDialogAlert implements DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which) {

            if (which == -1) {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    public class ClassDialogAlertDelete implements DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which) {

            if (which == -1) {
                databaseTools.open();
                databaseTools.deleteRequestById(currentRequest.getId());
                databaseTools.close();
                user.getRequests().remove(currentRequest);
                //user.deleteRequest(currentRequest);
                updateAndRefresh();
            }
        }
    }

    private boolean validInput ()
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
            if (filed.length() < 3){
                dialogFilterTvNotExist.setVisibility(View.VISIBLE);
                isOk = false;
            }
            else
                dialogFilterTvNotExist.setVisibility(View.INVISIBLE);
        }

        return isOk;
    }

    private void updateAndRefresh ()
    {
        databaseTools.open();
        databaseTools.updateRequestStatus(currentRequest, currentRequest.getStatus());
        databaseTools.close();
        refreshMyAdapter();
        dialogRequest.dismiss();
    }

    private void createAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new ClassDialogAlert());
        builder.setNegativeButton("No", new ClassDialogAlert());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SCREEN_MANAGE_PROFILE)
        {
            user = (Teacher) data.getExtras().get("Teacher");
        }
    }
}