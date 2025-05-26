package com.example.buildyourownmeal;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class home_dashboard extends Fragment {

    //DATABASE
    private databaseFunctions databaseFunctions;


    //RECYCLER VIEW
    private RecyclerView recyclerViewHomeCombos;
    private ArrayList<String> comboMealName, comboMealDescription, addonGroupId;
    private ArrayList<Bitmap> comboMealImg;
    private ArrayList<Integer> comboMealId;



    //VARIABLE DECLARATION
    private Button logInBtn, signUpBtn, cancelOrderBtn;

    //LOGIN WARNING
    private Dialog popUpLogInWarning;
    private LinearLayout logInWarning, craftNowBtn, logInTextAlert, userIntroduction, preparingYourOrderCon, mealInProgressCon, orderIsReadyCon;

    //USER INTRODUCTION/WELCOME
    private TextView userIntroductionName, viewAllBtn;

    //CHECK IF USER HAD ALREADY CHECKOUT/ORDERED
    private boolean checkIfUserOrdered = false;
    private int userId = 0;
    private String orderGroupId = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_dashboard, container, false);

        //DATABASE
        databaseFunctions = new databaseFunctions(getActivity());

        //SET CONNECTION TO SHARED PREFERENCE/USER SESSION
        SharedPreferences userSession = getActivity().getSharedPreferences("userSession", MODE_PRIVATE);
        boolean isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);
        String userRole = userSession.getString("role", "guest");
        String loggedInUsername = userSession.getString("username", null);
        int getUserId = userSession.getInt("userId", 0);

        boolean checkIfUserHadOrdered = databaseFunctions.checkIfUserHadOrdered(getUserId);

        //SET ID
        logInTextAlert = view.findViewById(R.id.logInTextAlert);
        userIntroduction = view.findViewById(R.id.userIntroduction);
        logInWarning = view.findViewById(R.id.login_warning);
        userIntroductionName = view.findViewById(R.id.userIntroductionName);
        viewAllBtn = view.findViewById(R.id.viewAllBtn);

        //VIEW ALL BUTTON
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), menu.class);
                startActivity(intent);
            }
        });

        //ORDER PROCESS CONTAINER
        preparingYourOrderCon = view.findViewById(R.id.preparingYourOrderCon);
        mealInProgressCon = view.findViewById(R.id.mealInProgressCon);
        orderIsReadyCon = view.findViewById(R.id.orderIsReadyCon);


        //GET ORDER SESSION
        SharedPreferences getOrderSession = getActivity().getSharedPreferences("orderSession", MODE_PRIVATE);
        checkIfUserOrdered = getOrderSession.getBoolean("checkIfUserOrdered", false);
        userId = getOrderSession.getInt("userId", 0);
        orderGroupId = getOrderSession.getString("orderGroupId", null);

        preparingYourOrderCon.setVisibility(View.GONE);
        mealInProgressCon.setVisibility(View.GONE);
        orderIsReadyCon.setVisibility(View.GONE);

        if (checkIfUserOrdered && checkIfUserHadOrdered) {
            Cursor getAdminOrderStatus = databaseFunctions.getAdminOrderStatus(userId, orderGroupId);

            String getStatus = null;
            if (getAdminOrderStatus != null && getAdminOrderStatus.moveToFirst()) {
                getStatus = getAdminOrderStatus.getString(getAdminOrderStatus.getColumnIndexOrThrow("status"));
            }

            if (getStatus != null) {
                switch (getStatus) {
                    case "Processing":
                        preparingYourOrderCon.setVisibility(View.VISIBLE);
                        mealInProgressCon.setVisibility(View.GONE);
                        orderIsReadyCon.setVisibility(View.GONE);

                        cancelOrderBtn = view.findViewById(R.id.cancelOrder);
                        cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog popUpAlert;
                                Button yesBtn, noBtn;

                                popUpAlert = new Dialog(getActivity());
                                popUpAlert.setContentView(R.layout.pop_up_delete_addon);
                                popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                                popUpAlert.setCancelable(true);
                                popUpAlert.show();

                                noBtn = popUpAlert.findViewById(R.id.cancelBtn);
                                noBtn.setText("No");
                                noBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popUpAlert.dismiss();
                                    }
                                });

                                yesBtn = popUpAlert.findViewById(R.id.deleteAddonBtn);
                                yesBtn.setText("Yes");
                                yesBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        boolean deleteAdminOrder = databaseFunctions.deleteAdminOrder(orderGroupId);

                                        if (deleteAdminOrder) {
                                            checkIfUserOrdered = false;
                                            Intent intent = new Intent(getActivity(), Navbar.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        } else {
                                            Log.d("may error ka", "Delete order failed");
                                        }
                                    }
                                });

                            }
                        });

                        break;
                    case "Meal in progress":
                        preparingYourOrderCon.setVisibility(View.GONE);
                        mealInProgressCon.setVisibility(View.VISIBLE);
                        orderIsReadyCon.setVisibility(View.GONE);
                        break;
                    case "Order is ready":
                        preparingYourOrderCon.setVisibility(View.GONE);
                        mealInProgressCon.setVisibility(View.GONE);
                        orderIsReadyCon.setVisibility(View.VISIBLE);
                        break;
                    case "Completed":
                        preparingYourOrderCon.setVisibility(View.GONE);
                        mealInProgressCon.setVisibility(View.GONE);
                        orderIsReadyCon.setVisibility(View.GONE);
                        break;
                }
            } else {
                Log.d("may error ka", "Status is empty");
            }
        }
        checkIfUserOrdered = false;


        //RECYCLER VIEW
        recyclerViewHomeCombos = view.findViewById(R.id.recyclerViewHomeCombo);
        comboMealName = new ArrayList<>();
        comboMealDescription = new ArrayList<>();
        comboMealImg = new ArrayList<>();
        addonGroupId = new ArrayList<>();
        comboMealId = new ArrayList<>();

        setUpHomeCombosModel();

        recyclerViewHomeCombos.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewAdapterHomeCombos recyclerViewAdapterHomeCombos = new recyclerViewAdapterHomeCombos(requireContext(), comboMealName, comboMealDescription, addonGroupId, comboMealImg, comboMealId);
        recyclerViewHomeCombos.setAdapter(recyclerViewAdapterHomeCombos);



        //IF USER IS ALREADY LOGGED IN / ALERT IF USER HAD NOT LOGGED IN

        craftNowBtn = view.findViewById(R.id.craftNowBtn);
        craftNowBtn.setClickable(true);
        craftNowBtn.setFocusable(true);

        craftNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), craftedMeal.class);
                startActivity(intent);
            }
        });


        //LOGIC STATEMENT FOR DISPLAYING ERROR "you are not logged in"
        //LOGIC STATEMENT FOR DISPLAYING USER NAME ON INTRODUCTION
        if (isUserLoggedIn) {
            logInWarning.setVisibility(View.GONE);
            logInTextAlert.setVisibility(View.GONE);
            userIntroduction.setVisibility(View.VISIBLE);

            userIntroductionName.setText(loggedInUsername);
        } else if (userRole.equals("guest")) {
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

    private void setUpHomeCombosModel () {
        Cursor getAdminMeal = databaseFunctions.getAdminMeal();

        if (getAdminMeal != null && getAdminMeal.moveToFirst()) {
            do {
                byte[] byteArray = getAdminMeal.getBlob(getAdminMeal.getColumnIndexOrThrow("mealImg"));
                comboMealImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                comboMealId.add(getAdminMeal.getInt(getAdminMeal.getColumnIndexOrThrow("adminMealId")));
                addonGroupId.add(getAdminMeal.getString(getAdminMeal.getColumnIndexOrThrow("adminAddonId")));
                comboMealName.add(getAdminMeal.getString(getAdminMeal.getColumnIndexOrThrow("mealName")));
                comboMealDescription.add(getAdminMeal.getString(getAdminMeal.getColumnIndexOrThrow("mealDescription")));
            } while (getAdminMeal.moveToNext());
            getAdminMeal.close();
        }
    }
}