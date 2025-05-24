package com.example.buildyourownmeal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class checkout extends AppCompatActivity {


    //DATABASE
    databaseFunctions databaseFunctions;

    //RECYCLER VIEW
    private RecyclerView recyclerViewCheckout;
    private ArrayList<Bitmap> mealImg;
    private ArrayList<String> mealType, addBtn, minusBtn, editBtn, orderAddonId;
    private ArrayList<Integer> mealTotalPrice, mealQuantity, trashBtn, checkoutItemPerTotalPrice, userOrderId;

    private RadioButton priority, standard, scheduledDate;
    private TextView sideActName, payment, totalPrice, subtotalPrice, priorityPickUpPrice, addItemBtn;
    private Button orderBtn;
    private ImageView backBtn;
    private LinearLayout paymentMethodBtn, orderCon;
    private int userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //BACK PRESSED ON PHONE SYSTEM
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);

                Intent intent = new Intent(checkout.this, cart.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        boolean isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);
        userId = userSession.getInt("userId", 0);
        //SET ID
        sideActName = findViewById(R.id.sideFragName);
        backBtn = findViewById(R.id.backBtn);
        priority = findViewById(R.id.priorityFee);
        standard = findViewById(R.id.standardFee);
        scheduledDate = findViewById(R.id.scheduledDate);
        payment = findViewById(R.id.paymentMethod);
        orderBtn = findViewById(R.id.orderBtnCart);
        orderCon = findViewById(R.id.orderCon);
        totalPrice = findViewById(R.id.totalPrice);
        subtotalPrice = findViewById(R.id.subtotalPrice);
        priorityPickUpPrice = findViewById(R.id.priorityPickUpPrice);
        addItemBtn = findViewById(R.id.addItemBtn);

        //RECYCLER
        recyclerViewCheckout = findViewById(R.id.recyclerViewCheckout);
        orderAddonId = new ArrayList<>();
        userOrderId = new ArrayList<>();
        mealImg = new ArrayList<>();
        mealType = new ArrayList<>();
        mealTotalPrice = new ArrayList<>();
        mealQuantity = new ArrayList<>();
        trashBtn = new ArrayList<>();
        minusBtn = new ArrayList<>();
        mealQuantity = new ArrayList<>();
        addBtn = new ArrayList<>();
        editBtn = new ArrayList<>();

        setUpCheckoutModel();

        recyclerViewAdapterCheckout recyclerViewAdapterCheckout = new recyclerViewAdapterCheckout(this, orderAddonId, userOrderId, mealImg, mealType, addBtn, minusBtn, editBtn, mealTotalPrice, mealQuantity, trashBtn);

        com.example.buildyourownmeal.recyclerViewAdapterCheckout.setOnPriceUpdatedListener(new recyclerViewAdapterCart.OnPriceUpdateListener() {
            @Override
            public void OnPriceUpdate(int newTotalPrice) {
                totalPrice.setText(String.valueOf(newTotalPrice));
            }
        });

        recyclerViewAdapterCheckout.recalculateTotalPriceAndNotify();
        recyclerViewAdapterCheckout.notifyDataSetChanged();

        recyclerViewCheckout.setAdapter(recyclerViewAdapterCheckout);
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));


        //SET TOOLBAR NAME
        sideActName.setText(getString(R.string.smallCheckOut));

        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkout.this, cart.class);
                startActivity(intent);
            }
        });

        //RADIO BUTTON
        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard.setChecked(true);
                priority.setChecked(false);
                scheduledDate.setChecked(false);
            }
        });

        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard.setChecked(false);
                priority.setChecked(true);
                scheduledDate.setChecked(false);
            }
        });

        scheduledDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard.setChecked(false);
                priority.setChecked(false);
                scheduledDate.setChecked(true);

                BottomSheetDialog popUpSched = new BottomSheetDialog(checkout.this);
                View popUpLayout = LayoutInflater.from(checkout.this).inflate(R.layout.pop_up_pick_up, null);
                popUpSched.setContentView(popUpLayout);
                popUpSched.setCancelable(true);

                FrameLayout bottomSheet = popUpSched.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    bottomSheet.setBackground(null);
                }
                popUpSched.show();
            }
        });


        if (isUserLoggedIn) {
            orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (priority.isChecked() || standard.isChecked()) {


                    } else {
                        Toast.makeText(checkout.this, getString(R.string.choosePickUpOption), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.logInSignUpFirst), Toast.LENGTH_SHORT).show();
        }


        //ADD ITEM BUTTON
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkout.this, menu.class);
                startActivity(intent);
            }
        });

        //SET ID
        paymentMethodBtn = findViewById(R.id.paymentCon);

        //PAYMENT METHOD
        paymentMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkout.this, paymentMethod.class);
                startActivity(intent);
            }
        });

    }

    private void setUpCheckoutModel () {
        Cursor getCartData = databaseFunctions.getUserOrder(userId);

        if (getCartData != null && getCartData.moveToFirst()) {
            do {
                orderAddonId.add(getCartData.getString(getCartData.getColumnIndexOrThrow("orderAddonId")));
                userOrderId.add(getCartData.getInt(getCartData.getColumnIndexOrThrow("userOrderId")));
                byte[] byteArray = getCartData.getBlob(getCartData.getColumnIndexOrThrow("mealImg"));
                mealImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                mealType.add(getCartData.getString(getCartData.getColumnIndexOrThrow("mealType")));
                mealTotalPrice.add(getCartData.getInt(getCartData.getColumnIndexOrThrow("orderTotalPrice")));
                mealQuantity.add(getCartData.getInt(getCartData.getColumnIndexOrThrow("mealQuantity")));
                trashBtn.add(R.drawable.trashicon);
                minusBtn.add("-");
                addBtn.add("+");
                editBtn.add("Edit");
            } while (getCartData.moveToNext());
        }
    }
}