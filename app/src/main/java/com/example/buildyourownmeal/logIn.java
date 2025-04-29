package com.example.buildyourownmeal;

import android.app.Dialog;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
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
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.ArrayList;
import java.util.List;

public class logIn extends AppCompatActivity {

    private databaseFunctions databaseFunctions;

    //VARIABLES
    private Button logInBtn;
    private TextView signUpLink;

    //LOG IN VARIABLES
    private EditText password;
    private AutoCompleteTextView email;
    private CheckBox rememberMe;
    private TextView forgotPass, guestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //DATABASE DECLARATION
        databaseFunctions = new databaseFunctions(this);

        //SHARED PREFERENCE
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();

        //LOG IN VARIABLES CONNECTION TO LAYOUT BUTTONS
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
                    //EMAIL VALIDATION
                    Boolean checkUserEmail = databaseFunctions.checkEmail(getEmail);

                    if (checkUserEmail) {
                        //PASSWORD VALIDATION
                        boolean checkPassword = databaseFunctions.checkPassword(getPass);

                        if (checkPassword) {
                            if (checkBox) {
                                editor.putString(getEmail, getPass);
                                editor.putBoolean("rememberMe", true);
                            } else {
                                editor.remove(getEmail);
                                editor.remove("rememberMe");
                            }
                            Cursor cursor = databaseFunctions.getUserInfo(getEmail);

                            if (cursor != null && cursor.moveToFirst()) {
                                //DATABASE GETTER
                                int dbUserId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                                String dbUsername = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                                String dbRole = cursor.getString(cursor.getColumnIndexOrThrow("role"));

                                //AUTHENTICATION
                                if (dbRole.equals("user")) {
                                    //STORE TO USER SESSION SHARED PREFERENCE
                                    editor.putInt("userId", dbUserId);
                                    editor.putString("username", dbUsername);
                                    editor.putString("email", getEmail);
                                    editor.putString("password", getPass);
                                    editor.putString("role", "user");
                                    editor.putBoolean("isUserLoggedIn", true);
                                    editor.apply();
                                    Intent intent = new Intent(logIn.this, Navbar.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else if (dbRole.equals("admin")){
                                    Intent intent = new Intent(logIn.this, admin.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                            } else {
                                popUpAlert(getString(R.string.sorryCanNotGetUserInfo));
                            }
                        } else {
                            popUpAlert(getString(R.string.wrongPassworError));
                        }

                    } else {
                        popUpAlert(getString(R.string.emailDoesNotExistError));
                    }
                }
            }
    });

        //GUEST BUTTON
        guestBtn = findViewById(R.id.guestBtn);
        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = userSession.edit();
                editor.putString("role", "guest");
                editor.putBoolean("isUserLoggedIn", false);
                editor.apply();
                Intent intent = new Intent(logIn.this, Navbar.class);
                startActivity(intent);

                finish();
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