package com.example.buildyourownmeal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class introduction_screen extends AppCompatActivity {

    //VARIABLE DECLARATION
    Button logInBtn, signUpBtn, guestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_introduction_screen);

        //DATABASE
        databaseFunctions databaseFunctions = new databaseFunctions(this);
        boolean insertAdmin = databaseFunctions.insertAdminData();

        if (!insertAdmin) {
            Log.d("Admin", "Admin not created");
            databaseFunctions.close();
        }

        //SHARED PREFERENCE
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);

        //SHARED PREFERENCE GETTERS
        boolean isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);

        if (isUserLoggedIn) {
            Intent intent = new Intent(introduction_screen.this, Navbar.class);
            startActivity(intent);
            finish();
        }

        //REFERENCE
        logInBtn = findViewById(R.id.logInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        guestBtn = findViewById(R.id.guestBtn);

        //INTENT
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(introduction_screen.this, logIn.class);
                startActivity(intent);

                finish();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(introduction_screen.this, signUp.class);
                startActivity(intent);

                finish();
            }
        });



        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = userSession.edit();
                editor.putString("role", "guest");
                editor.putBoolean("isUserLoggedIn", false);
                editor.apply();
                Intent intent = new Intent(introduction_screen.this, Navbar.class);
                startActivity(intent);

                finish();
            }
        });

    }
}