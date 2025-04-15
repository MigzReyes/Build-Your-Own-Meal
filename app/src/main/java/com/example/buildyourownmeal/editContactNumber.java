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

public class editContactNumber extends AppCompatActivity {

    //VARIABLE DECLARATION
    ImageView backBtn;
    TextView sideActname;
    EditText editContactNumber;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_contact_number);

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        sideActname = findViewById(R.id.sideFragName);
        editContactNumber = findViewById(R.id.editContactNumber);
        saveBtn = findViewById(R.id.saveBtn);


        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ACTIVITY NAME
        sideActname.setText(getString(R.string.contactNumber));
    }
}