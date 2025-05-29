package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.core.view.WindowInsetsControllerCompat;

public class forgot_password extends AppCompatActivity {

    //DATABASE
    private databaseFunctions databaseFunctions;

    //VARIABLE DECLARATION
    private EditText email;
    private Button reqResetLink;
    private TextView backBtnLogIn;
    private Button fabBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //REFERENCE
        email = findViewById(R.id.email);
        reqResetLink = findViewById(R.id.reqResetLink);
        backBtnLogIn = findViewById(R.id.backBtnLogIn);
        fabBackBtn = findViewById(R.id.fabBackBtn);

        //EMAIL
        reqResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString().trim();

                boolean checkEmail = databaseFunctions.checkEmail(emailText);

                if (emailText.isBlank()) {
                    popUpAlert(getString(R.string.pleaseFillUpTheInputField));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    popUpAlert(getString(R.string.invalidEmail));
                } else if (!checkEmail) {
                    popUpAlert(getString(R.string.emailDoesNotExistError));
                } else {
                    popUpAlert(getString(R.string.weHaveSendYouAnEmail));
                }
            }
        });

        //BACK BUTTON
        fabBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    public void popUpAlert(String getAlertText) {
        Dialog popUpAlert;
        Button closeBtn;
        TextView alertText;

        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_alerts);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);
        popUpAlert.show();

        alertText = popUpAlert.findViewById(R.id.alertText);
        alertText.setText(getAlertText);

        closeBtn = popUpAlert.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAlert.dismiss();
            }
        });
    }
}