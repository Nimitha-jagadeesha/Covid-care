package com.example.covidcare.models;

public class HospitalData {
    private String numberOfBeds;
    private String hospitalName;
    private String address;
    private String region;

    public HospitalData() {
    }

    public HospitalData(String numberOfBeds, String hospitalName, String address, String region) {
        this.numberOfBeds = numberOfBeds;
        this.hospitalName = hospitalName;
        this.address = address;
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public String getNumberOfBeds() {
        return numberOfBeds;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getAddress() {
        return address;
    }
}
