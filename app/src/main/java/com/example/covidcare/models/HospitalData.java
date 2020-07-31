package com.example.covidcare.models;

public class HospitalData {
    String numberOfBeds;
    String hospitalName;
    String region;

    public HospitalData() {
    }

    public HospitalData(String numberOfBeds, String hospitalName, String region) {
        this.numberOfBeds = numberOfBeds;
        this.hospitalName = hospitalName;
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
}
