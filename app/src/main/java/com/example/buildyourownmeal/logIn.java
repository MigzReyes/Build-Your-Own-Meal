package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

    databaseFunctions databaseFunctions;

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

        //DATABASE DECLARATION
        databaseFunctions = new databaseFunctions(this);

        //LOG IN VARIABLES CONNECTION TO LAYOUT BUTTONS
        email = findViewById(R.id.logInEmail);
        password = findViewById(R.id.logInPass);
        logInBtn = findViewById(R.id.logInBtn);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPass = findViewById(R.id.forgotPassword);

        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();

        if (userSession.getBoolean("rememberMe", false)) {
            String rememberMeEmail = userSession.getString("email", null);
            String rememberMePass = userSession.getString("password", null);

            email.setText(rememberMeEmail);
            password.setText(rememberMePass);
            rememberMe.setChecked(true);
        }

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = email.getText().toString().trim();
                String getPass = password.getText().toString().trim();
                boolean checkBox = rememberMe.isChecked();

                if (getEmail.isBlank() || getPass.isBlank()) {
                    popUpAlert(getString(R.string.fillUpAllInputFieldsError));
                } else {
                    Boolean checkUserEmail = databaseFunctions.checkEmail(getEmail);

                    if (checkUserEmail) {
                        boolean checkPassword = databaseFunctions.checkPassword(getPass);

                        if (checkPassword) {
                            if (checkBox) {
                                editor.putString("email", getEmail);
                                editor.putString("password", getPass);
                                editor.putBoolean("rememberMe", true);
                            } else {
                                editor.clear();
                            }
                            editor.putBoolean("isUserLoggedIn", true);
                            editor.apply();

                            Intent intent = new Intent(logIn.this, Navbar.class);
                            startActivity(intent);
                            finish();
                        } else {
                            popUpAlert(getString(R.string.wrongPassworError));
                        }

                    } else {
                        popUpAlert(getString(R.string.emailDoesNotExistError));
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


    public void popUpAlert(String alertMessage) {
        Dialog popUpAlert;
        Button close;
        TextView alertText;

        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_alerts);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);
        popUpAlert.show();

        alertText = popUpAlert.findViewById(R.id.alertText);
        close = popUpAlert.findViewById(R.id.closeBtn);

        //ALERT TEXT
        alertText.setText(alertMessage);

        //CLOSE
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAlert.dismiss();
            }
        });

    }
}