package com.example.school_exit;

import android.util.Log;
import android.view.Menu;

import java.io.Serializable;
import java.util.ArrayList;

public class Teacher extends User implements Serializable{

    private String numberOfClass;

    public Teacher (String personalId, String firstName, String lastName, String phoneNumber, String numberClass, String bitmap)
    {
        super(personalId, firstName, lastName, phoneNumber, bitmap);
        this.numberOfClass = numberClass;
    }

    public Teacher (String personalId, String firstName, String lastName, String phoneNumber, String numberClass)
    {
        super(personalId, firstName, lastName, phoneNumber);
        this.numberOfClass = numberClass;
    }

    public String getNumberOfClass() {
        return numberOfClass;
    }

    public void setNumberOfClass(String numberClass) {
        this.numberOfClass = numberClass;
    }


    @Override
    public String toString() {
        return "Teacher{" +
                "personalId=" + getPersonalId() +
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", phoneNumber=" + getPhoneNumber() +
                ", numberOfClass=" + numberOfClass +
                ", requests=" + getRequests() +
                '}';
    }

    public Menu removeItemsIfNecessary (Menu menu)
    {
        Log.d("data1", "TeacherClass, don't removing nothing from the menu");
        return menu;
    }

    public Parent getParentByPersonalId (String personalId, DatabaseTools databaseTools)
    {
        Log.d("data1", "TeacherClass, getting the Parent from the filed data base");
        databaseTools.open();
        Parent parent = databaseTools.getParentByPersonalId(personalId);
        databaseTools.close();

        return parent;
    }

    /*
    public boolean ableToPushApprove()
    {
        return true;
    }

     */



}
