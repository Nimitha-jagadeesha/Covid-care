package com.example.covidcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.covidcare.R;
import com.example.covidcare.dataexpert.StateExpert;
import com.example.covidcare.models.HospitalData;
import com.example.covidcare.models.UsersData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOptions extends AppCompatActivity {
    Spinner selectStateSpinner;
    DatabaseReference databaseReference;
    String selectedRegion;
    EditText hospitalNameEditText;
    EditText numberOfBedsEditText;
    EditText phoneNumberEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        bindViews();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StateExpert.statesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectStateSpinner.setAdapter(arrayAdapter);

        selectStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                selectedRegion = item.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void bindViews() {
        selectStateSpinner = findViewById(R.id.selectStatesSpinner);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        hospitalNameEditText = findViewById(R.id.HospitalNameAdminOptions);
        numberOfBedsEditText = findViewById(R.id.BedAvailabilityAdminOptions);
        phoneNumberEditText=findViewById(R.id.phone_number);
    }

    public void onClickAdd(View v)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("admin");
        databaseReference.child(phoneNumberEditText.getText().toString()).setValue(new UsersData(true,phoneNumberEditText.getText().toString()));
    }

    public void onClickUpdate(View v) {
        if (hospitalNameEditText.getText().toString().trim().isEmpty()) {
            hospitalNameEditText.setError("This field cannot be empty");
            return;
        }
        if (numberOfBedsEditText.getText().toString().trim().isEmpty()) {
            numberOfBedsEditText.setError("This field cannot be empty");
            return;
        }
        addToDatabase(hospitalNameEditText.getText().toString(), numberOfBedsEditText.getText().toString());
    }

    private void addToDatabase(String hospitalName, String NumberOfBeds) {
        hospitalName = hospitalName.toLowerCase();
        databaseReference.child(selectedRegion).child(hospitalName).setValue(new HospitalData(NumberOfBeds, hospitalName,selectedRegion));
        if(!selectedRegion.equals("INDIA"))
            databaseReference.child("INDIA").child(hospitalName+" ("+selectedRegion+") ").setValue(new HospitalData(NumberOfBeds, hospitalName+" ("+selectedRegion+") ",selectedRegion));
        Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show();
    }
}