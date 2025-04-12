package com.example.buildyourownmeal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class logIn extends AppCompatActivity {

    //VARIABLES
    private Button logInBtn;
    private TextView signUpLink;

    //LOG IN VARIABLES
    private EditText email, password;
    private CheckBox rememberMe;
    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        //LOG IN VARIABLES CONNECTION TO LAYOUT BUTTONS
        email = findViewById(R.id.logInEmail);
        password = findViewById(R.id.logInPass);
        logInBtn = findViewById(R.id.logInBtn);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPass = findViewById(R.id.forgotPassword);

        //SHARED PREFERENCE CONNECTION TO SIGN UP "myDb"
        SharedPreferences myDb = getSharedPreferences("myDb", MODE_PRIVATE);

        //GET THE EMAIL AND PASSWORD FROM THE SHARED PREFERENCE/DATABASE
        String dbUsername = myDb.getString("username", "No username found");
        String dbEmail = myDb.getString("email", "No email found");
        String dbPassword = myDb.getString("password", "No password found");

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = email.getText().toString().trim();
                String getPass = password.getText().toString().trim();
                Boolean checkBox = rememberMe.isChecked();

                if (getEmail.isBlank() || getPass.isBlank()) {
                    Toast.makeText(logIn.this, getString(R.string.fillUpAllInputFieldsError), Toast.LENGTH_SHORT).show();
                } else {
                    if (getEmail.equals(dbEmail)) {
                        if (getPass.equals(dbPassword)) {

                            SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
                            SharedPreferences.Editor editor = userSession.edit();
                            editor.putBoolean("isUserLoggedIn", true);
                            editor.putString("username", dbUsername);
                            editor.putString("email", dbEmail);
                            editor.apply();

                            Intent intent = new Intent(logIn.this, Navbar.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(logIn.this, getString(R.string.wrongPassworError), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(logIn.this, getString(R.string.emailDoesNotExistError), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //SIGN UP LINK
        //SET ID
        signUpLink = findViewById(R.id.dontHaveAccount);
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logIn.this, signUp.class);
                startActivity(intent);
            }
        });

        //BUTTON LINK "FORGOT PASSWORD"
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logIn.this, forgot_password.class);
                startActivity(intent);
            }
        });

    }
}