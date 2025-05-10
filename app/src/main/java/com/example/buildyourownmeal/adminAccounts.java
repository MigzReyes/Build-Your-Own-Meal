package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class adminAccounts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    //DATABASE
    private databaseFunctions databaseFunctions;

    //RECYCLER
    private RecyclerView recyclerViewAccount;
    private ArrayList<Integer> userId;
    private ArrayList<String> username, userEmail, userContactNum, userPassword, userBan, userRole;
    //VARIABLES
    private Button addUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_accounts);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //REFERENCE
        addUserBtn = findViewById(R.id.addUserBtn);

        //RECYCLER VIEW
        recyclerViewAccount = findViewById(R.id.recyclerViewAccount);
        userId = new ArrayList<>();
        username = new ArrayList<>();
        userEmail = new ArrayList<>();
        userContactNum = new ArrayList<>();
        userPassword = new ArrayList<>();
        userBan = new ArrayList<>();
        userRole = new ArrayList<>();

        setUpAccountModel();

        recyclerViewAccount.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterAdminAccount recyclerViewAdapterAdminAccount = new recyclerViewAdapterAdminAccount(this, userId, username, userEmail, userContactNum, userPassword, userBan, userRole);
        recyclerViewAccount.setAdapter(recyclerViewAdapterAdminAccount);


        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //SIDE BAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.adminAccount);
        NavigationView navigationView = findViewById(R.id.sidebar);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_sidebar, R.string.close_sidebar);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        toolbar.setTitleTextColor(getColor(R.color.white));
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //SEARCH BAR
        SearchView searchbar = findViewById(R.id.searchbar);
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbar.setIconified(false);
            }
        });

        //ADD USER BUTTON
        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminAccounts.this, adminAddUser.class);
                startActivity(intent);
            }
        });
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
        } else if (id == R.id.adminMeals) {
            Intent intent = new Intent(this, adminMeals.class);
            startActivity(intent);
        } else if (id == R.id.adminMenu) {
            Intent intent = new Intent(this, adminMenu.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpAccountModel() {
        Cursor getUserInfo = databaseFunctions.getAllUser();

        if (getUserInfo != null && getUserInfo.moveToFirst()) {
            do {
                userId.add(getUserInfo.getInt(getUserInfo.getColumnIndexOrThrow("userId")));
                username.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("username")));
                userEmail.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("email")));
                userContactNum.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("contactNumber")));
                userPassword.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("password")));
                userBan.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("ban")));
                userRole.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("role")));
            } while (getUserInfo.moveToNext());
            getUserInfo.close();
        }
    }


    public void popUpAlert(int position) {
        Dialog popUpAlert;
        Button deleteAccBtn;
        TextView cancelAccDeleteBtn;

        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_delete_account);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);
        popUpAlert.show();

        cancelAccDeleteBtn = popUpAlert.findViewById(R.id.cancelAccDeleteBtn);
        deleteAccBtn = popUpAlert.findViewById(R.id.deleteAccBtn);

        //ALERT TEXT
        cancelAccDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAlert.dismiss();
            }
        });

        //CLOSE
        deleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean deleteUser = databaseFunctions.deleteAccount("account", position);

                if (deleteUser) {

                    popUpAlert.dismiss();
                }
            }
        });

    }
}