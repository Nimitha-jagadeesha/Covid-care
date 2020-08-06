package com.example.covidcare.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.covidcare.R;
import com.example.covidcare.models.UsersData;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class SignInOrRegister extends AppCompatActivity {
    FirebaseAuth mAuth;
    int AUTH_UI_REQUEST_CODE = 10001;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To get fading transformation effect
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_sign_in_or_register);

        // Binding All the Views
        bindViews();

        // If user has already logged in then starting MainActivity
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }


    // Binding All the Views
    private void bindViews() {
        mAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
    }

    // OnClick listener for Signin/Register Button
    public void onClickSignInOrRegister(View v) {
        //Setting a list of providers to signin in AutUi IdgConfig list
        List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        // Setting  Auth Ui builder by provider list created and starting that activity
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
                .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                .setAlwaysShowSignInMethodScreen(true)
                .setTheme(R.style.AppTheme)
                .setLogo(R.mipmap.icon)
                .build();
        startActivityForResult(intent, AUTH_UI_REQUEST_CODE);
    }

    // Function that will be called after the auth Ui action completed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Checking if this function called for auth ui action
        if (requestCode == AUTH_UI_REQUEST_CODE) {

            // On sucessfully logged in
            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                // Checking if user registered now or logged in
                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show();

                }

                Intent intent;
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finishAffinity();
                startActivity(intent);
                finish();
            } else {

            }
        }
    }
}
