package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class adminMeals extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //DATABASE
    private databaseFunctions databaseFunctions;

    private DrawerLayout drawerLayout;
    private Button addNewMealBtn;

    //RECYCLER
    private RecyclerView adminMealsRecycler;
    private ArrayList<String> mealName, mealDescription, adminAddonId;
    private ArrayList<Bitmap> mealImg;
    private ArrayList<Integer> mealPrice, adminMealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_meals);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //REFERENCE
        addNewMealBtn = findViewById(R.id.addNewMealBtn);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //SIDEBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.adminMeals);
        NavigationView navigationView = findViewById(R.id.sidebar);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_sidebar, R.string.close_sidebar);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        toolbar.setTitleTextColor(getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //ADD A NEW MEAL BUTTON
        addNewMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminMeals.this, adminAddMeals.class);
                startActivity(intent);
            }
        });


        //RECYCLER
        adminMealsRecycler = findViewById(R.id.adminMealsRecycler);
        adminMealId = new ArrayList<>();
        adminAddonId = new ArrayList<>();
        mealName = new ArrayList<>();
        mealDescription = new ArrayList<>();
        mealImg = new ArrayList<>();
        mealPrice = new ArrayList<>();

        setUpAdminMeal();

        recyclerViewAdapterAdminMeals adminMealAdapter = new recyclerViewAdapterAdminMeals(this, adminAddonId, mealName, mealDescription, mealImg, mealPrice, adminMealId);
        adminMealsRecycler.setLayoutManager(new LinearLayoutManager(this));
        adminMealsRecycler.setAdapter(adminMealAdapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.adminDashboard) {
            Intent intent = new Intent(this, admin.class);
            startActivity(intent);
        } else if (id == R.id.adminOrders) {
            Intent intent = new Intent(this, adminOrders.class);
            startActivity(intent);
        } else if (id == R.id.adminMenu) {
            Intent intent = new Intent(this, adminMenu.class);
            startActivity(intent);
        } else if (id == R.id.adminAccount) {
            Intent intent = new Intent(this, adminAccounts.class);
            startActivity(intent);
        } else if (id == R.id.logOut) {
            Dialog popUpAlert;
            Button cancelBtn, logOutBtn;

            popUpAlert = new Dialog(this);
            popUpAlert.setContentView(R.layout.pop_up_logout);
            popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
            popUpAlert.setCancelable(true);
            popUpAlert.show();

            cancelBtn = popUpAlert.findViewById(R.id.cancelLogOutBtn);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUpAlert.dismiss();
                }
            });

            logOutBtn = popUpAlert.findViewById(R.id.logOutBtn);
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(adminMeals.this, introduction_screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpAdminMeal() {
        Cursor getAdminMeal = databaseFunctions.getAdminMeal();

        if (getAdminMeal != null && getAdminMeal.moveToFirst()) {
            do {
                adminMealId.add(getAdminMeal.getInt(getAdminMeal.getColumnIndexOrThrow("adminMealId")));
                adminAddonId.add(getAdminMeal.getString(getAdminMeal.getColumnIndexOrThrow("adminAddonId")));
                Log.d("may erro ka", String.valueOf(adminAddonId));
                mealName.add(getAdminMeal.getString(getAdminMeal.getColumnIndexOrThrow("mealName")));
                mealDescription.add(getAdminMeal.getString(getAdminMeal.getColumnIndexOrThrow("mealDescription")));
                byte[] byteArray = getAdminMeal.getBlob(getAdminMeal.getColumnIndexOrThrow("mealImg"));
                mealImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                mealPrice.add(getAdminMeal.getInt(getAdminMeal.getColumnIndexOrThrow("mealTotalPrice")));
            } while (getAdminMeal.moveToNext());
        }
    }
}