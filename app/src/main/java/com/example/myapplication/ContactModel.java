package com.example.myapplication;

public class ContactModel {

    String id;
    String fullName;
    String phoneNumber;

    ContactModel(String id, String setFullName, String setPhoneNumber) {
        this.id = id;
        this.fullName = setFullName;
        this.phoneNumber = setPhoneNumber;
    }

    // tidak ada setId() karena ID langsung di generate dari Firestore

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
