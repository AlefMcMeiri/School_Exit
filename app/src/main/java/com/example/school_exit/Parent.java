package com.example.school_exit;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Parent extends User implements Serializable {


    private ArrayList<Child> children;

    public Parent(String personalId, String firstName, String lastName, String phoneNumber, String bitmap)
    {
        super(personalId, firstName, lastName, phoneNumber, bitmap);
        children = new ArrayList<Child>();
    }

    public Parent(String personalId, String firstName, String lastName, String phoneNumber)
    {
        super(personalId, firstName, lastName, phoneNumber);
        children = new ArrayList<Child>();
    }

    @Override
    public String toString() {

        return "Parent{" +
                "personalId=" + getPersonalId() +
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", phoneNumber=" + getPhoneNumber() +
                ", children="  + children +
                ", requests=" + getRequests() +
                '}';
    }

    public Menu removeItemsIfNecessary(Menu menu)
    {
        Log.d("data1", "ParentClass, removing Items from menu");
        MenuItem item1 = menu.findItem(R.id.action_by_parent);
        MenuItem item2 = menu.findItem(R.id.action_by_day);
        MenuItem item3 = menu.findItem(R.id.action_exit);
        MenuItem item4 = menu.findItem(R.id.action_manage_profile);
        item1.setVisible(false);
        item2.setVisible(false);
        item3.setVisible(false);
        item4.setVisible(false);

        return menu;
    }

    public Parent getParentByPersonalId (String personalId, DatabaseTools databaseTools)
    {
        Log.d("data1", "ParentClass, getting the Parent from the class");
        return this;
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

    public ArrayList<Child> getChildren() {
        if (children == null)
            children = new ArrayList<Child>();
        return children;
    }

    public void addChild(Child child) {
        if (children == null)
            children = new ArrayList<Child>();
        children.add(child);
    }

    /*
    public boolean ableToPushApprove()
    {
        return false;
    }

     */
}
