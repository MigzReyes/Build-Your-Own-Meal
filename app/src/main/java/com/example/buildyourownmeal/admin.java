package com.example.buildyourownmeal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //DATABASE
    private databaseFunctions databaseFunctions;
    private DrawerLayout drawerLayout;
    private TextView ordersDashboardCount, mealsDashboardCount, menuDashboardCount, accountDashboardCount;

    private int numAccount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //REFERENCE
        ordersDashboardCount = findViewById(R.id.ordersDashboardCount);
        mealsDashboardCount = findViewById(R.id.mealsDashboardCount);
        menuDashboardCount = findViewById(R.id.menuDashboardCount);
        accountDashboardCount = findViewById(R.id.accountDashboardCount);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //SIDEBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.admin);
        NavigationView navigationView = findViewById(R.id.sidebar);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_sidebar, R.string.close_sidebar);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        toolbar.setTitleTextColor(getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //ACCOUNT COUNT
        numberOfAccount();
        accountDashboardCount.setText(String.valueOf(numAccount));

    }

    private void numberOfAccount() {
        Cursor getNumUser = databaseFunctions.getAllUser();

        if (getNumUser != null && getNumUser.moveToFirst()) {
            do {
                numAccount++;
            } while (getNumUser.moveToNext());
            getNumUser.close();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.adminOrders) {
            Intent intent = new Intent(this, adminOrders.class);
            startActivity(intent);
        } else if (id == R.id.adminMeals) {
            Intent intent = new Intent(this, adminMeals.class);
            startActivity(intent);
        } else if (id == R.id.adminMenu) {
            Intent intent = new Intent(this, adminMenu.class);
            startActivity(intent);
        } else if (id == R.id.adminAccount) {
            Intent intent = new Intent(this, adminAccounts.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}