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
    private DrawerLayout drawerLayout;
    private Button logInBtn, signUpBtn, logInPopUpAlert, signUpPopUpAlert;

    //LOGIN WARNING
    private Dialog popUpLogInWarning;
    private LinearLayout logInWarning, logInWarningBtn, craftNowBtn;

    //USER INTRODUCTION/WELCOME
    private TextView userIntro, logInWarningText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_dashboard, container, false);

        //ORDER BUTTON LINK
        LinearLayout orderCon = view.findViewById(R.id.orderCon);

        /*int[] btnId = {R.id.orderCon, R.id.orderBtn, R.id.orderBtn1, R.id.orderBtn2,
                        R.id.orderBtn3, R.id.orderBtn4, R.id.orderBtn5, R.id.orderBtn6,
                        R.id.orderBtn7, R.id.orderBtn8};*/

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
                popUpLogInWarning.show();
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




        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navcon = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navcon.popBackStack(R.id.home_dashboard, true);
                navcon.navigate(R.id.menu);
            }
        };

        /*for (int id : btnId) {
            view.findViewById(id).setOnClickListener(clickListener);
        }*/


        //IF USER IS ALREADY LOGGED IN
        //SET ID
        logInWarning = view.findViewById(R.id.login_warning);
        userIntro = view.findViewById(R.id.userIntroduction);


        //SET CONNECTION TO SHARED PREFERENCE/USER SESSION
        SharedPreferences userSession = getActivity().getSharedPreferences("userSession", MODE_PRIVATE);
        boolean isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);
        String loggedInUsername = userSession.getString("username", null);

        //LOGIC STATEMENT FOR DISPLAYING ERROR "you are not logged in"
        //LOGIC STATEMENT FOR DISPLAYING USER NAME ON INTRODUCTION
        if (isUserLoggedIn) {
            logInWarning.setVisibility(View.GONE);

            String welcome = getString(R.string.hello) + " " + loggedInUsername + "!";
            userIntro.setText(welcome);
        } else {
            logInWarning.setVisibility(View.VISIBLE);
        }

        //SHARED PREFERENCE FOR IF USER HAD ORDERED
        SharedPreferences orderProcess = getActivity().getSharedPreferences("orderProcess", MODE_PRIVATE);
        boolean ifUserHadOrdered = orderProcess.getBoolean("ifUserHadOrdered", false);


        //LOG IN BUTTON LINK
        //SET ID
        logInBtn = view.findViewById(R.id.logInBtn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), logIn.class);
                startActivity(intent);
            }
        });

        //SIGN UP BUTTON LINK
        //SET ID
        signUpBtn = view.findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), signUp.class);
                startActivity(intent);
            }
        });



        //SIDEBAR
        //HOOK
        /*Toolbar toolbar = view.findViewById(R.id.toolbar);
        drawerLayout = view.findViewById(R.id.home);
        NavigationView navigationView = view.findViewById(R.id.sidebar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar,R.string.open_sidebar, R.string.close_sidebar);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        setToolbarIconSize(toolbar, R.drawable.baseline_account_circle_24, 110);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();*/

        return view;
    }

    //FOR DISPLAYING FRAGMENTS FOR MAIN ACTIVITY/navbar.xml
    /*private void hideMainLayout() {
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.toolbar).setVisibility(NavigationView.GONE);
            view.findViewById(R.id.sidebar).setVisibility(NavigationView.GONE);
            view.findViewById(R.id.bottom_navigation).setVisibility(NavigationView.GONE);
        }
    }

    private void showMainLayout() {
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.toolbar).setVisibility(NavigationView.VISIBLE);
            view.findViewById(R.id.sidebar).setVisibility(NavigationView.VISIBLE);
            view.findViewById(R.id.bottom_navigation).setVisibility(NavigationView.VISIBLE);
        }
    }

    //LOGIC FOR SIDEBAR ITEMS
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.account) {
            Intent intent = new Intent(getActivity(), account.class);
            startActivity(intent);
        } else if (id == R.id.orderHis) {
            Intent intent = new Intent(getActivity(), orderHistory.class);
            startActivity(intent);
        } else if (id == R.id.contactUs) {
            Intent intent = new Intent(getActivity(), contactUs.class);
            startActivity(intent);
        } else if (id == R.id.termsAndCondition) {
            Intent intent = new Intent(getActivity(), termsAndCondition.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            showMainLayout();
        } else {
            super.getOnBackPressedDispatcher().onBackPressed();
        }

    }

    //ACTION BAR ICON SIZE METHOD
    private void setToolbarIconSize(Toolbar toolbar, int iconRes, int size) {
        Drawable drawable = ContextCompat.getDrawable(getContext(),  iconRes);
        if (drawable != null) {
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            toolbar.setNavigationIcon(new BitmapDrawable(getResources(), bitmap));
        }
    }*/
}