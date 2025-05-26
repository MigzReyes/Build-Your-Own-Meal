package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class adminOrders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //DATABASE
    private databaseFunctions databaseFunctions;

    //RECYCLER
    private RecyclerView adminOrdersRecycler;
    private ArrayList<String> customerName, customerEmail, customerNumber, orderDate, orderStatus, orderGroupId, pickUp, paymentMethod;
    private ArrayList<Integer> orderTotalPrice, orderCount, userId;
    private recyclerViewAdapterAdminOrders adminOrdersAdapter;

    private DrawerLayout drawerLayout;
    private boolean deletedOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_orders);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //SIDEBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.adminOrders);
        NavigationView navigationView = findViewById(R.id.sidebar);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_sidebar, R.string.close_sidebar);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        toolbar.setTitleTextColor(getColor(android.R.color.white));
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

        deletedOrder = getIntent().getBooleanExtra("deletedOrder", false);
        if (deletedOrder) {
            Dialog popUpAlert;
            TextView alertText;
            Button closeBtn;

            popUpAlert = new Dialog(adminOrders.this);
            popUpAlert.setContentView(R.layout.pop_up_alerts);
            popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
            popUpAlert.setCancelable(true);
            popUpAlert.show();

            alertText = popUpAlert.findViewById(R.id.alertText);
            alertText.setText("Order successfully deleted");

            closeBtn = popUpAlert.findViewById(R.id.closeBtn);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUpAlert.dismiss();
                }
            });
        }


        //REFERENCE

        //RECYCLER
        adminOrdersRecycler = findViewById(R.id.adminOrdersRecycler);
        userId = new ArrayList<>();
        orderGroupId = new ArrayList<>();
        orderCount = new ArrayList<>();
        customerName = new ArrayList<>();
        customerNumber = new ArrayList<>();
        customerEmail = new ArrayList<>();
        orderDate = new ArrayList<>();
        orderTotalPrice = new ArrayList<>();
        orderStatus = new ArrayList<>();
        pickUp = new ArrayList<>();
        paymentMethod = new ArrayList<>();

        setUpAdminOrder();

        adminOrdersRecycler.setLayoutManager(new LinearLayoutManager(this));
        adminOrdersAdapter = new recyclerViewAdapterAdminOrders(this, userId, orderGroupId, customerName, customerEmail, customerNumber, orderDate, orderStatus, orderTotalPrice, orderCount, pickUp, paymentMethod);
        adminOrdersRecycler.setAdapter(adminOrdersAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            String updatedOrderGroupId = data.getStringExtra("orderGroupId");
            int newTotalPrice = data.getIntExtra("newTotalPrice", 0);

            int index = orderGroupId.indexOf(updatedOrderGroupId);
            if (index != -1) {
                databaseFunctions.updateAdminOrderTotalPrice(updatedOrderGroupId, newTotalPrice);
                orderTotalPrice.set(index, newTotalPrice);
                adminOrdersAdapter.notifyItemChanged(index);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.adminDashboard) {
            Intent intent = new Intent(this, admin.class);
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
                    Intent intent = new Intent(adminOrders.this, introduction_screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUpAdminOrder() {
        Cursor getAdminUserOrder = databaseFunctions.getAdminOrder();

        if (getAdminUserOrder != null && getAdminUserOrder.moveToFirst()) {
            do {
                pickUp.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("pickUp")));
                paymentMethod.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("paymentMethod")));
                userId.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("userId")));
                orderGroupId.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderGroupId")));
                customerNumber.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("contactNumber")));
                orderCount.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("adminOrderId")));
                orderDate.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderedDate")));
                orderTotalPrice.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("totalPrice")));
                orderStatus.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("status")));
            } while (getAdminUserOrder.moveToNext());
        }
    }
}