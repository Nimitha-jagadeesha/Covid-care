package com.example.covidcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covidcare.R;
import com.example.covidcare.models.UsersData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOptions extends AppCompatActivity {
    EditText editTextAddUser;
    DatabaseReference databaseUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        bindViews();
    }

    private void bindViews() {
        editTextAddUser=findViewById(R.id.addUserCredential);
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
    }
    public void onClickAdd(View v)
    {
        String id = editTextAddUser.getText().toString();
        UsersData newUser = new UsersData(true,id);
        databaseUsers.child(id).setValue(newUser);
        Toast.makeText(this,"UserSucessfully added as a admin",Toast.LENGTH_LONG).show();
    }
}
