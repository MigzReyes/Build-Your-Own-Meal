package com.example.buildyourownmeal;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class paymentMethod extends AppCompatActivity {

    //VARIABLE DECLARATION
    RadioButton cash, gCash;
    ImageView backBtn;
    TextView sideActName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_method);

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        sideActName = findViewById(R.id.sideFragName);
        cash = findViewById(R.id.cash);
        gCash = findViewById(R.id.gCash);

        //BACK BUTTON
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(paymentMethod.this, checkout.class);
                startActivity(intent);
            }
        });

        //ACTIVITY NAME TOOLBAR
        sideActName.setText(getString(R.string.paymentMethod));

        //RADIO BUTTON
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash.setChecked(true);
                gCash.setChecked(false);
            }
        });

        gCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash.setChecked(false);
                gCash.setChecked(true);
            }
        });

    }
}