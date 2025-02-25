package com.example.buildyourownmeal;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signUp extends AppCompatActivity {

    //VARIABLES
    private Button signUpBtn;
    private TextView logInLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        //SET ID
        signUpBtn = findViewById(R.id.signUpBtn);
        logInLink = findViewById(R.id.alreadyHaveAcc);

        //ONCLICK FOR SIGNUP
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, logIn.class);
                startActivity(intent);
            }
        });

        //ONCLICK FOR LOG IN LINK
        logInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, logIn.class);
                startActivity(intent);
            }
        });


    }
}