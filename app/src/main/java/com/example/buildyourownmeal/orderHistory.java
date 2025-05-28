package com.example.buildyourownmeal;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class orderHistory extends AppCompatActivity {

    //DATABASE
    private databaseFunctions databaseFunctions;

    //ACTIVE ORDER RECYCLER
    private RecyclerView activeOrderRecycler;
    private ArrayList<String> activeOrderName, activeOrderAddonId, activeOrderGroupId;
    private ArrayList<Integer> activeOrderPrice, activeOrderId;
    private ArrayList<Bitmap> activeOrderImg;

    //PAST ORDER RECYCLER
    private RecyclerView pastOrderRecycler;
    private ArrayList<String> pastOrderName, pastOrderAddonId, pastOrderGroupId;
    private ArrayList<Integer> pastOrderPrice, pastOrderId;
    private ArrayList<Bitmap> pastOrderImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //BACK BUTTON
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ACTIVITY TITLE
        TextView sideFragName = findViewById(R.id.sideFragName);
        sideFragName.setText(R.string.orderHis);

        //SHARED PREFERENCE
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        int getUserId = userSession.getInt("userId", 0);


        //ACTIVE ORDER RECYCLER
        activeOrderRecycler = findViewById(R.id.activeOrderRecycler);
        activeOrderId = new ArrayList<>();
        activeOrderAddonId = new ArrayList<>();
        activeOrderGroupId = new ArrayList<>();
        activeOrderName = new ArrayList<>();
        activeOrderImg = new ArrayList<>();
        activeOrderPrice = new ArrayList<>();

        setUpActiveOrderModel(getUserId);

        recyclerViewAdapterActiveOrderHistory activeOrderAdapter = new recyclerViewAdapterActiveOrderHistory(this, getUserId, activeOrderName, activeOrderAddonId, activeOrderGroupId, activeOrderPrice, activeOrderImg, activeOrderId);
        activeOrderRecycler.setLayoutManager(new LinearLayoutManager(this));
        activeOrderRecycler.setAdapter(activeOrderAdapter);

        //PAST ORDER RECYCLER
        pastOrderRecycler = findViewById(R.id.pastOrderRecycler);
        pastOrderId = new ArrayList<>();
        pastOrderAddonId = new ArrayList<>();
        pastOrderGroupId = new ArrayList<>();
        pastOrderName = new ArrayList<>();
        pastOrderImg = new ArrayList<>();
        pastOrderPrice = new ArrayList<>();

        setUpPastOrderModel(getUserId);

        recyclerViewAdapterPastOrderHistory pastOrderAdapter = new recyclerViewAdapterPastOrderHistory(this, getUserId, pastOrderName, pastOrderAddonId, pastOrderGroupId, pastOrderPrice, pastOrderImg, pastOrderId);
        pastOrderRecycler.setLayoutManager(new LinearLayoutManager(this));
        pastOrderRecycler.setAdapter(pastOrderAdapter);
    }

    private void setUpPastOrderModel(int userId) {
        Cursor getOrderHistory = databaseFunctions.getOrderHistory(userId);

        if (getOrderHistory != null && getOrderHistory.moveToFirst()) {
            do {
                pastOrderId.add(getOrderHistory.getInt(getOrderHistory.getColumnIndexOrThrow("userOrderId")));
                pastOrderGroupId.add(getOrderHistory.getString(getOrderHistory.getColumnIndexOrThrow("orderGroupId")));
                pastOrderAddonId.add(getOrderHistory.getString(getOrderHistory.getColumnIndexOrThrow("orderAddonId")));
                pastOrderName.add(getOrderHistory.getString(getOrderHistory.getColumnIndexOrThrow("mealType")));
                byte[] byteArray = getOrderHistory.getBlob(getOrderHistory.getColumnIndexOrThrow("mealImg"));
                pastOrderImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                pastOrderPrice.add(getOrderHistory.getInt(getOrderHistory.getColumnIndexOrThrow("orderTotalPrice")));
            } while (getOrderHistory.moveToNext());
        }
    }

    private void setUpActiveOrderModel(int userId) {
        Cursor getAdminUserOrder = databaseFunctions.getAdminUserOrder(userId);

        if (getAdminUserOrder != null && getAdminUserOrder.moveToFirst()) {
            do {
                activeOrderId.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("userOrderId")));
                activeOrderGroupId.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderGroupId")));
                activeOrderAddonId.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("orderAddonId")));
                activeOrderName.add(getAdminUserOrder.getString(getAdminUserOrder.getColumnIndexOrThrow("mealType")));
                byte[] byteArray = getAdminUserOrder.getBlob(getAdminUserOrder.getColumnIndexOrThrow("mealImg"));
                activeOrderImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                activeOrderPrice.add(getAdminUserOrder.getInt(getAdminUserOrder.getColumnIndexOrThrow("orderTotalPrice")));
            } while (getAdminUserOrder.moveToNext());
        }
    }
}