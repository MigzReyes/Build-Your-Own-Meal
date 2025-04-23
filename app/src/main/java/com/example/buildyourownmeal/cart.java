package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class cart extends AppCompatActivity {

    //RECYCLER
    private ArrayList<recyclerCartModel> recyclerCartModelArrayList = new ArrayList<>();
    private int[] cartItemImg = {R.drawable.chickenkaraagemeal, R.drawable.tunasisigmeal, R.drawable.veggieballsmeal,
            R.drawable.chickenkaraagemeal, R.drawable.tunasisigmeal, R.drawable.veggieballsmeal};

    private RecyclerView recyclerViewCart;
    private Dialog popUpLogInWarning;

    //LOCAL VARIABLE
    private ImageView backBtn;
    private TextView fragName;
    private Button checkOutBtn;
    private LinearLayout changeSchedCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        //REYCLER VIEW
        recyclerViewCart = findViewById(R.id.recyclerViewCart);

        setUpCartModel();

        recyclerViewAdapterCart recyclerViewAdapterCart = new recyclerViewAdapterCart(this, recyclerCartModelArrayList);
        recyclerViewCart.setAdapter(recyclerViewAdapterCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setNestedScrollingEnabled(false);

        //SHARED PREFERENCE USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        String userRole = userSession.getString("role", "guest");


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
        String[] cartItemName = getResources().getStringArray(R.array.comboMealName);
        String[] cartItemPrice = getResources().getStringArray(R.array.cartItemPrices);

        for (int i = 0; i < cartItemName.length; i++) {
            recyclerCartModelArrayList.add(new recyclerCartModel(cartItemImg[i], cartItemPrice[i], cartItemName[i]));
        }
    }
}