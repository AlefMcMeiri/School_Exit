package com.example.school_exit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ParentFirstScreenActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnToWatchRequests, btnToCreateRequest;
    DatabaseTools databaseTools;
    TextView tvWelcome;
    Parent parent;
    public static final int SCREEN_CREATE_REQUEST = 3;
    public static final int SCREEN_PROFILE = 16;
    public static final int SCREEN_WATCH_CHILDREN = 17;
    public static final int SCREEN_ADD_KIDS = 18;
    public static final int SCREEN_WATCH_REQUESTS = 19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_first_screen);

        databaseTools = new DatabaseTools(this);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnToWatchRequests = findViewById(R.id.btnWatchRequestsPARENT);
        btnToCreateRequest = findViewById(R.id.btnCreateRequest);

        btnToWatchRequests.setOnClickListener(this);
        btnToCreateRequest.setOnClickListener(this);

        Intent intent = getIntent();

        databaseTools.open();
        parent = databaseTools.getParentByPersonalId((String) intent.getExtras().get("Id"));
        databaseTools.close();

        tvWelcome.setText("Welcome back " + parent.getFirstName() + "!");
        Log.d("data1", "Parent is: " + parent.getFirstName() + " " + parent.getLastName());

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.parent_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class ClassDialogAlert implements DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which) {

            if (which == -1) {
                setResult(RESULT_CANCELED);
                finish();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == SCREEN_ADD_KIDS || requestCode == SCREEN_PROFILE || requestCode == SCREEN_CREATE_REQUEST)
            {
                parent = (Parent) data.getExtras().get("Parent");
                tvWelcome.setText("Welcome back " + parent.getFirstName() + "!");
            }
            else if (requestCode == SCREEN_WATCH_REQUESTS)
            {
                parent = (Parent) data.getExtras().get("User");
            }
        }


    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        databaseTools.open();

        switch (item.getItemId()) {

            case R.id.action_manage_profile:
                Intent intent = new Intent (this, ManageProfile.class);
                intent.putExtra("User", parent);
                startActivityForResult(intent, SCREEN_PROFILE);
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

            default:
                break;
        }
        databaseTools.close();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v == btnToWatchRequests)
        {
            Intent intent = new Intent(this, UserActivityWatchRequests.class);
            //intent.putExtra("Id", parent.getPersonalId());
            intent.putExtra("User", (User)parent);
            startActivityForResult(intent, SCREEN_WATCH_REQUESTS);
        }
        else if (v == btnToCreateRequest)
        {
            if (parent.getChildren().size() == 0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No children found");
                builder.setMessage("Do you want to add kids? ");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new ClassDialogAlertNoChildren());
                builder.setNegativeButton("No", new ClassDialogAlertNoChildren());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else
            {
                Intent intent = new Intent(this, ParentCreateRequest.class);
                //intent.putExtra("Id", parent.getPersonalId());
                intent.putExtra("Parent", parent);
                startActivityForResult(intent, SCREEN_CREATE_REQUEST);
            }

        }
    }

    public class ClassDialogAlertNoChildren implements DialogInterface.OnClickListener {

        public void onClick(DialogInterface alertDialog, int which) {

            if (which == -1) {
                Intent intent = new Intent(ParentFirstScreenActivity.this, ParentAddKids.class);
                intent.putExtra("Parent", parent);
                startActivityForResult(intent, SCREEN_ADD_KIDS);
            }

        }
    }
}