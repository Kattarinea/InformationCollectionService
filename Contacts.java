package com.example.informationcollectionservice;

public class Contacts {
    private String id;
    private String name;
    private String phoneNumber;

    public String getId(){return id;}
    public String getName(){return name;}
    public String getPhoneNumber(){return phoneNumber;}

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
