package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class account extends AppCompatActivity {

    //VARIABLE DECLARATION
    databaseFunctions databaseFunctions;
    Dialog popUpAlert;
    ImageView backBtn, editUsername, editPassword, editEmail, editContactNumber;
    TextView sideActName, usernameProfile, passwordProfile, emailProfile, contactNumberProfile, deleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

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
        deleteAccount = findViewById(R.id.deleteAccount);


        //DATABASE INSTANTIATION
        databaseFunctions = new databaseFunctions(this);

        //SHARED PREFERENCE
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();

        //SHARED PREFERENCE GETTERS
        int getUserId = userSession.getInt("userId", 0);
        String getEmail = userSession.getString("email", null);

        //DATABASE GETTERS
        boolean dbCheckUserId = databaseFunctions.checkUserId(getUserId);

        if (dbCheckUserId) {
            Cursor dbGetUserInfo  = databaseFunctions.getUserInfo(getEmail);
            if (dbGetUserInfo != null && dbGetUserInfo.moveToFirst()) {
                String dbUsername = dbGetUserInfo.getString(dbGetUserInfo.getColumnIndexOrThrow("username"));
                String dbEmail = dbGetUserInfo.getString(dbGetUserInfo.getColumnIndexOrThrow("email"));
                String dbContactNumber = dbGetUserInfo.getString(dbGetUserInfo.getColumnIndexOrThrow("contactNumber"));

                usernameProfile.setText(dbUsername);
                emailProfile.setText(dbEmail);

                if (dbContactNumber == null || dbContactNumber.trim().isEmpty()) {
                    contactNumberProfile.setText(null);
                    dbGetUserInfo.close();
                } else {
                    contactNumberProfile.setText(dbContactNumber);
                    dbGetUserInfo.close();
                }
            } else {
                databaseFunctions.cannotRetrieveData();
            }
        }

        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_delete_account);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);

        //DELETE ACCOUNT
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button deleteAccBtn, cancelAccDeleteBtn;

                popUpAlert.show();

                deleteAccBtn = popUpAlert.findViewById(R.id.deleteAccBtn);
                deleteAccBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseFunctions.deleteAccount("account", getUserId);
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(account.this, introduction_screen.class);
                        startActivity(intent);
                        finish();
                    }
                });

                cancelAccDeleteBtn = popUpAlert.findViewById(R.id.cancelAccDeleteBtn);
                cancelAccDeleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpAlert.dismiss();
                    }
                });
            }
        });



        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, Navbar.class);
                startActivity(intent);
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
            }
        });

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, editPassword.class);
                startActivity(intent);
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, editEmail.class);
                startActivity(intent);
            }
        });

        editContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account.this, editContactNumber.class);
                startActivity(intent);
            }
        });
    }
}