package com.example.covidcare.models;

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
