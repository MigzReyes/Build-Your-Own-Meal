package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
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

public class editEmail extends AppCompatActivity {

    //VARIABLE DECLARATION
    private databaseFunctions databaseFunctions;
    private Dialog popUpAlert;
    private ImageView backBtn;
    private TextView sideActName;
    private EditText editEmail;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_email);

        //DATABASE INSTANTIATION
        databaseFunctions = new databaseFunctions(this);

        //SHARED PREFERENCE
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        sideActName = findViewById(R.id.sideFragName);
        editEmail = findViewById(R.id.editEmail);
        saveBtn = findViewById(R.id.saveBtn);

        //SHARED PREFERENCE GETTERS
        int userId = userSession.getInt("userId", 0);
        String showEmail = userSession.getString("email", null);

        //SET TEXT CURRENT EMAIL
        editEmail.setText(showEmail);

        //POP UP ALERT
        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_save_changes);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = editEmail.getText().toString().trim();
                Button saveChangesBtn, cancelChangesBtn;

                boolean checkEmail = databaseFunctions.checkEmail(getEmail);

                if (getEmail.isBlank()) {
                    popUpAlert(getString(R.string.pleaseFillUpTheInputField));
                } else {
                    if (checkEmail) {
                        popUpAlert(getString(R.string.emailAlreadyExist));
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
                        popUpAlert(getString(R.string.invalidEmail));
                    } else {
                        popUpAlert.show();

                        saveChangesBtn = popUpAlert.findViewById(R.id.saveChangesBtn);
                        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseFunctions.updateEmail(userId, getEmail);
                                editor.putString("email", getEmail);
                                editor.apply();
                                Intent intent = new Intent(editEmail.this, account.class);
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
                }
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
        sideActName.setText(getString(R.string.email));

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