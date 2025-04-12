package com.example.buildyourownmeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class forgot_password extends AppCompatActivity {

    //VARIABLE DECLARATION
    EditText email;
    Button reqResetLink;
    TextView backBtnLogIn;
    CardView fabBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        //REFERENCE
        email = findViewById(R.id.email);
        reqResetLink = findViewById(R.id.reqResetLink);
        backBtnLogIn = findViewById(R.id.backBtnLogIn);
        fabBackBtn = findViewById(R.id.fabBackBtn);

        //GET TEXT
        String emailText = email.getText().toString().trim();

        //EMAIL
        reqResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailText.isEmpty()) {
                    Toast.makeText(forgot_password.this, getString(R.string.enterEmailAddress), Toast.LENGTH_LONG).show();
                } /* else {
                    //DATABASE INSERT
                } */
            }
        });

        //BACK BUTTON
        fabBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgot_password.this, logIn.class);
                startActivity(intent);
            }
        });

        backBtnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgot_password.this, logIn.class);
                startActivity(intent);
            }
        });

    }
}