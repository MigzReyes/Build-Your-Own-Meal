package com.example.buildyourownmeal;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class cart extends AppCompatActivity {

    //LOCAL VARIABLE
    ImageView backBtn;
    TextView fragName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);


        //VARIABLE REFERENCE
        backBtn = findViewById(R.id.backBtn);
        fragName = findViewById(R.id.sideFragName);

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

    }
}