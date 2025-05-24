package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adminUserOrdersList extends AppCompatActivity {

    //DATABASE
    private databaseFunctions databaseFunctions;

    //RECYCLER
    private RecyclerView adminUserOrderRecycler;
    private ArrayList<String> customerName, mealName, orderGroupId, orderAddonId;
    private ArrayList<Integer> mealQuantity, mealTotalPrice, userId;
    private ArrayList<Bitmap> mealImg;

    private ImageView backBtn;
    private TextView activityName, deleteAllBtn;
    private String intentOrderGroupId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_user_orders_list);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //INTENT EXTRA
        intentOrderGroupId = getIntent().getStringExtra("orderGroupId");
        int intentUserId = getIntent().getIntExtra("userId", 0);
        Log.d("may error ka", intentOrderGroupId);

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        activityName = findViewById(R.id.sideActName);
        deleteAllBtn = findViewById(R.id.deleteAllBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activityName.setText(getString(R.string.orders));

        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog popUpAlert;
                TextView alertText;
                Button cancelBtn, deleteBtn;

                popUpAlert = new Dialog(adminUserOrdersList.this);
                popUpAlert.setContentView(R.layout.pop_up_delete_addon);
                popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                popUpAlert.setCancelable(true);
                popUpAlert.show();

                alertText = popUpAlert.findViewById(R.id.alertText);
                alertText.setText("Are you sure you want to delete all the order?");

                cancelBtn = popUpAlert.findViewById(R.id.cancelBtn);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpAlert.dismiss();
                    }
                });

                deleteBtn = popUpAlert.findViewById(R.id.deleteAddonBtn);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean deleteAllOrders = databaseFunctions.deleteAdminOrder(intentOrderGroupId);

                        if (deleteAllOrders) {
                            Intent intent = new Intent(adminUserOrdersList.this, adminOrders.class);
                            intent.putExtra("deletedAllOrders", true);
                            intent.putExtra("orderGroupId", intentOrderGroupId);
                            intent.putExtra("userId", intentUserId);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        //RECYCLER
        adminUserOrderRecycler = findViewById(R.id.adminUserOrderRecycler);
        userId = new ArrayList<>();
        mealImg = new ArrayList<>();
        orderGroupId = new ArrayList<>();
        orderAddonId = new ArrayList<>();
        customerName = new ArrayList<>();
        mealName = new ArrayList<>();
        mealQuantity = new ArrayList<>();
        mealTotalPrice = new ArrayList<>();

        setUpUserOrdersList();

        adminUserOrderRecycler.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterAdminUserOrder adminUserOrderAdapter = new recyclerViewAdapterAdminUserOrder(this, customerName, mealName, orderGroupId, orderAddonId, mealQuantity, mealTotalPrice, userId, mealImg);
        adminUserOrderRecycler.setAdapter(adminUserOrderAdapter);
    }

    private void setUpUserOrdersList() {
        Cursor getAdminUserOrder = databaseFunctions.getAdminUserOrder(intentOrderGroupId);

        if (getAdminUserOrder != null & getAdminUserOrder.moveToFirst()) {
            do {
                userId.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("userId")));
                orderAddonId.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderAddonId")));
                orderGroupId.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderGroupId")));
                byte[] byteArray = getAdminUserOrder.getBlob(getAdminUserOrder.getColumnIndexOrThrow("mealImg"));
                mealImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));

                //CHANGE THIS IF MAY MEAL NAME NA
                mealName.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("mealType")));

                mealQuantity.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("mealQuantity")));
                mealTotalPrice.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("orderTotalPrice")));
            } while (getAdminUserOrder.moveToNext());
        }
    }

    private void popUpAlert(String setText) {
        Dialog popUpAlert;
        TextView alertText;
        Button closeBtn;

        popUpAlert = new Dialog(adminUserOrdersList.this);
        popUpAlert.setContentView(R.layout.pop_up_alerts);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);
        popUpAlert.show();

        alertText = popUpAlert.findViewById(R.id.alertText);
        alertText.setText(setText);

        closeBtn = popUpAlert.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAlert.dismiss();
            }
        });
    }
}