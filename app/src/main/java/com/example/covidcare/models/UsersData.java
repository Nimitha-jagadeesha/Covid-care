package com.example.covidcare.models;


//Model class for user data.

public class UsersData {
    private String emailOrNumber;
    private String id;

    public String getEmailOrNumber() {
        return emailOrNumber;
    }

    public String getId() {
        return id;
    }

    public UsersData() {
    }

    public UsersData(String emailOrNumber, String id) {
        this.emailOrNumber = emailOrNumber;
        this.id = id;
    }
}
