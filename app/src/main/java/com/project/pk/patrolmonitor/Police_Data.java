package com.project.pk.patrolmonitor;

public class Police_Data {
    private String Name;
    private String Email;
    private String Contact;
    private String DeviceID;
    private String Status;

    public Police_Data() {
        Name = null;
        Email = null;
        Contact = null;
        DeviceID = null;
        Status = null;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


}
