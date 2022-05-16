package com.example.school_exit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManageProfile extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Dialog dialog;
    TextView tvFirstName, tvLastName, tvPhoneNumber, tvPassword, tvNoKids, tvId, tvClass, tvChildren, tvChildName, tvChildNumber, tvChildClass, tvTeacher, dialogTvFirstName, dialogTvLastName, dialogTvId;
    Button btnAddKids, btnFinishParent, btnFinishTeacher, btnUpdate, dialogBtnUpdate, dialogBtnCancel, dialogBtnDelete;
    EditText etFirstName, etLastName, etPhoneNumber, etPassword, dialogEtFirstName, dialogEtLastName;
    Spinner dialogSpGrade, dialogSpNumberOfGrade;
    ListView lv;
    Child currentChild;
    ChildAdapter childAdapter;
    ArrayList<Child> currentChildren;
    DatabaseTools databaseTools;
    User user;
    Bitmap bitmap;
    ImageButton ibPicture;
    LinearLayout linearLayout;
    int grade, number;

    public static final int SCREEN_ADD_KIDS = 4;
    public static final int SCREEN_IMAGE = 47;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_profile);

        linearLayout = findViewById(R.id.manageProfileLlTeacher);
        tvChildClass = findViewById(R.id.tvClass);
        tvChildNumber = findViewById(R.id.tvChildNumber);
        tvChildName = findViewById(R.id.tvChildName);
        tvTeacher = findViewById(R.id.tvTeacher);
        tvChildren = findViewById(R.id.manageProfileTvChildren);
        tvClass = findViewById(R.id.manageProfileTvClass);
        tvId = findViewById(R.id.manageProfileTvId);
        etPassword = findViewById(R.id.manageProfileEtPassword);
        btnUpdate = findViewById(R.id.manageProfileBtnUpdate);
        ibPicture = findViewById(R.id.manageProfileIbPicture);
        etFirstName = findViewById(R.id.manageProfileEtFirstName);
        etLastName = findViewById(R.id.manageProfileEtLastName);
        etPhoneNumber = findViewById(R.id.manageProfileEtPhoneNumber);
        tvNoKids = findViewById(R.id.manageProfileTvNoChildren);
        btnAddKids = findViewById(R.id.manageProfileBtnAddKids);
        btnFinishParent = findViewById(R.id.manageProfileBtnFinishParent);
        btnFinishTeacher = findViewById(R.id.manageProfileBtnFinishTeacher);
        lv = findViewById(R.id.manageProfileLvChildren);
        databaseTools = new DatabaseTools(this);
        tvFirstName = findViewById(R.id.manageProfileTvFirstName);
        tvLastName = findViewById(R.id.manageProfileTvLastName);
        tvPhoneNumber = findViewById(R.id.manageProfileTvPhoneNumber);
        tvPassword = findViewById(R.id.manageProfileTvPassword);

        btnAddKids.setOnClickListener(this);
        btnAddKids.setOnClickListener(this);
        btnFinishParent.setOnClickListener(this);
        btnFinishTeacher.setOnClickListener(this);
        ibPicture.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        Intent intent = getIntent();
        user = (User) intent.getExtras().get("User");

        /*
        String parentId = (String) intent.getExtras().get("Id");
        databaseTools.open();
        classParent = databaseTools.getParentByPersonalId(parentId);
        databaseTools.close();
        */

        tvId.setText("Id: " + user.getPersonalId());
        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etPhoneNumber.setText(user.getPhoneNumber());
        databaseTools.open();
        etPassword.setText(databaseTools.getPasswordById(user.getPersonalId())[0]);
        databaseTools.close();
        BitmapDrawable bd = new BitmapDrawable(ImageUtils.stringToBitmap(user.getBitmap()));
        ibPicture.setBackground(bd);
        bitmap = ImageUtils.stringToBitmap(user.getBitmap());
        tvFirstName.setVisibility(View.INVISIBLE);
        tvLastName.setVisibility(View.INVISIBLE);
        tvPhoneNumber.setVisibility(View.INVISIBLE);
        tvPassword.setVisibility(View.INVISIBLE);

        if (user instanceof Parent)
        {
            btnFinishTeacher.setVisibility(View.INVISIBLE);
            tvClass.setVisibility(View.INVISIBLE);

            currentChildren = ((Parent)user).getChildren();

            childAdapter = new ChildAdapter(this, R.layout.manage_profile, ((Parent)user).getChildren());
            lv.setAdapter(childAdapter);

            if (((Parent) user).getChildren().size() == 0) {
                tvNoKids.setVisibility(View.VISIBLE);
            }
            else {
                tvNoKids.setVisibility(View.INVISIBLE);
            }

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    currentChild = ((Parent) user).getChildren().get(position);
                    createDialog();
                }
            });
        }
        else if (user instanceof Teacher)
        {
            tvClass.setText("Class: " + ((Teacher) user).getNumberOfClass());
            tvChildren.setVisibility(View.INVISIBLE);
            tvChildName.setVisibility(View.INVISIBLE);
            tvChildNumber.setVisibility(View.INVISIBLE);
            tvChildClass.setVisibility(View.INVISIBLE);
            tvTeacher.setVisibility(View.INVISIBLE);
            lv.setVisibility(View.INVISIBLE);
            tvNoKids.setVisibility(View.INVISIBLE);
            btnAddKids.setVisibility(View.INVISIBLE);
            btnFinishParent.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public void onClick(View v)
    {
        if (v == btnAddKids)
        {
            Intent intent = new Intent(this, ParentAddKids.class);
            intent.putExtra("Parent", (Parent)user);
            startActivityForResult(intent, SCREEN_ADD_KIDS);
        }
        else if (v == ibPicture)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, SCREEN_IMAGE);
        }
        else if (v == btnFinishParent)
        {
            Intent intent = getIntent();
            intent.putExtra("Parent", (Parent)user);
            setResult(RESULT_OK, intent);
            finish();
        }
        else if (v == btnFinishTeacher)
        {
            Intent intent = getIntent();
            intent.putExtra("Teacher", (Teacher)user);
            setResult(RESULT_OK, intent);
            finish();
        }
        else if (v == btnUpdate)
        {
            if (updateUserValidInput())
            {
                String updatedFirstName = etFirstName.getText().toString();
                String updatedLastName = etLastName.getText().toString();
                String updatedPhoneNumber = etPhoneNumber.getText().toString();
                String updatedBitmap = ImageUtils.bitmapToString(bitmap);

                user.setFirstName(updatedFirstName);
                user.setLastName(updatedLastName);
                user.setPhoneNumber(updatedPhoneNumber);
                user.setBitmap(updatedBitmap);

                if (user instanceof Parent)
                {
                    databaseTools.open();
                    databaseTools.updateParent((Parent)user, etPassword.getText().toString());
                    databaseTools.close();

                    Toast.makeText(this, "Personal details has updated successfully", Toast.LENGTH_LONG).show();
                }
                else if (user instanceof Teacher)
                {
                    databaseTools.open();
                    databaseTools.updateTeacher((Teacher)user, etPassword.getText().toString());
                    databaseTools.close();

                    Toast.makeText(this, "Personal details has updated successfully", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (v == dialogBtnCancel)
        {
            dialog.dismiss();
        }
        else if (v == dialogBtnDelete)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert!");
            builder.setMessage("Are you sure you want to delete " + currentChild.getFirstName() + " from children list?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new ClassDialogAlert());
            builder.setNegativeButton("No", new ClassDialogAlert());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if (v == dialogBtnUpdate)
        {
            if (dialogValidInput())
            {
                String firstName = dialogEtFirstName.getText().toString();
                String lastName = dialogEtLastName.getText().toString();
                String parentId = currentChild.getParentPersonalId();
                String kidClass = "" + grade + "-" + number;
                Child child = new Child(currentChild.getPersonalId(), firstName, lastName, parentId, kidClass);

                currentChild.setFirstName(firstName);
                currentChild.setLastName(lastName);
                currentChild.setNumberClass(kidClass);

                databaseTools.open();
                databaseTools.updateChild(child);
                databaseTools.close();

                refreshMyAdapter();

                Toast.makeText(this,  "Child updated successfully", Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        }
    }

    private boolean updateUserValidInput() {
        boolean isOk = true;

        if (etFirstName.getText().toString().length() < 2){
            isOk = false;
            tvFirstName.setVisibility(View.VISIBLE);
        }
        else
            tvFirstName.setVisibility(View.INVISIBLE);
        if (etLastName.getText().toString().length() < 2){
            isOk = false;
            tvLastName.setVisibility(View.VISIBLE);
        }
        else
            tvLastName.setVisibility(View.INVISIBLE);
        if (etPhoneNumber.getText().toString().length() != 10) {
            isOk = false;
            tvPhoneNumber.setVisibility(View.VISIBLE);
        }
        else
            tvPhoneNumber.setVisibility(View.INVISIBLE);
        if (etPassword.getText().toString().length() < 6) {
            isOk = false;
            tvPassword.setVisibility(View.VISIBLE);
        }
        else
            tvPassword.setVisibility(View.INVISIBLE);

        return isOk;

    }

    public void refreshMyAdapter()
    {
        currentChildren = ((Parent) user).getChildren();
        if (currentChildren.size() == 0)
            tvNoKids.setVisibility(View.VISIBLE);
        else
            tvNoKids.setVisibility(View.INVISIBLE);
        childAdapter = new ChildAdapter(this,0, currentChildren);
        lv.setAdapter(childAdapter);
    }

    private void createDialog() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_parent_update_child);
        dialog.setCancelable(false);

        // tracking all the relevant views
        dialogBtnCancel = dialog.findViewById(R.id.parentUpdateChildBtnCancel);
        dialogBtnDelete = dialog.findViewById(R.id.parentUpdateChildBtnDelete);
        dialogBtnUpdate = dialog.findViewById(R.id.parentUpdateChildBtnUpdate);
        dialogEtFirstName = dialog.findViewById(R.id.parentUpdateChildEtFirstName);
        dialogEtLastName = dialog.findViewById(R.id.parentUpdateChildEtLastName);
        dialogTvId = dialog.findViewById(R.id.parentUpdateChildTvId);
        dialogTvFirstName = dialog.findViewById(R.id.parentUpdateChildTvFirstName);
        dialogTvLastName = dialog.findViewById(R.id.parentUpdateChildTvLastName);
        dialogSpGrade = dialog.findViewById(R.id.parentUpdateChildSpinnerGrade);
        dialogSpNumberOfGrade = dialog.findViewById(R.id.parentUpdateChildSpinnerNumber);

        dialogTvLastName.setVisibility(View.INVISIBLE);
        dialogTvFirstName.setVisibility(View.INVISIBLE);

        // on click listener
        dialogBtnCancel.setOnClickListener(this);
        dialogBtnUpdate.setOnClickListener(this);
        dialogBtnDelete.setOnClickListener(this);

        List<String> arrayListGrades = new ArrayList<>();
        List<String> arrayListNumbers = new ArrayList<>();

        for (int i = 1; i<=6; i++){
            arrayListGrades.add(String.valueOf(i));
            arrayListNumbers.add(String.valueOf(i));
        }
        /*
        arrayListGrades.add("1");
        arrayListGrades.add("2");
        arrayListGrades.add("3");
        arrayListGrades.add("4");
        arrayListGrades.add("5");
        arrayListGrades.add("6");
        arrayListNumbers.add("1");
        arrayListNumbers.add("2");
        arrayListNumbers.add("3");
        arrayListNumbers.add("4");
        arrayListNumbers.add("5");
        arrayListNumbers.add("6");

         */

        ArrayAdapter<String> dataAdapterGrades = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListGrades);
        ArrayAdapter<String> dataAdapterNumbers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListNumbers);

        dialogSpGrade.setAdapter(dataAdapterGrades);
        dialogSpNumberOfGrade.setAdapter(dataAdapterNumbers);

        dialogSpGrade.setOnItemSelectedListener(this);
        dialogSpNumberOfGrade.setOnItemSelectedListener(this);

        dialogEtFirstName.setText(currentChild.getFirstName());
        dialogEtLastName.setText(currentChild.getLastName());
        dialogTvId.setText("Id: " + currentChild.getPersonalId());

        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        if(parent.getId() == R.id.parentUpdateChildSpinnerGrade)
        {
            grade = Integer.valueOf(item);
        }
        else if(parent.getId() == R.id.parentUpdateChildSpinnerNumber)
        {
            number = Integer.valueOf(item);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean dialogValidInput() {
        boolean isOk = true;
        String firstName = dialogEtFirstName.getText().toString();
        String lastName = dialogEtLastName.getText().toString();
        if (firstName.equals("") || firstName.length() < 2){
            dialogTvFirstName.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            dialogTvFirstName.setVisibility(View.INVISIBLE);
        if (lastName.equals("") || lastName.length() < 2){
            dialogTvLastName.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            dialogTvLastName.setVisibility(View.INVISIBLE);
        return isOk;
    }

    public class ClassDialogAlert implements DialogInterface.OnClickListener {

        public void onClick(DialogInterface alertDialog, int which) {

            if (which == -1) {

                Log.d("data1", "user before deleting child " + user);
                databaseTools.open();
                ArrayList<Request> arrayList = databaseTools.deleteRequestsByChildId(currentChild.getPersonalId());
                databaseTools.deleteChildById(currentChild.getPersonalId());
                databaseTools.close();

                Log.d("data1", "user requests before deleting requests: " + user);
                Log.d("data1", "array of requests is: " + arrayList);
                Log.d("data1", "AAAAAAAAAAAAA   requests before: " + user.getRequests());
                if (arrayList != null) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        Log.d("data1", "removing request: " + arrayList.get(i));
                        // TODO: create method that find the index of the request that i want to delete
                        int indexOfRequest = getIndexogRequest(arrayList.get(i));
                        if (indexOfRequest == -1)
                        {
                            Log.d("data1", "the request " + arrayList.get(i) + " aren't exist in the user's requests list");
                        }
                        else
                            user.getRequests().remove(indexOfRequest); // TODO: don't do anything
                    }
                }
                Log.d("data1", "AAAAAAAAAAAAA   requests after: " + user.getRequests());

                ((Parent) user).getChildren().remove(currentChild);

                refreshMyAdapter();
                Toast.makeText(ManageProfile.this, currentChild.getFirstName() + " deleted from the children list successfully", Toast.LENGTH_LONG).show();

                Log.d("data1", "user after deleting everything" + user);

                dialog.dismiss();
            }

        }

        private int getIndexogRequest(Request request) {
            for (int i = 0; i < user.getRequests().size(); i++)
            {
                if (user.getRequests().get(i).getId() == request.getId())
                    return i;
            }
            return -1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCREEN_ADD_KIDS && resultCode == RESULT_OK)
        {
            user= (Parent) data.getExtras().get("Parent");
            refreshMyAdapter();
        }
        else if (resultCode == RESULT_OK && requestCode == SCREEN_IMAGE)
        {
            bitmap = (Bitmap) data.getExtras().get("data");
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            ibPicture.setBackground(bd);
        }

    }
}