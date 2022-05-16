package com.example.school_exit;

import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class User extends Person implements Serializable {

    private String phoneNumber;
    private ArrayList<Request> requests;
    private String bitmap;

    public User (String personalId, String firstName, String lastName, String phoneNumber)
    {
        super(personalId, firstName, lastName);
        this.phoneNumber = phoneNumber;
        requests = new ArrayList<Request>();
        this.bitmap = "";
    }

    public User (String personalId, String firstName, String lastName, String phoneNumber, String bitmap)
    {
        super(personalId, firstName, lastName);
        this.phoneNumber = phoneNumber;
        requests = new ArrayList<Request>();
        this.bitmap = bitmap;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Request> getRequests() {
        if (requests == null)
            requests = new ArrayList<Request>();
        return requests;
    }

    public void addRequest(Request request) {
        if (requests == null)
            requests = new ArrayList<Request>();
        requests.add(request);
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public void setRequests (ArrayList<Request> requests) {
        this.requests = requests;
    }

    public abstract String toString ();

    public abstract Menu removeItemsIfNecessary(Menu menu);

    public abstract Parent getParentByPersonalId (String personalId, DatabaseTools databaseTools);

    //public abstract boolean ableToPushApprove();
}
