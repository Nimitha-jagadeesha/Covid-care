package com.example.covidcare.models;

public class HospitalData {
    String numberOfBeds;
    String HospitalName;

    public HospitalData() {
    }


    public HospitalData(String numberOfBeds, String hospitalName) {
        this.numberOfBeds = numberOfBeds;
        HospitalName = hospitalName;
    }


    public String getNumberOfBeds() {
        return numberOfBeds;
    }

    public String getHospitalName() {
        return HospitalName;
    }
}
