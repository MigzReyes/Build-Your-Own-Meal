package com.example.buildyourownmeal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class dropdownAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resources;
    private List<String> items;

    public dropdownAdapter(@NonNull Context context, int resources, List<String> items) {
        super(context, resources, items);
        this.context = context;
        this.resources = resources;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getDropDownView(position, convertView, parent);
        return v;
    }


}
