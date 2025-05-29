package com.example.buildyourownmeal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class adminOrders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //DATABASE
    private databaseFunctions databaseFunctions;
    private recyclerViewAdapterAdminOrders recyclerViewAdapterAdminOrders;


    //RECYCLER
    private ArrayList<orderModel> orderModelsList;
    private RecyclerView adminOrdersRecycler;
    private ArrayList<String> customerName, customerEmail, customerNumber, orderDate, orderStatus, orderGroupId, pickUp, paymentMethod;
    private ArrayList<Integer> orderTotalPrice, orderCount, userId;
    private recyclerViewAdapterAdminOrders adminOrdersAdapter;



    private TextView filterNewest, filterOldest;
    private Spinner filterStatus;
    private SearchView searchbar;

    private DrawerLayout drawerLayout;
    private boolean deletedOrder = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_orders);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);
        recyclerViewAdapterAdminOrders = new recyclerViewAdapterAdminOrders();

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

        //REFERENCE
        filterNewest = findViewById(R.id.filterNewest);
        filterOldest = findViewById(R.id.filterOldest);
        filterStatus = findViewById(R.id.filterStatus);

        //SEARCH BAR
        searchbar = findViewById(R.id.searchbar);
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbar.setIconified(false);
            }
        });

        //COLLAPSE SEARCHVIEW WHEN CLICK OUTSIDE OF THE SEARCHVIEW
        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!searchbar.isShown()) {
                    return false;
                }

                float x = event.getRawX();
                float y = event.getRawY();

                Rect rect = new Rect();
                searchbar.getGlobalVisibleRect(rect);
                if (!rect.contains((int) x, (int) y)) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(searchbar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    searchbar.onActionViewCollapsed();
                }
                return false;
            }
        });

        //STATUS FILTER
        List<String> status = Arrays.asList("Processing", "Meal in progress", "Order is ready");
        dropdownAdapter dropdownAdapter = new dropdownAdapter(this, R.layout.custom_spinner_bg, status);
        dropdownAdapter.setDropDownViewResource(R.layout.custom_dropdown_bg);
        filterStatus.setAdapter(dropdownAdapter);

        //ALERT
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




        //RECYCLER
        adminOrdersRecycler = findViewById(R.id.adminOrdersRecycler);
        orderModelsList = new ArrayList<>();
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
        adminOrdersAdapter = new recyclerViewAdapterAdminOrders(this, userId, orderGroupId, customerName, customerEmail, customerNumber, orderDate, orderStatus, orderTotalPrice, orderCount, pickUp, paymentMethod, orderModelsList);
        adminOrdersRecycler.setAdapter(adminOrdersAdapter);

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterOrder(newText);
                return false;
            }
        });

        filterNewest.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                filterNewest.setTextColor(getColor(R.color.blackBoldLetters));
                filterOldest.setTextColor(getColor(R.color.greyLetters));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                orderModelsList.sort(new Comparator<orderModel>() {
                    @Override
                    public int compare(orderModel o1, orderModel o2) {
                        try {
                            Date date1 = sdf.parse(o1.getOrderDate());
                            Date date2 = sdf.parse(o2.getOrderDate());
                            return date2.compareTo(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                adminOrdersAdapter.notifyDataSetChanged();
            }
        });

        filterOldest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterNewest.setTextColor(getColor(R.color.greyLetters));
                filterOldest.setTextColor(getColor(R.color.blackBoldLetters));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                orderModelsList.sort(new Comparator<orderModel>() {
                    @Override
                    public int compare(orderModel o1, orderModel o2) {
                        try {
                            Date date1 = sdf.parse(o1.getOrderDate());
                            Date date2 = sdf.parse(o2.getOrderDate());
                            return date1.compareTo(date2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                adminOrdersAdapter.notifyDataSetChanged();
            }
        });

        filterStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();

                ArrayList<orderModel> filteredList = new ArrayList<>();
                for (orderModel item : orderModelsList) {
                    if (item.getOrderStatus().equalsIgnoreCase(selectedFilter)) {
                        filteredList.add(item);
                    }
                }
                adminOrdersAdapter.filterList(filteredList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public void deleteOrder(String orderGroupIdDelete) {
        int index = orderGroupId.indexOf(orderGroupIdDelete);

        if (index != -1) {
            orderModelsList.remove(index);
            adminOrdersAdapter.notifyItemRemoved(index);
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
                /*pickUp.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("pickUp")));
                paymentMethod.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("paymentMethod")));
                userId.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("userId")));
                orderGroupId.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderGroupId")));
                customerNumber.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("contactNumber")));
                orderCount.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("adminOrderId")));
                orderDate.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderedDate")));
                orderTotalPrice.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("totalPrice")));
                orderStatus.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("status")));*/

                String modelPickUp = getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("pickUp"));
                String modelPaymentMethod = getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("paymentMethod"));
                int modelUserId = getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("userId"));
                String modelOrderGroupId = getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderGroupId"));
                String modelCustomerNumber = getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("contactNumber"));
                int modelOrderCount = getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("adminOrderId"));
                String modelOrderDate = getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderedDate"));
                int modelOrderTotalPrice = getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("totalPrice"));
                String modelOrderStatus = getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("status"));

                orderModelsList.add(new orderModel(modelCustomerNumber, modelOrderDate, modelOrderStatus, modelOrderGroupId, modelPickUp, modelPaymentMethod, modelOrderTotalPrice, modelOrderCount, modelUserId));
            } while (getAdminUserOrder.moveToNext());
        }
    }

    private void filterOrder(String searchBarInputText) {
        ArrayList<orderModel> filteredList = new ArrayList<>();


        for (orderModel item : orderModelsList) {
            if (item.getOrderStatus().toLowerCase().contains(searchBarInputText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Log.d("Search View", "Search is empty");
        } else {
            adminOrdersAdapter.filterList(filteredList);
        }
    }
}