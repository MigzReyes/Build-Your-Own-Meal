package com.example.buildyourownmeal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class account extends AppCompatActivity {

    //VARIABLE DECLARATION
    databaseFunctions databaseFunctions;
    ImageView backBtn, editUsername, editPassword, editEmail, editContactNumber;
    TextView sideActName, usernameProfile, passwordProfile, emailProfile, contactNumberProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        sideActName = findViewById(R.id.sideFragName);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editEmail = findViewById(R.id.editEmail);
        editContactNumber = findViewById(R.id.editContactNumber);
        usernameProfile = findViewById(R.id.usernameProfile);
        passwordProfile = findViewById(R.id.passwordProfile);
        emailProfile = findViewById(R.id.emailProfile);
        contactNumberProfile = findViewById(R.id.contactNumberProfile);


        //DATABASE INSTANTIATION
        databaseFunctions = new databaseFunctions(this);

        //SHARED PREFERENCE
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);

        //SHARED PREFERENCE GETTERS
        int getUserId = userSession.getInt("userId", 0);
        String getEmail = userSession.getString("email", null);

        //DATABASE GETTERS
        Cursor dbGetUserInfo  = databaseFunctions.getUserInfo(getEmail);
        boolean dbCheckUserId = databaseFunctions.checkUserId(getUserId);

        if (dbCheckUserId) {
            if (dbGetUserInfo != null && dbGetUserInfo.moveToFirst()) {
                String dbUsername = dbGetUserInfo.getString(dbGetUserInfo.getColumnIndexOrThrow("username"));
                String dbEmail = dbGetUserInfo.getString(dbGetUserInfo.getColumnIndexOrThrow("email"));
                String dbContactNumber = dbGetUserInfo.getString(dbGetUserInfo.getColumnIndexOrThrow("contactNumber"));

                usernameProfile.setText(dbUsername);
                emailProfile.setText(dbEmail);

                if (dbContactNumber == null || dbContactNumber.trim().isEmpty()) {
                    contactNumberProfile.setText(null);
                } else {
                    contactNumberProfile.setText(dbContactNumber);
                }
            } else {
                databaseFunctions.cannotRetrieveData();
            }
        }



        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, Navbar.class);
                startActivity(intent);
                finish();
            }
        });

        //ACTIVITY TITLE
        sideActName.setText(R.string.profile);

        //PAGE LINK
        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, editUsername.class);
                startActivity(intent);
                finish();
            }
        });

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, editPassword.class);
                startActivity(intent);
                finish();
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, editEmail.class);
                startActivity(intent);
                finish();
            }
        });

        editContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, editContactNumber.class);
                startActivity(intent);
                finish();
            }
        });
    }
}