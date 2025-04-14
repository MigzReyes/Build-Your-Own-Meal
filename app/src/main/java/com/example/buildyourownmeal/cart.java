package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class cart extends AppCompatActivity {

    //LOCAL VARIABLE
    ImageView backBtn;
    TextView fragName, changeSchedBtn;
    Button checkOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);


        //VARIABLE REFERENCE
        backBtn = findViewById(R.id.backBtn);
        fragName = findViewById(R.id.sideFragName);
        checkOutBtn = findViewById(R.id.checkOutBtn);
        changeSchedBtn = findViewById(R.id.changeSchedBtn);

        //BACK BUTTON LISTENER
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        //SET TEXT FOR APPBAR
        fragName.setText(R.string.cart);

        //CHECK OUT INTENT
         checkOutBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(cart.this, checkout.class);
                 startActivity(intent);
             }
         });

        //POP UP DIALOG CHANGE SCHEDULE
        changeSchedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog popUpSched = new BottomSheetDialog(cart.this);
                View popUpLayout = LayoutInflater.from(cart.this).inflate(R.layout.pop_up_pick_up, null);
                popUpSched.setContentView(popUpLayout);
                popUpSched.setCancelable(true);
                popUpSched.show();
            }
        });

    }
}