package com.example.covidcare.models;

public class UsersData {
    private boolean admin;
    private String phoneNumber;

    public UsersData() {
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UsersData(boolean admin, String phoneNumber) {
        this.admin = admin;
        this.phoneNumber = phoneNumber;
    }
}
