package com.example.buildyourownmeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class dropdown_gcash extends BaseAdapter {
    private Context context;
    private int[] images;
    private LayoutInflater inflater;

    public dropdown_gcash(Context context, int[] images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dropdown_gcash, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.gcashImage);
        imageView.setImageResource(images[position]);

        return convertView;
    }
}
