package com.example.covidcare.dataexpert;

import java.util.ArrayList;
//Class which contain list of states
public class StateExpert {
    public static ArrayList<String> statesList = new ArrayList<>();

    public static void clearAllStates() {
        statesList.clear();
    }

    public static int getSize() {
        return statesList.size();
    }
}
