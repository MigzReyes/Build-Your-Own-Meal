package com.example.buildyourownmeal;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class paymentMethod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_method);

        Spinner dropdown = findViewById(R.id.dropdown);
        int[] image = {R.drawable.gcash};

        dropdown_gcash adapter = new dropdown_gcash(this, image);
        dropdown.setAdapter(adapter);
    }
}