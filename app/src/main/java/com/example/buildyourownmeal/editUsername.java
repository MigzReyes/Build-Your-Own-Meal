package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
    private databaseFunctions databaseFunctions;
    private ImageView backBtn;
    private TextView sideActname;
    private EditText editUsername;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_username);

        //DATABASE INSTANTIATION
        databaseFunctions = new databaseFunctions(this);

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        sideActname = findViewById(R.id.sideFragName);
        editUsername = findViewById(R.id.editUsername);
        saveBtn = findViewById(R.id.saveBtn);

        Dialog popUpAlert;

        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_save_changes);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUsername = editUsername.getText().toString().trim();
                Button saveChangesBtn, cancelChangesBtn;

                popUpAlert.show();

                saveChangesBtn = popUpAlert.findViewById(R.id.saveChangesBtn);
                saveChangesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseFunctions.insertUsername(getUsername);
                        Intent intent = new Intent(editUsername.this, account.class);
                        startActivity(intent);
                        finish();
                    }
                });

                cancelChangesBtn = popUpAlert.findViewById(R.id.cancelChangesBtn);
                cancelChangesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpAlert.dismiss();
                    }
                });


            }
        });

        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ACTIVITY NAME
        sideActname.setText(getString(R.string.username));

    }
}