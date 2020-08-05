package com.example.covidcare.models;

public class HospitalData {
    private String numberOfBeds;
    private String hospitalName;
    private String address;
    private String region;
    private String phoneNumber;

    public String getNumberOfBeds() {
        return numberOfBeds;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getAddress() {
        return address;
    }

    public String getRegion() {
        return region;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public HospitalData() {
    }

    public HospitalData(String numberOfBeds, String hospitalName, String address, String region, String phoneNumber) {
        this.numberOfBeds = numberOfBeds;
        this.hospitalName = hospitalName;
        this.address = address;
        this.region = region;
        this.phoneNumber = phoneNumber;
    }
}
