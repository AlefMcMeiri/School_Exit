package com.example.school_exit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Dialog dialog;
    EditText etFirstName, etLastName, etPhoneNumber, etId, etPassword, etConfirmPassword;
    RadioButton rbTeacher, rbParent;
    Spinner spinnerGrade, spinnerNumber;
    Button btnCreateAccount, btnDialogSubmit, btnDialogCancel;
    TextView tvFirstName, tvLastName, tvPhoneNumber, tvId, tvPassword, tvConfirmPassword, tvRb, tvPicture;
    DatabaseTools databaseTools;
    List<String> arrayListGrades, arrayListNumbers;
    ArrayAdapter<String> spinnerGradeAdapter, spinnerNumberAdapter;
    int grade, number;
    String teacherClass = "";
    ImageButton ibPicture;
    Bitmap bitmap;
    boolean thereIsAPicture = false;
    int howManyTimesChecked = 0;

    public static final int SCREEN_IMAGE = 38;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // get all the relevant widgets

        databaseTools = new DatabaseTools(this);

        etFirstName = findViewById(R.id.signUpScreenEtFirstName);
        etLastName = findViewById(R.id.signUpScreenEtLastName);
        etPhoneNumber = findViewById(R.id.signUpScreenEtPhoneNumber);
        etId = findViewById(R.id.signUpScreenEtPersonalId);
        etPassword = findViewById(R.id.signUpScreenEtPassword);
        etConfirmPassword = findViewById(R.id.signUpScreenEtConfirmPassword);
        rbTeacher = findViewById(R.id.signUpScreenRadioButtonTeacher);
        rbParent = findViewById(R.id.signUpScreenRadioButtonParent);
        btnCreateAccount = findViewById(R.id.signUpScreenBtnCreateAccount);
        tvFirstName = findViewById(R.id.singUpScreenTvFirstName);
        tvLastName = findViewById(R.id.singUpScreenTvLastName);
        tvPhoneNumber = findViewById(R.id.singUpScreenTvPhoneNumber);
        tvId = findViewById(R.id.singUpScreenTvId);
        tvPassword = findViewById(R.id.singUpScreenTvPassword);
        tvConfirmPassword = findViewById(R.id.singUpScreenTvConfirmPassword);
        tvRb = findViewById(R.id.singUpScreenTvRb);
        ibPicture = findViewById(R.id.signUpScreenIbPicture);
        tvPicture = findViewById(R.id.singUpScreenTvPicture);

        tvFirstName.setVisibility(View.INVISIBLE);
        tvLastName.setVisibility(View.INVISIBLE);
        tvPhoneNumber.setVisibility(View.INVISIBLE);
        tvId.setVisibility(View.INVISIBLE);
        tvPassword.setVisibility(View.INVISIBLE);
        tvConfirmPassword.setVisibility(View.INVISIBLE);
        tvRb.setVisibility(View.INVISIBLE);
        tvPicture.setVisibility(View.INVISIBLE);

        btnCreateAccount.setOnClickListener(this);
        ibPicture.setOnClickListener(this);

        Intent intent = getIntent();
        etId.setText((String) intent.getExtras().get("Id")); // get the id from first screen
        etPassword.setText((String) intent.getExtras().get("Password")); // get the password from first screen

        arrayListGrades = new ArrayList<String>();
        arrayListNumbers = new ArrayList<String>();

        for (int i = 1; i<=6; i++){
            arrayListGrades.add(String.valueOf(i));
            arrayListNumbers.add(String.valueOf(i));
        }

        spinnerGradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListGrades);
        spinnerNumberAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListNumbers);

    }

    @Override
    public void onClick(View v) {
        if (btnCreateAccount == v) {

            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String id = etId.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String password = etPassword.getText().toString();
            boolean isTeacher = rbTeacher.isChecked();

            if (validInput()){
                databaseTools.open();
                boolean isIdExist = databaseTools.isIdAlreadyExist(id);
                databaseTools.close();
                if (isIdExist)
                    Toast.makeText(this, "Id already exist", Toast.LENGTH_LONG).show();
                else
                {
                    if (isTeacher)
                        createDialog();

                    else {
                        Parent parent = new Parent(id, firstName, lastName, phoneNumber);
                        parent.setBitmap(ImageUtils.bitmapToString(bitmap));
                        databaseTools.open();
                        databaseTools.insertParent(parent, password);
                        databaseTools.close();

                        Intent intent = new Intent(this, ParentAddKids.class);
                        intent.putExtra("Parent", parent);
                        startActivity(intent);

                        finish();

                    }
                }
            }
        }
        else if (v == ibPicture)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, SCREEN_IMAGE);
        }
        else if (v == btnDialogCancel)
        {

            dialog.dismiss();
        }
        else if (v == btnDialogSubmit)
        {
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String id = etId.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String password = etPassword.getText().toString();

            teacherClass = "" + grade + "-" + number;
            Log.d("data1", "your class is: " + teacherClass);

            databaseTools.open();
            boolean isTeacherExist = databaseTools.isTeacherExist(teacherClass);
            databaseTools.close();

            if (isTeacherExist)
                Toast.makeText(this, "There is already a teacher for this class", Toast.LENGTH_LONG).show();
            else {
                Teacher teacher = new Teacher(id, firstName, lastName, phoneNumber, teacherClass);
                teacher.setBitmap(ImageUtils.bitmapToString(bitmap));
                databaseTools.open();
                databaseTools.insertTeacher(teacher, password);
                databaseTools.close();
                dialog.dismiss();
                finish();
            }

        }
    }

    private void createDialog() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_your_class);
        dialog.setTitle("Home class");
        dialog.setCancelable(false);

        /*
        rbArrayGrade = new RadioButton[] {dialog.findViewById(R.id.rbGrade1), dialog.findViewById(R.id.rbGrade2), dialog.findViewById(R.id.rbGrade3),
                dialog.findViewById(R.id.rbGrade4), dialog.findViewById(R.id.rbGrade5), dialog.findViewById(R.id.rbGrade6)};
        rbArrayNumber = new RadioButton[] {dialog.findViewById(R.id.rbNumber1), dialog.findViewById(R.id.rbNumber2), dialog.findViewById(R.id.rbNumber3),
                dialog.findViewById(R.id.rbNumber4), dialog.findViewById(R.id.rbNumber5), dialog.findViewById(R.id.rbNumber6)};
         */

        btnDialogSubmit = dialog.findViewById(R.id.fillYourClassScreenBtnSubmit);
        btnDialogCancel = dialog.findViewById(R.id.fillYourClassScreenBtnCancel);
        spinnerGrade = dialog.findViewById(R.id.fillYourClassScreenSpinnerGrade);
        spinnerNumber = dialog.findViewById(R.id.fillYourClassScreenSpinnerNumber);

        spinnerGrade.setAdapter(spinnerGradeAdapter);
        spinnerNumber.setAdapter(spinnerNumberAdapter);

        spinnerGrade.setOnItemSelectedListener(this);
        spinnerNumber.setOnItemSelectedListener(this);
        btnDialogCancel.setOnClickListener(this);
        btnDialogSubmit.setOnClickListener(this);

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED)
        {

        }
        else if (resultCode == RESULT_OK && requestCode == SCREEN_IMAGE)
        {
            thereIsAPicture = true;
            bitmap = (Bitmap) data.getExtras().get("data");
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            ibPicture.setBackground(bd);
        }
    }

    private boolean validInput()
    {

        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String id = etId.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        boolean isOk = true;

        if (firstName.length() < 2) { // check if firstName is two digits or more
            tvFirstName.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvFirstName.setVisibility(View.INVISIBLE);
        if (lastName.length() < 2) {
            tvLastName.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvLastName.setVisibility(View.INVISIBLE);
        if (phoneNumber.length() != 10) {
            tvPhoneNumber.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvPhoneNumber.setVisibility(View.INVISIBLE);
        if (id.length() != 9) {
            tvId.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvId.setVisibility(View.INVISIBLE);
        if (password.length() < 6) {
            tvPassword.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvPassword.setVisibility(View.INVISIBLE);
        if (!confirmPassword.equals(etPassword.getText().toString())) {
            tvConfirmPassword.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvConfirmPassword.setVisibility(View.INVISIBLE);

        if (!rbParent.isChecked() && !rbTeacher.isChecked()) {
            tvRb.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvRb.setVisibility(View.INVISIBLE);

        if (thereIsAPicture == false){
            isOk = false;
            tvPicture.setVisibility(View.VISIBLE);
        }
        else
            tvPicture.setVisibility(View.INVISIBLE);

        return isOk;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        if(parent.getId() == R.id.fillYourClassScreenSpinnerGrade)
        {
            grade = Integer.valueOf(item);
            if (howManyTimesChecked < 2)
                howManyTimesChecked++;
            else
                Toast.makeText(this, "You chose grade " + item,Toast.LENGTH_SHORT).show();
        }
        else if(parent.getId() == R.id.fillYourClassScreenSpinnerNumber)
        {
            number = Integer.valueOf(item);
            if (howManyTimesChecked < 2)
                howManyTimesChecked++;
            else
                Toast.makeText(this, "You chose number of class " + item,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}