package com.example.covidcare.dataexpert;

import com.example.covidcare.models.HospitalData;

import java.util.ArrayList;

public class HospitalExpert {
    private static ArrayList<HospitalData> hospitalsAndBedCountList = new ArrayList<HospitalData>();

    public static void clearListData() {
        hospitalsAndBedCountList.clear();
    }

    public static int getHospitalsCount() {
        return hospitalsAndBedCountList.size();
    }

    public static String getHospitalName(int position) {
        return hospitalsAndBedCountList.get(position).getHospitalName();
    }

    public static String getNumberOfBedsAvailable(int position) {
        return hospitalsAndBedCountList.get(position).getNumberOfBeds();
    }

    public static void addHospitalData(HospitalData data) {
        hospitalsAndBedCountList.add(data);
    }

    public static String getHospitalAddress(int position) {
        return hospitalsAndBedCountList.get(position).getAddress();
    }

    public static String getHospitalNumber(int position) {
        return hospitalsAndBedCountList.get(position).getPhoneNumber();
    }
}
