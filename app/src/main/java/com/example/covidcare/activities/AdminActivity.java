package com.example.covidcare.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.covidcare.R;
import com.example.covidcare.Utility.NetworkQueueSingleton;
import com.example.covidcare.Utility.URLExpert;
import com.example.covidcare.Utility.VolleyErrorHandle;
import com.example.covidcare.adaptors.HospitalsAdaptor;
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

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    Spinner selectStateSpinner;
    TextView conformedCasesTextView;
    TextView RecoveredCasesTextView;
    TextView DeathCasesTextView;
    DatabaseReference databaseHospitalData;
    RecyclerView recyclerViewHospitalList;
    HospitalsAdaptor adaptor;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_admin);

        bindViews();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        getData();

        // Navigation Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // To send supporting mail from fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "healthifySupport@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support request");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

               startActivity(new Intent(AdminActivity.this,MainActivity.class));
               finish();
                swipeRefresh.setRefreshing(false);
            }
        });

        // onSelect function of Spinner
        selectStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HospitalExpert.clearListData();
                Object item = parent.getItemAtPosition(position);
                Query query;
                query = FirebaseDatabase.getInstance().getReference("data/" + item.toString());
                query.addListenerForSingleValueEvent(valueEventListener);
                adaptor.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Event listener of Query
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            recyclerViewHospitalList.setVisibility(View.INVISIBLE);
            HospitalExpert.clearListData();
            if (dataSnapshot.exists()) {
                progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HospitalData hosdata = snapshot.getValue(HospitalData.class);
                    HospitalExpert.addHospitalData(hosdata);
                }
                adaptor.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }
            else
            progressBar.setVisibility(View.GONE);
            recyclerViewHospitalList.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };

    //On back press of navigation drawer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // OnSelect function of navigation Drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_share) {
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

        } else if (id == R.id.nav_admin) {
            try {
                startActivity(new Intent(this, AdminOptions.class));
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else if (id == R.id.settings) {
            startActivity(new Intent(this, Settings.class));
        } else if (id == R.id.about)
            startActivity(new Intent(this, About.class));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getData() {

        //GET request get all states
        StateExpert.clearAllStates();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URLExpert.getAllStates(),
                null,
                this::parseData,
                error -> VolleyErrorHandle.handleError(error, getApplicationContext())
        );

        NetworkQueueSingleton.geInstance(this).addToRequestQueue(request);

        // GET request to get corona updates
        JsonObjectRequest cases = new JsonObjectRequest(
                Request.Method.GET,
                URLExpert.getTotalCoronaCases(),
                null,
                this::parseCasesData,
                error -> VolleyErrorHandle.handleError(error, getApplicationContext())
        );

        NetworkQueueSingleton.geInstance(this).addToRequestQueue(cases);

    }


    // Parsing response to get states
    private void parseData(JSONObject response) {

        try {
            JSONObject data = response.getJSONObject("data");
            JSONArray value = data.getJSONArray("regional");
            for (int i = 0; i < value.length(); i++) {
                JSONObject joke = ((JSONObject) value.get(i));
                String state = joke.getString("state");
                StateExpert.statesList.add(state);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, StateExpert.statesList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectStateSpinner.setAdapter(arrayAdapter);
            if (StateExpert.getSize() != 0) {
                selectStateSpinner.setSelection(StateExpert.getSize() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // Parsing response to get number of corona cases
    private void parseCasesData(JSONObject response) {
        try {
            JSONObject cases = response.getJSONObject("confirmed");
            conformedCasesTextView.setText("Confirmed:\n" + cases.getInt("value"));
            cases = response.getJSONObject("recovered");
            RecoveredCasesTextView.setText("Recovered:\n" + cases.getInt("value"));
            cases = response.getJSONObject("deaths");
            DeathCasesTextView.setText("Deaths:\n" + cases.getInt("value"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // To bind all the views
    private void bindViews() {
        mAuth = FirebaseAuth.getInstance();
        selectStateSpinner = findViewById(R.id.selectStatesSpinner);
        conformedCasesTextView = findViewById(R.id.Confirmed_cases);
        RecoveredCasesTextView = findViewById(R.id.RecoveredCases);
        DeathCasesTextView = findViewById(R.id.DeathCases);
        databaseHospitalData = FirebaseDatabase.getInstance().getReference().child("data");
        recyclerViewHospitalList = findViewById(R.id.recyclerviewHospitalView);
        recyclerViewHospitalList.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new HospitalsAdaptor(this);
        recyclerViewHospitalList.setAdapter(adaptor);
        progressBar = findViewById(R.id.progressbar);
        swipeRefresh=findViewById(R.id.swipeRefresh);

    }
}