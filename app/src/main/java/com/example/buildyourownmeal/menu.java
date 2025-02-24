package com.example.buildyourownmeal;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class menu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        int[] menuBtn = {R.id.craftBtn, R.id.menuCon,};

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), menuItem.class);
                startActivity(intent);
            }
        };

        for (int id : menuBtn) {
            view.findViewById(id).setOnClickListener(clickListener);
        }


        return view;
    }
}