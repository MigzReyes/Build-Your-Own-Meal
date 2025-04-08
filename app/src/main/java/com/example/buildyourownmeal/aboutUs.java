package com.example.buildyourownmeal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class aboutUs extends Fragment {

    //LOCAL VARIABLE
    TextView fragName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        //VARIABLE REFERENCE
        fragName = view.findViewById(R.id.fragName);
        fragName.setText(R.string.aboutUs);


        return view;
    }
}