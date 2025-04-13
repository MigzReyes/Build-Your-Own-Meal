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

public class editUsername extends AppCompatActivity {

    //VARIABLE DECLARATION
    ImageView backBtn;
    TextView sideActname;
    EditText editUsername;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_username);

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        sideActname = findViewById(R.id.sideFragName);
        editUsername = findViewById(R.id.editUsername);
        saveBtn = findViewById(R.id.saveBtn);

        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editUsername.this, account.class);
                startActivity(intent);
            }
        });

        //ACTIVITY NAME
        sideActname.setText(getString(R.string.username));

    }
}