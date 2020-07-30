package com.example.covidcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.covidcare.R;

public class AdminOptions extends AppCompatActivity {
    Spinner selectStateSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        bindViews();
    }

    private void bindViews() {
        selectStateSpinner=findViewById(R.id.selectStatesSpinner);
    }
}