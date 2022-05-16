package com.example.school_exit;

import java.io.Serializable;

public class Person implements Serializable {

    private String personalId;
    private String firstName;
    private String lastName;

    public Person (String personalId, String firstName, String lastName)
    {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
