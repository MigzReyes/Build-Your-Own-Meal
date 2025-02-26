package com.example.buildyourownmeal;

import static android.app.PendingIntent.getActivity;

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

public class signUp extends AppCompatActivity {

    //VARIABLES
    private Button signUpBtn;
    private TextView logInLink;

    //SIGN UP VARIABLES
    public EditText username;
    private EditText signUpEmail;
    private EditText password;
    private  EditText conPassword;
    private CheckBox termsAndCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        //SET ID
        signUpBtn = findViewById(R.id.signUpBtn);
        logInLink = findViewById(R.id.alreadyHaveAcc);

        //SIGN UP SET ID
        username = findViewById(R.id.username);
        signUpEmail = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);
        conPassword = findViewById(R.id.conPass);
        termsAndCon = findViewById(R.id.AgreeTermsAndCon);

        //SHARED PREFERENCE/DATABASE SIGN UP
        SharedPreferences myDb = getSharedPreferences("myDb", MODE_PRIVATE);
        SharedPreferences.Editor editor = myDb.edit();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //VARIABLE FOR GETTING THE TEXT FROM THE EDIT TEXT
                String name = username.getText().toString().trim();
                String email = signUpEmail.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String conPass = conPassword.getText().toString().trim();
                Boolean checkBox = termsAndCon.isChecked();

                //LOGIC STATEMENT FOR CHECKING IF THE USER HAS INPUT THE TEXT FIELD
                if (name.isBlank() || email.isBlank() || pass.isBlank() || conPass.isBlank()){
                    Toast.makeText(signUp.this, getString(R.string.fillUpAllInputFieldsError), Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(conPass)) {
                    Toast.makeText(signUp.this,  getString(R.string.conPassAndPassDoesNotMatchError), Toast.LENGTH_SHORT).show();
                } else if (!checkBox) {
                    Toast.makeText(signUp.this, getString(R.string.agreeToTheTermsAndConError), Toast.LENGTH_LONG).show();
                }else {
                    //STORE THE VALUE TO THE SHARED PREFERENCE
                    editor.putString("username", name);
                    editor.putString("email", email);
                    editor.putString("password", pass);
                    editor.apply();
                    //AFTER STORING IT REDIRECTS THE USER TO THE LOG IN LAYOUT
                    Intent intent = new Intent(signUp.this, logIn.class);
                    startActivity(intent);
                }
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