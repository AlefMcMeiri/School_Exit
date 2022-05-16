package com.example.school_exit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ParentAddKids extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    Spinner spinnerGrade, spinnerNumber;
    List<String> arrayListGrades, arrayListNumbers;
    Button btnAddKid, btnFinish;
    int grade, number;
    DatabaseTools databaseTools;
    EditText etFirstName, etLastName, etId;
    TextView tvFirstName, tvLastName, tvId;
    Parent parent;
    int howManyTimesChecked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_add_kids);

        databaseTools = new DatabaseTools(this);

        spinnerGrade = findViewById(R.id.parentAddKidsScreenSpinnerGrade);
        spinnerNumber = findViewById(R.id.parentAddKidsScreenSpinnerNumber);
        btnAddKid = findViewById(R.id.parentAddChildrenBtnAddKid);
        btnFinish = findViewById(R.id.parentAddChildrenBtnFinish);
        etFirstName = findViewById(R.id.parentAddKidsScreenEtFirstName);
        etLastName = findViewById(R.id.parentAddKidsScreenEtLastName);
        etId = findViewById(R.id.parentAddKidsScreenEtId);
        tvFirstName = findViewById(R.id.parentAddKidsScreenTvFirstName);
        tvLastName = findViewById(R.id.parentAddKidsScreenTvLastName);
        tvId = findViewById(R.id.parentAddKidsScreenTvId);

        tvFirstName.setVisibility(View.INVISIBLE);
        tvLastName.setVisibility(View.INVISIBLE);
        tvId.setVisibility(View.INVISIBLE);

        btnAddKid.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        arrayListGrades = new ArrayList<String>();
        arrayListNumbers = new ArrayList<String>();

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


        ArrayAdapter<String> dataAdapterGrades = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListGrades);
        ArrayAdapter<String> dataAdapterNumbers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayListNumbers);

        spinnerGrade.setAdapter(dataAdapterGrades);
        spinnerNumber.setAdapter(dataAdapterNumbers);

        spinnerGrade.setOnItemSelectedListener(this);
        spinnerNumber.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        parent = (Parent) intent.getExtras().get("Parent");
        /*
        databaseTools.open();
        String parentId = (String) intent.getExtras().get("Id");
        parent = databaseTools.getParentByPersonalId(parentId); // get the id from first screen
        databaseTools.close();
        */

        Log.d("data1", "ParentAddKids, Parent Is: " + parent.toString());

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        if(parent.getId() == R.id.parentAddKidsScreenSpinnerGrade)
        {
            grade = Integer.valueOf(item);
            if (howManyTimesChecked < 2)
                howManyTimesChecked++;
            else
                Toast.makeText(this, "Your choose grade " + item,Toast.LENGTH_SHORT).show();
        }
        else if(parent.getId() == R.id.parentAddKidsScreenSpinnerNumber)
        {
            number = Integer.valueOf(item);
            if (howManyTimesChecked < 2)
                howManyTimesChecked++;
            else
                Toast.makeText(this, "Your choose number of class " + item,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v == btnAddKid)
        {
            if (validInput())
            {
                String parentId = parent.getPersonalId();
                String kidClass = "" + grade + "-" + number;
                Child child = new Child(etId.getText().toString(), etFirstName.getText().toString(), etLastName.getText().toString(), parentId, kidClass);

                databaseTools.open();
                boolean isIdExist = databaseTools.isIdAlreadyExist(child.getPersonalId());
                databaseTools.close();
                if (isIdExist)
                    Toast.makeText(this, "Id already exist", Toast.LENGTH_LONG).show();

                else {
                    databaseTools.open();
                    databaseTools.insertChild(child);
                    databaseTools.close();
                    parent.addChild(child);

                    Toast.makeText(this, child.getFirstName() + " has added to children list successfully", Toast.LENGTH_LONG).show();
                    etId.setText("");
                    etFirstName.setText("");
                    etLastName.setText("");

                }
            }
        }
        else if (v == btnFinish)
        {
            Intent intent = getIntent();
            intent.putExtra("Parent", parent);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean validInput() {
        boolean isOk = true;
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String id = etId.getText().toString();
        if (firstName.equals("") || firstName.length() < 2){
            tvFirstName.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvFirstName.setVisibility(View.INVISIBLE);
        if (lastName.equals("") || lastName.length() < 2){
            tvLastName.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvLastName.setVisibility(View.INVISIBLE);
        if (id.equals("") || id.length() != 9){
            tvId.setVisibility(View.VISIBLE);
            isOk = false;
        }
        else
            tvId.setVisibility(View.INVISIBLE);
        return isOk;
    }
}