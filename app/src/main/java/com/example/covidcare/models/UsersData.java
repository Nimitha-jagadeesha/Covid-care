package com.example.covidcare.models;

public class UsersData {
    private boolean admin;
    private String id;

    public UsersData() {
    }

    public UsersData(boolean admin, String id) {
        this.admin = admin;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean getAdmin() {
        return admin;
    }
}
