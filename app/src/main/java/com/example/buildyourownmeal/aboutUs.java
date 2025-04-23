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

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class aboutUs extends AppCompatActivity {

    //LOCAL VARIABLE
    TextView fragName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);

        //VARIABLE REFERENCE
        fragName = findViewById(R.id.fragName);
        fragName.setText(R.string.aboutUs);

        //BOTTOM NAVBAR
        //HOOK
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
                    Intent intent = new Intent(aboutUs.this, Navbar.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.menu) {
                    Intent intent = new Intent(aboutUs.this, menu.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.cart) {
                    Intent intent = new Intent(aboutUs.this, cart.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });*/


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
                Intent intent = new Intent(aboutUs.this, Navbar.class);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(aboutUs.this, menu.class);
                startActivity(intent);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(aboutUs.this, cart.class);
                startActivity(intent);
            }
        });

        aboutUs.setImageResource(R.drawable.aboutusiconorange);

    }
}