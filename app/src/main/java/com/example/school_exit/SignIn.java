package com.example.school_exit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    EditText etId, etPass;
    TextView tvRegisterNow, tvId, tvPassword;
    Button btnSubmit;
    DatabaseTools databaseTools;
    public static final int DONT_HAVE_ACCOUNT_SCREEN = 0;
    public static final int PARENT_FIRST_SCREEN = 1;
    public static final int USER_WATCH_REQUESTS = 2;
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        etId = findViewById(R.id.signInScreenEtId);
        etPass = findViewById(R.id.signInScreenEtPassword);
        tvRegisterNow = findViewById(R.id.signInScreenTvRegisterNow);
        btnSubmit = findViewById(R.id.signInScreenBtnSubmit);
        tvId = findViewById(R.id.singInScreenTvId);
        tvPassword = findViewById(R.id.signInScreenTvPassword);

        tvRegisterNow.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        databaseTools = new DatabaseTools(this);

        //Request request1 = new Request(LocalDateTime.of(2021,12,17,0,0), 21658, "sick", "haha", 0);
        //Request request2 = new Request(LocalDateTime.of(2021,12,18,0,0), 77328, "test", "haha bariboa", 0);

        //LocalDateTime localDateTime = LocalDateTime.of(2020,10,10,0,0);
        //Request request = new Request(localDateTime, 21658, "sick", "haha", 0);

        /*
        Child ori = new Child("111111111", "Ori", "Meiri", "222222222", "6-6");
        Child oren = new Child("333333333", "Oren", "Israeli", "444444444", "6-6");
        Child nemo = new Child ("555555555", "Nehoray", "Madar", "666666666", "6-6");
        Child roy = new Child ("777777777", "Roy-Elkana", "Hatton", "888888888", "6-4");
        Child tal = new Child ("999999999", "Tal", "Peretz", "123456789", "6-4");

        Request request1 = new Request(LocalDateTime.of(2021, 12,17, 0, 0), "111111111", "sick", "non", 0);
        Request request2 = new Request(LocalDateTime.of(2021, 11,18, 0, 0), "333333333", "sick", "non", 0);
        Request request3 = new Request(LocalDateTime.of(2021, 1,22, 0, 0), "555555555", "sick", "non", 0);
        Request request4 = new Request(LocalDateTime.of(2021, 8,1, 0, 0), "777777777", "sick", "non", 0);
        Request request5 = new Request(LocalDateTime.of(2021, 6,16, 0, 0), "999999999", "sick", "non", 0);

        databaseTools.open();
        databaseTools.insertChild(ori);
        databaseTools.insertChild(oren);
        databaseTools.insertChild(nemo);
        databaseTools.insertChild(roy);
        databaseTools.insertChild(tal);
        databaseTools.insertRequest(request1);
        databaseTools.insertRequest(request2);
        databaseTools.insertRequest(request3);
        databaseTools.insertRequest(request4);
        databaseTools.insertRequest(request5);
        databaseTools.close();

         */

        tvId.setVisibility(View.INVISIBLE);
        tvPassword.setVisibility(View.INVISIBLE);
/*
        Parent parent = new Parent("214798922", "Ori", "Meiri", "0586620011");
        parent.addChild(new Child("111222333", "Ori", "Meiri", "214798922", "6-2"));
        parent.addChild(new Child("111222444", "Amit", "Meiri", "214798922", "6-3"));
        Request request1 = new Request(LocalDateTime.of(2021, 12,17, 0, 0), "111222333", "sick", "non", 0);
        Request request2 = new Request(LocalDateTime.of(2021, 11,18, 0, 0), "111222444", "sick", "non", 0);
        parent.addRequest(request1);
        parent.addRequest(request2);

        List<Request> parentRequests;
        parentRequests = parent.getRequests();
        Log.d("data1", "its work");
        Log.d("data1", "children is: " + parent.getChildren());
        Log.d("data1", "requests is: " + parentRequests);

 */

        receiveSMSPermission();

        BroadcastReceiver br = new SMSreceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(br, filter);

    }





    private void receiveSMSPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_RECEIVE_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }


    @Override
    public void onClick(View v) {
        if (v == tvRegisterNow) // create account
        {
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.putExtra("Id", etId.getText().toString());
            intent.putExtra("Password", etPass.getText().toString());
            startActivityForResult(intent, DONT_HAVE_ACCOUNT_SCREEN);
        }
        else if (v == btnSubmit) //sign in
        {
            if (validInput())
            {
                databaseTools.open();
                String[] passwordAndKind =  databaseTools.getPasswordById(etId.getText().toString());
                databaseTools.close();
                if (passwordAndKind == null)
                    Toast.makeText(this, "Id does'nt exist", Toast.LENGTH_LONG).show();
                else
                {
                    String parentOrTeacher = passwordAndKind[1];
                    String password = passwordAndKind[0];

                    if (! isPasswordMachTheId(password))
                        Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
                    else
                    {
                        if (parentOrTeacher.equals("parent"))
                        {
                            moveToParentFirstScreen();
                        }
                        else
                        {
                            moveToTeacherScreen();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PARENT_FIRST_SCREEN && resultCode == RESULT_CANCELED)
        {
            etId.setText("");
            etPass.setText("");
        }

        else if( requestCode == USER_WATCH_REQUESTS)
        {
            etId.setText("");
            etPass.setText("");
        }
    }

    private boolean isPasswordMachTheId (String password)
    {
        if (password.equals(etPass.getText().toString()))
            return true;
        return false;
    }
    private void moveToParentFirstScreen ()
    {
        Intent intent = new Intent(this, ParentFirstScreenActivity.class);
        intent.putExtra("Id", etId.getText().toString());
        startActivityForResult(intent, PARENT_FIRST_SCREEN);
    }
    private void moveToTeacherScreen ()
    {
        Intent intent = new Intent(this, UserActivityWatchRequests.class);
        //intent.putExtra("Id", etId.getText().toString());
        databaseTools.open();
        Teacher teacher = databaseTools.getTeacherById(etId.getText().toString());
        databaseTools.close();
        intent.putExtra("User", (User)teacher);
        startActivityForResult(intent, USER_WATCH_REQUESTS);
    }

    private boolean validInput ()
    {
        String id = etId.getText().toString();
        String password = etPass.getText().toString();
        boolean isOk = true;

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
        return isOk;
    }
}