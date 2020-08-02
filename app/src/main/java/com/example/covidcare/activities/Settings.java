package com.example.covidcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.covidcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    Switch switchSetting;
    static boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bindViews();

        switchSetting.setChecked(checked);
        if (switchSetting != null)
            switchSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        checked = true;
                        saveSettings(true);
                        finish();
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        checked = false;
                        saveSettings(false);
                        finish();
                    }
                }
            });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onClickLogOut(View v) {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, SignInOrRegister.class));
    }

    public void onClickDeleteAccount(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setMessage("Do you want to delete?");
        builder.setCancelable(false);

        builder
                .setPositiveButton(
                        "Yes",
                        (dialog, which) -> {
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                            assert currentUser != null;
                            currentUser.delete().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(Settings.this, SignInOrRegister.class));
                                    Toast.makeText(Settings.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(e -> {

                            });
                        });

        // Set the Negative button
        builder
                .setNegativeButton(
                        "No",
                        (dialog, which) -> {

                            // If user click no
                            // then dialog box is canceled.
                            dialog.cancel();
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void saveSettings(boolean bool) {
        SharedPreferences sharedPreferences = getSharedPreferences("CovidCareSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Setting", bool);
        editor.apply();
    }

    private void bindViews() {
        switchSetting = findViewById(R.id.modeSwitch);
    }

}
