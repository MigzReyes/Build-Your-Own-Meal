package com.example.buildyourownmeal;

import static android.app.PendingIntent.getActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Patterns;
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
import androidx.core.view.WindowInsetsControllerCompat;

public class signUp extends AppCompatActivity {

    private databaseFunctions databaseFunctions;

    //VARIABLES
    private Button signUpBtn;
    private TextView logInLink, atLeastEightLetter, atLeastOneUpperCaseLetter, atLeastOneLowerCaseLetter, atLeastOneNumber;

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

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //DATABASE DECLARATION;
        databaseFunctions = new databaseFunctions(this);

        //SET ID
        signUpBtn = findViewById(R.id.signUpBtn);
        logInLink = findViewById(R.id.alreadyHaveAcc);

        //SIGN UP SET ID
        username = findViewById(R.id.username);
        signUpEmail = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);
        conPassword = findViewById(R.id.conPass);
        termsAndCon = findViewById(R.id.AgreeTermsAndCon);
        atLeastEightLetter = findViewById(R.id.atLeastEightLetter);
        atLeastOneUpperCaseLetter = findViewById(R.id.atLeastOneUpperCaseLetter);
        atLeastOneLowerCaseLetter = findViewById(R.id.atLeastOneLowerCaseLetter);
        atLeastOneNumber = findViewById(R.id.atLeastOneNumber);


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPasswordStrength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //VARIABLE FOR GETTING THE TEXT FROM THE EDIT TEXT
                String name = username.getText().toString().trim();
                String email = signUpEmail.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String conPass = conPassword.getText().toString().trim();
                boolean checkBox = termsAndCon.isChecked();

                //LOGIC STATEMENT FOR CHECKING IF THE USER HAS INPUT THE TEXT FIELD
                if (name.isBlank() || email.isBlank() || pass.isBlank() || conPass.isBlank()){
                    popUpAlert(getString(R.string.fillUpAllInputFieldsError));
                } else if (databaseFunctions.checkEmail(email)) {
                    popUpAlert(getString(R.string.emailAlreadyExist));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    popUpAlert(getString(R.string.invalidEmail));
                } else if (!pass.equals(conPass)) {
                    popUpAlert(getString(R.string.conPassAndPassDoesNotMatchError));
                } else if (!checkBox) {
                    popUpAlert(getString(R.string.agreeToTheTermsAndConError));
                } else {
                    boolean checkUserPass = databaseFunctions.isPasswordValid(pass);

                    if (checkUserPass) {
                        Boolean insertData = databaseFunctions.insertUserData(name, email, pass, "user");

                        if (insertData) {
                            Intent intent = new Intent(signUp.this, logIn.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    } else {
                        popUpAlert(getString(R.string.passwordIsWeak));
                    }
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

    private void checkPasswordStrength(String password) {

        if (password.length() >= 8) {
            atLeastEightLetter.setTextColor(getColor(R.color.greyLetters));
        } else {
            atLeastEightLetter.setTextColor(getColor(R.color.whiteBoldLetters));
        }

        if (password.matches(".*[A-Z].*")) {
            atLeastOneUpperCaseLetter.setTextColor(getColor(R.color.greyLetters));
        } else {
            atLeastOneUpperCaseLetter.setTextColor(getColor(R.color.whiteBoldLetters));
        }

        if (password.matches(".*[a-z].*")) {
            atLeastOneLowerCaseLetter.setTextColor(getColor(R.color.greyLetters));
        } else {
            atLeastOneLowerCaseLetter.setTextColor(getColor(R.color.whiteBoldLetters));
        }

        if (password.matches(".*\\d.*")) {
            atLeastOneNumber.setTextColor(getColor(R.color.greyLetters));
        } else {
            atLeastOneNumber.setTextColor(getColor(R.color.whiteBoldLetters));
        }
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