package com.example.covidcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

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
                    i=new Intent(SplashSceen.this,MainActivity.class);
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
