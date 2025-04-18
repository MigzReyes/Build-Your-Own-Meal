package com.example.buildyourownmeal;

import android.app.Dialog;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.ArrayList;
import java.util.List;

public class logIn extends AppCompatActivity {

    databaseFunctions databaseFunctions;

    //VARIABLES
    private Button logInBtn;
    private TextView signUpLink;

    //LOG IN VARIABLES
    private EditText password;
    private AutoCompleteTextView email;
    private CheckBox rememberMe;
    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        //DATABASE DECLARATION
        databaseFunctions = new databaseFunctions(this);

        //SHARED PREFERENCE
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();

        //LOG IN VARIABLES CONNECTION TO LAYOUT BUTTONS
        String username = userSession.getString("username", null);
        email = findViewById(R.id.logInEmail);
        password = findViewById(R.id.logInPass);
        logInBtn = findViewById(R.id.logInBtn);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPass = findViewById(R.id.forgotPassword);



        Map<String, ?> allEntries = userSession.getAll();

        List<String> emailList = new ArrayList<>(allEntries.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                emailList
        );

        email.setAdapter(adapter);

        email.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEmail = (String) parent.getItemAtPosition(position);
            String savedPassword = userSession.getString(selectedEmail, "");

            rememberMe.setChecked(true);
            password.setText(savedPassword);
        });


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
                                editor.putString(getEmail, getPass);
                                editor.putBoolean("rememberMe", true);
                            } else {
                                editor.clear();
                            }
                            editor.putString("username", username);
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
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
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