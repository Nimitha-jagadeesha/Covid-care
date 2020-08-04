package com.example.covidcare.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminOptions extends AppCompatActivity {
    Spinner selectRegionToAddSpinner;
    Spinner selectRegionToDeleteSpinner;
    DatabaseReference databaseReference;
    String selectedRegionToAdd;
    String selectedRegionToDelete;
    EditText hospitalNameToUpdateEditText;
    EditText hospitalNameToDeleteEditText;
    EditText numberOfBedsEditText;
    EditText NumberOrEmailEditText;
    EditText addressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        bindViews();


        selectRegionToAddSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                selectedRegionToAdd = item.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        selectRegionToDeleteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                selectedRegionToDelete = item.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void bindViews() {
        selectRegionToAddSpinner = findViewById(R.id.selectStatesSpinner);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        hospitalNameToUpdateEditText = findViewById(R.id.HospitalNameAdminOptions);
        numberOfBedsEditText = findViewById(R.id.BedAvailabilityAdminOptions);
        NumberOrEmailEditText = findViewById(R.id.phone_number);
        addressEditText = findViewById(R.id.AddressAdminOptions);
        hospitalNameToDeleteEditText = findViewById(R.id.HospitalNameToDelete);
        selectRegionToDeleteSpinner = findViewById(R.id.SpinnerForDelete);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, StateExpert.statesList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectRegionToAddSpinner.setAdapter(arrayAdapter);
        selectRegionToDeleteSpinner.setAdapter(arrayAdapter);
    }

    public void onClickAdd(View v) {
        try {
            String data = NumberOrEmailEditText.getText().toString();
            if (data.isEmpty()) {
                NumberOrEmailEditText.setError("Please enter this field");
                return;
            }
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("admin");
            String id = FirebaseDatabase.getInstance().getReference().push().getKey();
            assert id != null;
            databaseReference.child(id).setValue(new UsersData(data, id));
            NumberOrEmailEditText.setText("");
            Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void onClickUpdate(View v) {

        String hospitalName = hospitalNameToUpdateEditText.getText().toString().trim();
        String numberOfBeds = numberOfBedsEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        if (hospitalName.isEmpty()) {
            hospitalNameToUpdateEditText.setError("This field cannot be empty");
            return;
        }
        if (numberOfBeds.isEmpty()) {
            numberOfBedsEditText.setError("This field cannot be empty");
            return;
        }
        if (address.isEmpty()) {
            addressEditText.setError("This field cannot be empty");
            return;
        }

        addToDatabase(hospitalName, numberOfBeds, address);
    }

    private void addToDatabase(String hospitalName, String NumberOfBeds, String address) {
        hospitalName = hospitalName.toLowerCase();
        databaseReference.child(selectedRegionToAdd).child(hospitalName).setValue(new HospitalData(NumberOfBeds, hospitalName, address, selectedRegionToAdd));
        if (!selectedRegionToAdd.equals("INDIA"))
            databaseReference.child("INDIA").child(hospitalName + " (" + selectedRegionToAdd + ") ").setValue(new HospitalData(NumberOfBeds, hospitalName + " (" + selectedRegionToAdd + ") ", address, selectedRegionToAdd));
        hospitalNameToUpdateEditText.setText("");
        numberOfBedsEditText.setText("");
        addressEditText.setText("");
        if (StateExpert.getSize() != 0) {
            selectRegionToAddSpinner.setSelection(0);
        }
        Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show();
    }

    public void onClickDeleteAdmin(View v) {
        String data = NumberOrEmailEditText.getText().toString().trim();
        if (data.isEmpty()) {
            NumberOrEmailEditText.setError("This field cannot be empty");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminOptions.this);
        builder.setMessage("Are you sure to remove the admin privileges of the user " + data);
        builder.setTitle("Alert !");
        builder.setCancelable(false);

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                Query query = FirebaseDatabase.getInstance().getReference("admin")
                                        .orderByChild("emailOrNumber")
                                        .equalTo(data);
                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                UsersData userData = snapshot.getValue(UsersData.class);
                                                FirebaseDatabase.getInstance().getReference().child("admin/" + userData.getId()).removeValue();
                                                NumberOrEmailEditText.setText("");
                                                Toast.makeText(AdminOptions.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        } else
                                            Toast.makeText(AdminOptions.this, "Specified admin doesn't exist", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                };
                                query.addListenerForSingleValueEvent(valueEventListener);
                            }
                        });

        // Set the Negative button
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // If user click no then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();

    }

    public void onClickDeleteHospital(View v) {
        String hospitalName = hospitalNameToDeleteEditText.getText().toString().trim().toLowerCase();
        if (hospitalName.isEmpty()) {
            hospitalNameToDeleteEditText.setError("Please Enter this Field");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminOptions.this);
        builder.setMessage("Are you sure to delete the details of " +hospitalName+" in "+selectedRegionToDelete );
        builder.setTitle("Alert !");
        builder.setCancelable(false);


        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                Query query = databaseReference.child(selectedRegionToDelete).child(hospitalName);
                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            databaseReference.child(selectedRegionToDelete).child(hospitalName).removeValue();
                                            if (!selectedRegionToDelete.equals("INDIA"))
                                                databaseReference.child("INDIA").child(hospitalName + " (" + selectedRegionToAdd + ") ").removeValue();
                                            hospitalNameToDeleteEditText.setText("");
                                            if (StateExpert.getSize() != 0) {
                                                selectRegionToDeleteSpinner.setSelection(0);
                                            }
                                            selectRegionToAddSpinner.setSelection(0);
                                            hospitalNameToDeleteEditText.setText("");

                                            Toast.makeText(AdminOptions.this, "Successfully deleted hospital information", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(AdminOptions.this, "Specified hospital doesn't exist", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                };
                                query.addListenerForSingleValueEvent(valueEventListener);
                            }
                        });

        // Set the Negative button
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();

    }
}