package com.example.buildyourownmeal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class cart extends AppCompatActivity {

    private RadioButton priority, standard;
    private TextView itemCount, mealNameSummary, mealNameSubtotal, addOn, mealPriceSum, mealPriceSubtotal, mealPriceTotal, payment, addItemBtn;
    private Button orderBtn;
    private LinearLayout paymentMethodBtn, orderCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        //SET ID
        priority = findViewById(R.id.priorityFee);
        standard = findViewById(R.id.standardFee);
        itemCount = findViewById(R.id.itemCount);
        mealNameSummary = findViewById(R.id.mealName);
        mealNameSubtotal = findViewById(R.id.mealName1);
        addOn = findViewById(R.id.addOn);
        mealPriceSum = findViewById(R.id.priceItem);
        mealPriceSubtotal = findViewById(R.id.priceItem1);
        mealPriceTotal = findViewById(R.id.priceItem2);
        payment = findViewById(R.id.paymentMethod);
        orderBtn = findViewById(R.id.orderBtnCart);
        orderCon = findViewById(R.id.orderCon);
        addItemBtn = findViewById(R.id.addItemBtn);

        //SHARED PREFERENCE
        SharedPreferences menuItem = getSharedPreferences("menuItem", MODE_PRIVATE);
        boolean menuSession = menuItem.getBoolean("menuSession", false);
        String selectedItems = menuItem.getString("selectedItems", "No selected items");
        int dbItemCount = menuItem.getInt("itemCount", 0);
        String mealName = menuItem.getString("mealName", "No meal name");
        float totalPrice = menuItem.getFloat("totalPrice", 0);

        if (menuSession) {
            mealNameSummary.setText(mealName);
            mealNameSubtotal.setText(mealName);
            addOn.setText(selectedItems);

            String itemCountX = dbItemCount + "x";
            itemCount.setText(itemCountX);

            String totalPricePesos = totalPrice + "₱";
            mealPriceTotal.setText(totalPricePesos);
            mealPriceSubtotal.setText(totalPricePesos);
            mealPriceSum.setText(totalPricePesos);

        } else {
            orderCon.setVisibility(View.GONE);
        }

        //ADD ITEM BUTTON
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cart.this, menu.class);
                startActivity(intent);
            }
        });

        //SET ID
        paymentMethodBtn = findViewById(R.id.paymentCon);

        //PAYMENT METHOD
        paymentMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cart.this, paymentMethod.class);
                startActivity(intent);
            }
        });

        //BACK BUTTON
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackPress();
            }
        });
    }

    public void handleBackPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager();
        } else {
            finish();
        }
    }
}