package com.example.school_exit;

import java.io.Serializable;

public class Child extends Person implements Serializable {

    //
    private String parentPersonalId;
    private String numberClass;

    public Child (String personalId, String firstName, String lastName, String parentPersonalId, String numberClass) // constructor
    {
        super(personalId, firstName, lastName);
        this.parentPersonalId = parentPersonalId;
        this.numberClass = numberClass;
    }

    public String getParentPersonalId() {
        return parentPersonalId;
    } // Parent id getter

    public void setParentPersonalId(String parentPersonalId) { this.parentPersonalId = parentPersonalId; } // Parent id setter

    public String getNumberClass() {
        return numberClass;
    } // Number of class getter

    public void setNumberClass(String numberClass) { // Number of class setter
        this.numberClass = numberClass;
    }

    @Override
    public String toString() { // toString
        return "Child{" +
                "personalId=" + getPersonalId() +
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", parentPersonalId=" + parentPersonalId +
                ", numberClass=" + numberClass +
                '}';
    }
}
