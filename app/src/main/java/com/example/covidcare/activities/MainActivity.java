package com.example.covidcare.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.covidcare.BuildConfig;
import com.example.covidcare.adaptors.HospitalsAdaptor;
import com.example.covidcare.R;
import com.example.covidcare.Utility.NetworkQueueSingleton;
import com.example.covidcare.Utility.URLExpert;
import com.example.covidcare.Utility.VolleyErrorHandle;
import com.example.covidcare.dataexpert.HospitalExpert;
import com.example.covidcare.dataexpert.StateExpert;
import com.example.covidcare.models.HospitalData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    Spinner selectStateSpinner;
    TextView conformedCasesTextView;
    TextView RecoveredCasesTextView;
    TextView DeathCasesTextView;
    DatabaseReference databaseHospitalData ;
    RecyclerView recyclerViewHospitalList;
    HospitalsAdaptor adaptor;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        bindViews();
        getData();
        mAuth = FirebaseAuth.getInstance();

        //Checking if its admin Account
        if (mAuth.getCurrentUser() != null && Objects.equals(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(), "nimitha1jagadeesha@gmail.com")) {
            startActivity(new Intent(MainActivity.this, AdminActivity.class));
            finish();
        } else if (mAuth.getCurrentUser() == null)
            startActivity(new Intent(this, SignInOrRegister.class));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "covidCareSupport@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support request");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        selectStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HospitalExpert.clearListData();
                Object item = parent.getItemAtPosition(position);
                Query query;
                query = FirebaseDatabase.getInstance().getReference("data/"+item.toString());
                query.addListenerForSingleValueEvent(valueEventListener);
                adaptor.notifyDataSetChanged();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerViewHospitalList.setVisibility(View.INVISIBLE);
            HospitalExpert.clearListData();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HospitalData hosdata = snapshot.getValue(HospitalData.class);
                    HospitalExpert.addHospitalData(hosdata);
                }
                adaptor.notifyDataSetChanged();


            }
            progressBar.setVisibility(View.INVISIBLE);
            recyclerViewHospitalList.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };



    private void bindViews() {
        selectStateSpinner = findViewById(R.id.selectStatesSpinner);
        conformedCasesTextView = findViewById(R.id.Confirmed_cases);
        RecoveredCasesTextView = findViewById(R.id.RecoveredCases);
        DeathCasesTextView = findViewById(R.id.DeathCases);
        databaseHospitalData=FirebaseDatabase.getInstance().getReference().child("data");
        recyclerViewHospitalList=findViewById(R.id.recyclerviewHospitalView);
        recyclerViewHospitalList.setLayoutManager(new LinearLayoutManager(this));
        adaptor =new HospitalsAdaptor(this);
        recyclerViewHospitalList.setAdapter(adaptor);
        progressBar=findViewById(R.id.progressbar);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, SignInOrRegister.class));
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out the app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);



        } else if (id == R.id.emergencyCall) {

            String s = "tel:1075";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(s));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return true;
            }
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getData() {
        StateExpert.clearAllStates();

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
        JsonObjectRequest cases = new JsonObjectRequest(
                Request.Method.GET,
                URLExpert.getTotalCoronaCases(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseCasesData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHandle.handleError(error, getApplicationContext());
                    }
                }
        );

        NetworkQueueSingleton.geInstance(this).addToRequestQueue(cases);

    }

    private void parseCasesData(JSONObject response) {
        try {
            JSONObject cases = response.getJSONObject("confirmed");
            conformedCasesTextView.setText("Confirmed:\n" + cases.getInt("value"));
            cases = response.getJSONObject("recovered");
            RecoveredCasesTextView.setText("Recovered:\n" + cases.getInt("value"));
            cases = response.getJSONObject("deaths");
            DeathCasesTextView.setText("Deaths:\n" + cases.getInt("value"));
        } catch (JSONException e) {
//        e.printStackTrace();
        }
    }

    private void parseData(JSONObject response) {

        try {
            JSONObject data = response.getJSONObject("data");
            JSONArray value = data.getJSONArray("regional");
            for (int i = 0; i < value.length(); i++) {
                JSONObject joke = ((JSONObject) value.get(i));
                String state = joke.getString("state");
                StateExpert.statesList.add(state);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StateExpert.statesList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectStateSpinner.setAdapter(arrayAdapter);
            if (StateExpert.getSize() != 0) {
                selectStateSpinner.setSelection(StateExpert.getSize() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}