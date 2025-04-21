package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class editPassword extends AppCompatActivity {

    //VARIABLE DECLARATION
    databaseFunctions databaseFunctions;
    private Dialog popUpAlert;
    ImageView backBtn;
    TextView sideBarActName, atLeastEightLetter, atLeastOneUpperCaseLetter, atLeastOneLowerCaseLetter, atLeastOneNumber;
    EditText currentPassword, newPassword, confirmPassword;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_password);

        //DATABASE INSTANTIATION
        databaseFunctions = new databaseFunctions(this);

        //SHARED PREFERENCE
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSession.edit();

        //SHARED PREFERENCE GETTERS
        int userId = userSession.getInt("userId", 0);

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

        //POP UP ALERT
        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_save_changes);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);

        newPassword.addTextChangedListener(new TextWatcher() {
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

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getCurrentPassword = currentPassword.getText().toString().trim();
                String getNewPassword = newPassword.getText().toString().trim();
                String getConPassword = confirmPassword.getText().toString().trim();
                boolean getPass = databaseFunctions.checkPassword(getCurrentPassword);

                if (getCurrentPassword.isBlank() || getNewPassword.isBlank() || getConPassword.isBlank()) {
                    popUpAlert(getString(R.string.fillUpAllInputFieldsError));
                } else {
                    if (getPass) {
                        if (getNewPassword.equals(getCurrentPassword)) {
                            popUpAlert(getString(R.string.currentPasswordCannotBeTheSameAsNewPassword));
                        } else {
                            if (getNewPassword.equals(getConPassword)) {
                                boolean checkUserPass = databaseFunctions.isPasswordValid(getNewPassword);

                                if (checkUserPass) {
                                    Button saveChangesBtn, cancelChangesBtn;

                                    popUpAlert.show();

                                    saveChangesBtn = popUpAlert.findViewById(R.id.saveChangesBtn);
                                    saveChangesBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            databaseFunctions.updatePassword(userId, getNewPassword);
                                            editor.putString("password", getNewPassword);
                                            editor.apply();
                                            Intent intent = new Intent(editPassword.this, account.class);
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
                                } else {
                                    popUpAlert(getString(R.string.passwordIsWeak));
                                }

                            } else {
                                popUpAlert(getString(R.string.conPassAndPassDoesNotMatchError));
                            }
                        }
                    } else {
                        popUpAlert(getString(R.string.currentPasswordIsWrong));
                    }
                }
            }
        });

        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editPassword.this, account.class);
                startActivity(intent);
                finish();
            }
        });

        //ACTIVITY NAME
        sideBarActName.setText(getString(R.string.password));

    }

    private void checkPasswordStrength(String password) {

        if (password.length() >= 8) {
            atLeastEightLetter.setTextColor(getColor(R.color.blackBoldLetters));
        } else {
            atLeastEightLetter.setTextColor(getColor(R.color.greyLetters));
        }

        if (password.matches(".*[A-Z].*")) {
            atLeastOneUpperCaseLetter.setTextColor(getColor(R.color.blackBoldLetters));
        } else {
            atLeastOneUpperCaseLetter.setTextColor(getColor(R.color.greyLetters));
        }

        if (password.matches(".*[a-z].*")) {
            atLeastOneLowerCaseLetter.setTextColor(getColor(R.color.blackBoldLetters));
        } else {
            atLeastOneLowerCaseLetter.setTextColor(getColor(R.color.greyLetters));
        }

        if (password.matches(".*\\d.*")) {
            atLeastOneNumber.setTextColor(getColor(R.color.blackBoldLetters));
        } else {
            atLeastOneNumber.setTextColor(getColor(R.color.greyLetters));
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