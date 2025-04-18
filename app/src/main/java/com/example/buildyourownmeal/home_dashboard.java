package com.example.buildyourownmeal;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class home_dashboard extends Fragment /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    //VARIABLE DECLARATION
    private Button logInBtn, signUpBtn, logInPopUpAlert, signUpPopUpAlert;

    //LOGIN WARNING
    private Dialog popUpLogInWarning;
    private LinearLayout logInWarning, craftNowBtn, logInTextAlert, userIntroduction;

    //USER INTRODUCTION/WELCOME
    private TextView userIntroductionName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_dashboard, container, false);




        //IF USER IS ALREADY LOGGED IN / ALERT IF USER HAD NOT LOGGED IN
        //SET ID
        logInTextAlert = view.findViewById(R.id.logInTextAlert);
        userIntroduction = view.findViewById(R.id.userIntroduction);
        logInWarning = view.findViewById(R.id.login_warning);
        userIntroductionName = view.findViewById(R.id.userIntroductionName);

        // POP LOG IN WARNING
        popUpLogInWarning = new Dialog(requireContext());
        popUpLogInWarning.setContentView(R.layout.pop_up_login_signup_alert);
        popUpLogInWarning.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpLogInWarning.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpLogInWarning.setCancelable(true);

        craftNowBtn = view.findViewById(R.id.craftNowBtn);
        craftNowBtn.setClickable(true);
        craftNowBtn.setFocusable(true);

        logInPopUpAlert = popUpLogInWarning.findViewById(R.id.popUpAlertLogInBtn);
        signUpPopUpAlert = popUpLogInWarning.findViewById(R.id.popUpAlertSignUpBtn);

        craftNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), craftedMeal.class);
                startActivity(intent);
            }
        });

        logInPopUpAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), logIn.class);
                startActivity(intent);
                popUpLogInWarning.dismiss();
            }
        });

        signUpPopUpAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), signUp.class);
                startActivity(intent);
                popUpLogInWarning.dismiss();
            }
        });

        //SET CONNECTION TO SHARED PREFERENCE/USER SESSION
        SharedPreferences userSession = getActivity().getSharedPreferences("userSession", MODE_PRIVATE);
        boolean isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);
        String loggedInUsername = userSession.getString("username", null);

        //LOGIC STATEMENT FOR DISPLAYING ERROR "you are not logged in"
        //LOGIC STATEMENT FOR DISPLAYING USER NAME ON INTRODUCTION
        if (isUserLoggedIn) {
            logInWarning.setVisibility(View.GONE);
            logInTextAlert.setVisibility(View.GONE);
            userIntroduction.setVisibility(View.VISIBLE);

            userIntroductionName.setText(loggedInUsername);
        } else {
            logInWarning.setVisibility(View.VISIBLE);
            logInTextAlert.setVisibility(View.VISIBLE);
            userIntroduction.setVisibility(View.GONE);
        }

        //LOG IN BUTTON LINK FOR ALERT
        //SET ID
        logInBtn = view.findViewById(R.id.logInBtn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), logIn.class);
                startActivity(intent);
            }
        });

        //SIGN UP BUTTON LINK FOR ALERT
        //SET ID
        signUpBtn = view.findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), signUp.class);
                startActivity(intent);
            }
        });

        return view;
    }
}