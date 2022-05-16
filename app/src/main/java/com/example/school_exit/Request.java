package com.example.school_exit;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;

public class Request implements Serializable {

    private long id;
    private LocalDateTime date;
    private String childPersonalId;
    private String reason;
    private String note;
    private int status;

    public Request (LocalDateTime date, String childPersonalId, String reason, String note, int status)
    {
        this.id = -1;
        this.date = date;
        this.childPersonalId = childPersonalId;
        this.reason = reason;
        this.note = note;
        this.status = status;
    }

    public Request (long id, LocalDateTime date, String childPersonalId, String reason, String note, int status)
    {
        this.id = id;
        this.date = date;
        this.childPersonalId = childPersonalId;
        this.reason = reason;
        this.note = note;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getChildPersonalId() {
        return childPersonalId;
    }

    public void setChildPersonalId(String childPersonalId) {
        this.childPersonalId = childPersonalId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString ()
    {
        return "Child Id: " + getChildPersonalId() +
                ", Reason: " + getReason() +
                ", Note:" + getNote() +
                ", ID: " + getId();
    }

}
