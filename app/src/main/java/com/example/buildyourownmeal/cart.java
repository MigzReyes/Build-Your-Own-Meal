package com.example.buildyourownmeal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class cart extends Fragment {

    //LOCAL VARIABLE
    ImageView backBtn;
    TextView fragName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        //VARIABLE REFERENCE
        backBtn = view.findViewById(R.id.backBtn);
        fragName = view.findViewById(R.id.sideFragName);

        //BACK BUTTON LISTENER
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        //SET TEXT FOR APPBAR
        fragName.setText(R.string.cart);


        return view;
    }
}