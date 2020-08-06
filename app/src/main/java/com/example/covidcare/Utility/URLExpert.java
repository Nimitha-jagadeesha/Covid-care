package com.example.covidcare.Utility;
//This class contains all URL's.
public class URLExpert
{
    public static String getAllStates() {
        return "https://api.rootnet.in/covid19-in/hospitals/beds";
    }

    public static String getTotalCoronaCases() {
        return "https://covid19.mathdro.id/api/countries/India";
    }
}