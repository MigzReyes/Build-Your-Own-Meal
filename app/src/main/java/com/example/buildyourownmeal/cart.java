package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class cart extends AppCompatActivity {

    //DATABASE
    databaseFunctions databaseFunctions;

    //RECYCLER
    private ArrayList<String> addonGroupId;
    private ArrayList<String> mealType, cartItemName, minusBtn, addBtn;
    private ArrayList<Integer> cartItemPrice, mealQuantity, trashBtn, userOrderId;
    private ArrayList<Bitmap> cartItemImg;
    private RecyclerView recyclerViewCart;

    private Dialog popUpLogInWarning;

    //LOCAL VARIABLE
    private ImageView backBtn;
    private TextView fragName, addMoreItems, totalPrice;
    private Button checkOutBtn;
    private LinearLayout changeSchedCon;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

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

                saveCartState();
                Intent intent = new Intent(cart.this, Navbar.class);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //SHARED PREFERENCE USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        String userRole = userSession.getString("role", "guest");
        userId = userSession.getInt("userId", 0);

        //VARIABLE REFERENCE
        backBtn = findViewById(R.id.backBtn);
        fragName = findViewById(R.id.sideFragName);
        checkOutBtn = findViewById(R.id.checkOutBtn);
        changeSchedCon = findViewById(R.id.changeSchedCon);
        addMoreItems = findViewById(R.id.addMoreItems);
        totalPrice = findViewById(R.id.totalPrice);

        //RECYCLER VIEW
        addonGroupId = new ArrayList<>();
        userOrderId = new ArrayList<>();
        mealType = new ArrayList<>();
        cartItemName = new ArrayList<>();
        cartItemPrice = new ArrayList<>();
        cartItemImg = new ArrayList<>();
        minusBtn = new ArrayList<>();
        addBtn = new ArrayList<>();
        mealQuantity = new ArrayList<>();
        trashBtn = new ArrayList<>();
        recyclerViewCart = findViewById(R.id.recyclerViewCart);

        setUpCartModel();

        recyclerViewAdapterCart recyclerViewAdapterCart = new recyclerViewAdapterCart(this, addonGroupId, mealType, cartItemName, minusBtn, addBtn, cartItemPrice, mealQuantity, trashBtn, userOrderId, cartItemImg);

        //ON PRICE UPDATE LISTENER
        com.example.buildyourownmeal.recyclerViewAdapterCart.setOnPriceUpdatedListener(new recyclerViewAdapterCart.OnPriceUpdateListener() {
            @Override
            public void OnPriceUpdate(int newTotalPrice) {
                totalPrice.setText(String.valueOf(newTotalPrice));
            }
        });

        recyclerViewAdapterCart.recalculateTotalPriceAndNotify();
        recyclerViewAdapterCart.notifyDataSetChanged();

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(recyclerViewAdapterCart);


        //BACK BUTTON LISTENER
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCartState();
                Intent intent = new Intent(cart.this, Navbar.class);
                startActivity(intent);
                finish();
            }
        });

        addMoreItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCartState();
                Intent intent = new Intent(cart.this, menu.class);
                startActivity(intent);
            }
        });

        //SET TEXT FOR APPBAR
        fragName.setText(R.string.cart);

        //POP UP ALERT
        popUpLogInWarning = new Dialog(this);
        popUpLogInWarning.setContentView(R.layout.pop_up_login_signup_alert);
        popUpLogInWarning.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpLogInWarning.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpLogInWarning.setCancelable(true);

        //CHECK OUT INTENT
         checkOutBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Button popUpAlertLogInBtn, popUpAlertSignUpBtn;

                 if (userRole.equals("guest")) {

                     popUpLogInWarning.show();

                     popUpAlertLogInBtn = popUpLogInWarning.findViewById(R.id.popUpAlertLogInBtn);
                     popUpAlertLogInBtn.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                                Intent intent = new Intent(cart.this, logIn.class);
                                popUpLogInWarning.dismiss();
                                startActivity(intent);
                         }
                     });

                     popUpAlertSignUpBtn = popUpLogInWarning.findViewById(R.id.popUpAlertSignUpBtn);
                     popUpAlertSignUpBtn.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             Intent intent = new Intent(cart.this, signUp.class);
                             startActivity(intent);
                         }
                     });

                 } else if (userRole.equals("user")) {
                     saveCartState();

                     Intent intent = new Intent(cart.this, checkout.class);
                     startActivity(intent);
                 }
             }
         });
    }

    private void setUpCartModel () {
        Cursor getUserOrder = databaseFunctions.getUserOrder(userId);

        if (getUserOrder != null && getUserOrder.moveToFirst()) {
            do {
                addonGroupId.add(getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("orderAddonId")));
                userOrderId.add(getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("userOrderId")));
                mealType.add(getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("mealType")));
                cartItemPrice.add(getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("orderTotalPrice")));
                cartItemName.add(getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("mealType")));
                int dbUserId = getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("userId"));
                Bitmap getMealImgBit = databaseFunctions.getImg(dbUserId);
                cartItemImg.add(getMealImgBit);
                minusBtn.add("-");
                addBtn.add("+");
                mealQuantity.add(getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("mealQuantity")));
                trashBtn.add(R.drawable.trashicon);
            } while (getUserOrder.moveToNext());
            getUserOrder.close();
        }
    }

    private void saveCartState() {
        for (int i = 0; i < userOrderId.size(); i++) {
            int orderId = userOrderId.get(i);
            int quantity = mealQuantity.get(i);
            int price = cartItemPrice.get(i);

            databaseFunctions.updateCartItem(orderId, quantity, price);
        }
    }
}