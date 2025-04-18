package com.example.buildyourownmeal;

import static android.app.PendingIntent.getActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
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

public class signUp extends AppCompatActivity {

    private databaseFunctions databaseFunctions;
    private Dialog popUpAlert;

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
                } else if (!pass.equals(conPass)) {
                    popUpAlert(getString(R.string.conPassAndPassDoesNotMatchError));
                } else if (!checkBox) {
                    popUpAlert(getString(R.string.agreeToTheTermsAndConError));
                }else {
                    boolean checkUserEmail = databaseFunctions.checkEmail(email);

                    if (!checkUserEmail) {
                        popUpAlert(getString(R.string.emailAlreadyExist));
                    } else {
                        Boolean insertData = databaseFunctions.insertData(name, email, pass);

                        if (insertData) {
                            Intent intent = new Intent(signUp.this, logIn.class);
                            startActivity(intent);
                            finish();
                        }
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

    public void popUpAlert(String alertMessage) {
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