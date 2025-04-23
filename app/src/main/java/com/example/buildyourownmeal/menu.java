package com.example.buildyourownmeal;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class menu extends AppCompatActivity {

    //FOR RECYCLER VIEW
    private ArrayList<recyclerMenuCombosModel> recyclerMenuCombosModelArrayList = new ArrayList<>();
    private int[] comboMealImg = {R.drawable.chickenkaraagemeal, R.drawable.tunasisigmeal, R.drawable.veggieballsmeal,
            R.drawable.chickenkaraagemeal, R.drawable.tunasisigmeal, R.drawable.veggieballsmeal};

    private RecyclerView recyclerViewMenuCombos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        //RECYCLER VIEW
        recyclerViewMenuCombos = findViewById(R.id.recyclerViewMenuCombos);


        setUpMenuCombosModel();

        recyclerViewAdapterMenuCombos recyclerViewAdapterMenuCombos = new recyclerViewAdapterMenuCombos(this, recyclerMenuCombosModelArrayList);
        recyclerViewMenuCombos.setAdapter(recyclerViewAdapterMenuCombos);
        recyclerViewMenuCombos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMenuCombos.setNestedScrollingEnabled(false);


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

        /*BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setAnimation(null);
        bottomNav.setItemRippleColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navCon = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNav, navCon);
        } else {
            Log.e("Navbar", "NavHostFragment is empty, Check your layout xml");
        }

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home_dashboard) {
                    Intent intent = new Intent(menu.this, Navbar.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.cart) {
                    Intent intent = new Intent(menu.this, cart.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.aboutUs) {
                    Intent intent = new Intent(menu.this, aboutUs.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });*/

        int[] menuBtn = {R.id.craftNowBtn};

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, craftedMeal.class);
                startActivity(intent);
            }
        };

        for (int id : menuBtn) {
            findViewById(id).setOnClickListener(clickListener);
        }

    }

    private void setUpMenuCombosModel() {
        String[] comboMealNames = getResources().getStringArray(R.array.comboMealName);
        String[] comboMealDescription = getResources().getStringArray(R.array.comboMealDescription);

        for (int i = 0; i < comboMealNames.length; i++) {
            recyclerMenuCombosModelArrayList.add(new recyclerMenuCombosModel(comboMealNames[i], comboMealDescription[i], comboMealImg[i]));
        }
    }
}