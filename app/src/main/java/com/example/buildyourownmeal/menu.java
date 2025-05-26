package com.example.buildyourownmeal;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class menu extends AppCompatActivity {

    //DATABASE
    private databaseFunctions databaseFunctions;

    //RECYCLER VIEW
    private RecyclerView recyclerViewMenuCombos;
    private ArrayList<String> comboMealName, comboMealDescription, addonGroupId;
    private ArrayList<Bitmap> comboMealImg;
    private ArrayList<Integer> comboMealPrice, comboMealId;



    private LinearLayout craftNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //REFERENCE
        craftNowBtn = findViewById(R.id.craftNowBtn);

        craftNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, craftedMeal.class);
                startActivity(intent);
            }
        });

        //RECYCLER VIEW
        recyclerViewMenuCombos = findViewById(R.id.recyclerViewMenuCombos);
        comboMealId = new ArrayList<>();
        addonGroupId = new ArrayList<>();
        comboMealImg = new ArrayList<>();
        comboMealName = new ArrayList<>();
        comboMealDescription = new ArrayList<>();
        comboMealPrice = new ArrayList<>();

        setUpMenuCombosModel();

        recyclerViewMenuCombos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterMenuCombos recyclerViewAdapterMenuCombos = new recyclerViewAdapterMenuCombos(this, comboMealName, comboMealDescription, addonGroupId, comboMealImg, comboMealPrice, comboMealId);
        recyclerViewMenuCombos.setAdapter(recyclerViewAdapterMenuCombos);









        //BOTTOM NAVBAR
        //HOOK
        ImageView home, menu, cart, aboutUs;

        home = findViewById(R.id.navHome);
        menu = findViewById(R.id.navMenu);
        cart = findViewById(R.id.navCart);
        aboutUs = findViewById(R.id.navAboutUs);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, Navbar.class);
                startActivity(intent);
            }
        });

        menu.setImageResource(R.drawable.menuorange);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, cart.class);
                startActivity(intent);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, aboutUs.class);
                startActivity(intent);
            }
        });
    }

    private void setUpMenuCombosModel() {
        Cursor getAdminMeal = databaseFunctions.getAdminMeal();

        if (getAdminMeal != null && getAdminMeal.moveToFirst()) {
            do {
                byte[] byteArray = getAdminMeal.getBlob(getAdminMeal.getColumnIndexOrThrow("mealImg"));
                comboMealImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                comboMealId.add(getAdminMeal.getInt(getAdminMeal.getColumnIndexOrThrow("adminMealId")));
                addonGroupId.add(getAdminMeal.getString(getAdminMeal.getColumnIndexOrThrow("adminAddonId")));
                comboMealName.add(getAdminMeal.getString(getAdminMeal.getColumnIndexOrThrow("mealName")));
                comboMealDescription.add(getAdminMeal.getString(getAdminMeal.getColumnIndexOrThrow("mealDescription")));
                comboMealPrice.add(getAdminMeal.getInt(getAdminMeal.getColumnIndexOrThrow("mealTotalPrice")));
            } while (getAdminMeal.moveToNext());
            getAdminMeal.close();
        }
    }
}