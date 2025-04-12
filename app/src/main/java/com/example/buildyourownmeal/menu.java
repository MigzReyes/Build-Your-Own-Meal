package com.example.buildyourownmeal;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        //BOTTOM NAVBAR
        //HOOK
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navCon = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNav, navCon);
        } else {
            Log.e("Navbar", "NavHostFragment is empty, Check your layout xml");
        }

        int[] menuBtn = {R.id.craftNowBtn, R.id.menuCon,};

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
}