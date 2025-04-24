package com.example.buildyourownmeal;

import static android.view.View.GONE;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.LinearLayout;

public class preMadeMeal extends AppCompatActivity {

    //VARIABLE DECLARATION


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pre_made_meal);

        //VARIABLE REFERENCE


    }
}