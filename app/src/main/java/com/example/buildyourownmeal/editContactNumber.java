package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class editContactNumber extends AppCompatActivity {

    //VARIABLE DECLARATION
    databaseFunctions databaseFunctions;
    Dialog popUpAlert;
    ImageView backBtn;
    TextView sideActname;
    EditText editContactNumber;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_contact_number);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //BACK PRESSED ON PHONE SYSTEM
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);

                Intent intent = new Intent(editContactNumber.this, account.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        //DATABASE INSTANTIATION
        databaseFunctions = new databaseFunctions(this);

        //SHARED PREFERENCE
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        sideActname = findViewById(R.id.sideFragName);
        editContactNumber = findViewById(R.id.editContactNumber);
        saveBtn = findViewById(R.id.saveBtn);

        //SHARED PREFERENCE GETTERS
        int userId = userSession.getInt("userId", 0);
        String email = userSession.getString("email", null);

        //DATABASE GETTERS
        Cursor getUserInfoDb = databaseFunctions.getUserInfo(email);

        //SET TEXT CURRENT CONTACT NUMBER
        if (getUserInfoDb != null && getUserInfoDb.moveToFirst()) {
            String getContactNumberDb = getUserInfoDb.getString(getUserInfoDb.getColumnIndexOrThrow("contactNumber"));
            editContactNumber.setText(getContactNumberDb);
        } else {
            popUpAlert(getString(R.string.sorryCanNotGetUserInfo));
        }

        //POP UP ALERT
        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_save_changes);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getContactNumber = editContactNumber.getText().toString().trim();
                Button saveChangesBtn, cancelChangesBtn;
                boolean checkContactNumber = databaseFunctions.checkContactNumber(getContactNumber);

                if (getContactNumber.isEmpty()) {
                    popUpAlert(getString(R.string.pleaseFillUpTheInputField));
                } else if (getContactNumber.length() < 11) {
                    popUpAlert(getString(R.string.invalidPhoneNumber));
                } else if (getContactNumber.length() > 13) {
                    popUpAlert(getString(R.string.invalidPhoneNumber));
                }  else if (checkContactNumber) {
                    popUpAlert(getString(R.string.contactNumberIsAlreadyInUsed));
                } else {
                    popUpAlert.show();

                    saveChangesBtn = popUpAlert.findViewById(R.id.saveChangesBtn);
                    saveChangesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseFunctions.updateContactNumber(userId, getContactNumber);
                            editor.putString("contactNumber", getContactNumber);
                            editor.apply();
                            Intent intent = new Intent(editContactNumber.this, account.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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
        });

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