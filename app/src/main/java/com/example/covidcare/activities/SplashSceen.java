package com.example.covidcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.covidcare.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashSceen extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sceen);
        mAuth=FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(mAuth.getCurrentUser()!=null)
                {
                    i=new Intent(SplashSceen.this, MainActivity.class);
                }
                else {
                    i = new Intent(SplashSceen.this, SignInOrRegister.class);

                }
                startActivity(i);
                finish();
            }
        }, 500);
    }
}