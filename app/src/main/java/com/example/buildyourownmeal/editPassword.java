package com.example.buildyourownmeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class editPassword extends AppCompatActivity {

    //VARIABLE DECLARATION
    ImageView backBtn;
    TextView sideBarActName, atLeastEightLetter, atLeastOneUpperCaseLetter, atLeastOneLowerCaseLetter, atLeastOneNumber;
    EditText currentPassword, newPassword, confirmPassword;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_password);

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        sideBarActName = findViewById(R.id.sideFragName);
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        saveBtn = findViewById(R.id.saveBtn);
        atLeastEightLetter = findViewById(R.id.atLeastEightLetter);
        atLeastOneUpperCaseLetter = findViewById(R.id.atLeastOneUpperCaseLetter);
        atLeastOneLowerCaseLetter = findViewById(R.id.atLeastOneLowerCaseLetter);
        atLeastOneNumber = findViewById(R.id.atLeastOneNumber);

        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editPassword.this, account.class);
                startActivity(intent);
            }
        });

        //ACTIVITY NAME
        sideBarActName.setText(getString(R.string.password));

    }
}