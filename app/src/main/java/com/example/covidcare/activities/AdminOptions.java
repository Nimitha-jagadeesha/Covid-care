package com.example.covidcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.covidcare.R;
import com.example.covidcare.Utility.NetworkQueueSingleton;
import com.example.covidcare.Utility.URLExpert;
import com.example.covidcare.Utility.VolleyErrorHandle;
import com.example.covidcare.dataexpert.StateExpert;
import com.example.covidcare.models.HospitalData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminOptions extends AppCompatActivity {
    Spinner selectStateSpinner;
    DatabaseReference databaseReference;
    String selectedRegion;
    EditText hospitalNameEditText;
    EditText numberOfBedsEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        bindViews();
        getData();

        selectStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                selectedRegion=item.toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }

    private void getData()
    {
        StateExpert.clearAllStates();
        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    URLExpert.getAllStates(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            parseData(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyErrorHandle.handleError(error, getApplicationContext());
                        }
                    }
            );

            NetworkQueueSingleton.geInstance(this).addToRequestQueue(request);
        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void parseData(JSONObject response)
    {

        try {
            JSONObject data=response.getJSONObject("data");
            JSONArray value=data.getJSONArray("regional");
            for(int i=0;i<value.length();i++)
            {
                JSONObject joke=((JSONObject)value.get(i));
                String state=joke.getString("state");
                StateExpert.statesList.add(state);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, StateExpert.statesList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectStateSpinner.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindViews() {
        selectStateSpinner=findViewById(R.id.selectStatesSpinner);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("data");
        hospitalNameEditText=findViewById(R.id.HospitalNameAdminOptions);
        numberOfBedsEditText=findViewById(R.id.BedAvailabilityAdminOptions);
    }
    public void onClickUpdate(View v){
       if(hospitalNameEditText.getText().toString().trim().isEmpty())
       {
           hospitalNameEditText.setError("This field cannot be empty");
           return;
       }
       if(numberOfBedsEditText.getText().toString().trim().isEmpty())
       {
           numberOfBedsEditText.setError("This field cannot be empty");
           return;
       }
       addToDatabase(hospitalNameEditText.getText().toString(),numberOfBedsEditText.getText().toString());
    }

    private void addToDatabase(String hospitalName, String NumberOfBeds) {
        hospitalName=hospitalName.toLowerCase();
        databaseReference.child(selectedRegion).child(hospitalName).setValue(new HospitalData(NumberOfBeds,hospitalName));
        Toast.makeText(this,"Updated",Toast.LENGTH_LONG).show();
    }
}