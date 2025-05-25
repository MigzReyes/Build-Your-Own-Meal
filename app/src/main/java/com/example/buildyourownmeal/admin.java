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
    private int numOrder = 0;
    private int numMenu = 0;

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

        //ORDER COUNT
        numberOfOrders();
        ordersDashboardCount.setText(String.valueOf(numOrder));

        //MENU COUNT
        numberOfMenu();
        menuDashboardCount.setText(String.valueOf(numMenu));

        //ACCOUNT COUNT
        numberOfAccount();
        accountDashboardCount.setText(String.valueOf(numAccount));

    }

    private void numberOfOrders() {
        Cursor getNumOrder = databaseFunctions.getAdminOrder();

        if (getNumOrder != null && getNumOrder.moveToFirst()) {
            do {
                numOrder++;
            } while (getNumOrder.moveToNext());
        }
    }

    private void numberOfMenu() {
        int numRice = 0;
        int numMainDish = 0;
        int numSide = 0;
        int numSauce = 0;
        int numDessert = 0;
        int numDrink = 0;

        //RICE COUNT
        Cursor getAddonRice = databaseFunctions.getAddonTable("rice");

        if (getAddonRice != null && getAddonRice.moveToFirst()) {
            do {
                numRice++;
            } while (getAddonRice.moveToNext());
        }

        //MAIN DISH COUNT
        Cursor getAddonMainDish = databaseFunctions.getAddonTable("main_dish");

        if (getAddonMainDish != null && getAddonMainDish.moveToFirst()) {
            do {
                numMainDish++;
            } while (getAddonMainDish.moveToNext());
        }

        //SIDE DISH COUNT
        Cursor getAddonSide = databaseFunctions.getAddonTable("side_dish");

        if (getAddonSide != null && getAddonSide.moveToFirst()) {
            do {
                numSide++;
            } while (getAddonSide.moveToNext());
        }

        //SAUCE COUNT
        Cursor getAddonSauce = databaseFunctions.getAddonTable("sauce");

        if (getAddonSauce != null && getAddonSauce.moveToFirst()) {
            do {
                numSauce++;
            } while (getAddonSauce.moveToNext());
        }

        //DESSERT COUNT
        Cursor getAddonDessert = databaseFunctions.getAddonTable("dessert");

        if (getAddonDessert != null && getAddonDessert.moveToFirst()) {
            do {
                numDessert++;
            } while (getAddonDessert.moveToNext());
        }

        //DRINK COUNT
        Cursor getAddonDrink = databaseFunctions.getAddonTable("drink");

        if (getAddonDrink != null && getAddonDrink.moveToFirst()) {
            do {
                numDrink++;
            } while (getAddonDrink.moveToNext());
        }

        numMenu = numRice + numMainDish + numSide + numSauce + numDessert + numDrink;
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