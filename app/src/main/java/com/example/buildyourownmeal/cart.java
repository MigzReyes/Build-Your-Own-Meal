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
    private ArrayList<String> cartItemName;
    private ArrayList<Integer> cartItemPrice;
    private ArrayList<Bitmap> cartItemImg;
    private RecyclerView recyclerViewCart;

    private Dialog popUpLogInWarning;

    //LOCAL VARIABLE
    private ImageView backBtn;
    private TextView fragName;
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

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //SHARED PREFERENCE USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        String userRole = userSession.getString("role", "guest");
        userId = userSession.getInt("userId", 0);

        //RECYCLER VIEW
        cartItemName = new ArrayList<>();
        cartItemPrice = new ArrayList<>();
        cartItemImg = new ArrayList<>();
        recyclerViewCart = findViewById(R.id.recyclerViewCart);

        setUpCartModel();

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterCart recyclerViewAdapterCart = new recyclerViewAdapterCart(this, cartItemName, cartItemPrice, cartItemImg);
        recyclerViewCart.setAdapter(recyclerViewAdapterCart);

        //VARIABLE REFERENCE
        backBtn = findViewById(R.id.backBtn);
        fragName = findViewById(R.id.sideFragName);
        checkOutBtn = findViewById(R.id.checkOutBtn);
        changeSchedCon = findViewById(R.id.changeSchedCon);

        //BACK BUTTON LISTENER
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                cartItemPrice.add(getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("orderTotalPrice")));
                cartItemName.add(getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("mealType")));
                int dbUserId = getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("userId"));
                Bitmap getMealImgBit = databaseFunctions.getImg(dbUserId);
                cartItemImg.add(getMealImgBit);
            } while (getUserOrder.moveToNext());
            getUserOrder.close();
        }

    }
}