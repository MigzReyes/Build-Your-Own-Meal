package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.Arrays;
import java.util.List;

public class adminEditUser extends AppCompatActivity {
    //DATABASE
    databaseFunctions databaseFunctions;

    //VARIABLES
    private Spinner userRole;
    private EditText accountUsername, accountEmail, accountContactNumber, accountPassword, accountConPassword;
    private TextView sideBarActName, atLeastEightLetter, atLeastOneUpperCaseLetter, atLeastOneLowerCaseLetter, atLeastOneNumber;
    private RadioButton banTrue, banFalse;
    private Button editUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_user);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //USER DATA
        int userId = getIntent().getIntExtra("userId", 0);
        String username = getIntent().getStringExtra("username");
        String userEmail = getIntent().getStringExtra("userEmail");
        String userContactNum = getIntent().getStringExtra("userContactNum");
        String userPassword = getIntent().getStringExtra("userPassword");
        String ban = getIntent().getStringExtra("ban").toString().trim();
        String role = getIntent().getStringExtra("role");

        //REFERENCE
        userRole = findViewById(R.id.userRole);
        accountUsername = findViewById(R.id.accountUsername);
        accountEmail = findViewById(R.id.accountEmail);
        accountContactNumber = findViewById(R.id.accountContactNumber);
        accountPassword = findViewById(R.id.accountPassword);
        accountConPassword = findViewById(R.id.accountConPassword);
        atLeastEightLetter = findViewById(R.id.atLeastEightLetter);
        atLeastOneUpperCaseLetter = findViewById(R.id.atLeastOneUpperCaseLetter);
        atLeastOneLowerCaseLetter = findViewById(R.id.atLeastOneLowerCaseLetter);
        atLeastOneNumber = findViewById(R.id.atLeastOneNumber);
        banTrue = findViewById(R.id.banTrue);
        banFalse = findViewById(R.id.banFalse);
        editUserBtn = findViewById(R.id.editUserBtn);

        //SET USER DATA
        accountUsername.setText(username);
        accountEmail.setText(userEmail);
        accountContactNumber.setText(userContactNum);
        accountPassword.setText(userPassword);
        accountConPassword.setText(userPassword);
        userRole.setTag(role);
        if (ban.equals("true")) {
            banTrue.setChecked(true);
        } else if (ban.equals("false")) {
            banFalse.setChecked(true);
        }

        //TOOLBAR ACTIVITY NAME

        //DROPDOWN USER ROLE
        List<String> dropdownRole = Arrays.asList("User", "Admin");
        dropdownAdapter dropdownAdapter = new dropdownAdapter(this, R.layout.custom_spinner_bg, dropdownRole);
        dropdownAdapter.setDropDownViewResource(R.layout.custom_dropdown_bg);
        userRole.setAdapter(dropdownAdapter);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //PASSWORD LISTENER
        accountPassword.addTextChangedListener(new TextWatcher() {
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

        //BAN
        banFalse.setChecked(true);


        editUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUsername = accountUsername.getText().toString().trim();
                String getEmail = accountEmail.getText().toString().trim();
                String getContactNum = accountContactNumber.getText().toString().trim();
                String getPass = accountPassword.getText().toString().trim();
                String getConPass = accountConPassword.getText().toString().trim();
                String getRole = userRole.getSelectedItem().toString().trim();

                boolean checkEmail = databaseFunctions.checkEmailId(getEmail, userId);
                boolean checkContactNum = databaseFunctions.checkContactNumberId(getContactNum, userId);
                boolean checkUserPass = databaseFunctions.isPasswordValid(getPass);

                if (getUsername.isBlank() || getEmail.isBlank() || getPass.isBlank() || getConPass.isBlank()) {
                    popUpAlert(getString(R.string.pleaseFillUpTheInputField));
                } else if (checkEmail) {
                    popUpAlert(getString(R.string.emailAlreadyExist));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
                    popUpAlert(getString(R.string.invalidEmail));
                } else if (!getPass.equals(getConPass)) {
                    popUpAlert(getString(R.string.conPassAndPassDoesNotMatchError));
                } else if (!checkUserPass) {
                    popUpAlert(getString(R.string.passwordIsWeak));
                } else {
                    if (banTrue.isChecked()) {
                        if (!getContactNum.isBlank()) {
                            if (getContactNum.length() < 11) {
                                popUpAlert(getString(R.string.invalidPhoneNumber));
                            } else if (getContactNum.length() > 13) {
                                popUpAlert(getString(R.string.invalidPhoneNumber));
                            } else if (checkContactNum) {
                                popUpAlert(getString(R.string.contactNumberIsAlreadyInUsed));
                            } else {
                                boolean updateUserInfo = databaseFunctions.updateUserInfo(userId, getUsername, getEmail, getContactNum, getPass, "true", getRole);

                                if (updateUserInfo) {
                                    Intent intent = new Intent(adminEditUser.this, adminAccounts.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            boolean updateUserInfo = databaseFunctions.updateUserInfo(userId, getUsername, getEmail, null, getPass, "true", getRole);

                            if (updateUserInfo) {
                                Intent intent = new Intent(adminEditUser.this, adminAccounts.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                    } else if (banFalse.isChecked()) {
                        if (!getContactNum.isBlank()) {
                            if (getContactNum.length() < 11) {
                                popUpAlert(getString(R.string.invalidPhoneNumber));
                            } else if (getContactNum.length() > 13) {
                                popUpAlert(getString(R.string.invalidPhoneNumber));
                            } else if ( checkContactNum) {
                                popUpAlert(getString(R.string.contactNumberIsAlreadyInUsed));
                            } else {
                                boolean updateUserInfo = databaseFunctions.updateUserInfo(userId, getUsername, getEmail, getContactNum, getPass, "false", getRole);

                                if (updateUserInfo) {
                                    Intent intent = new Intent(adminEditUser.this, adminAccounts.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            boolean updateUserInfo = databaseFunctions.updateUserInfo(userId, getUsername, getEmail, null, getPass, "true", getRole);

                            if (updateUserInfo) {
                                Intent intent = new Intent(adminEditUser.this, adminAccounts.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                    }

                }

            }
        });
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