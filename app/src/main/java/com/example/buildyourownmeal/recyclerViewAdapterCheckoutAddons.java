package com.example.buildyourownmeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapterCheckoutAddons extends RecyclerView.Adapter<recyclerViewAdapterCheckoutAddons.MyViewHolder> {

    private Context context;
    private ArrayList<String> addon;

    public recyclerViewAdapterCheckoutAddons(Context context, ArrayList<String> addon) {
        this.context = context;
        this.addon = addon;
    }

    @NonNull
    @Override
    public recyclerViewAdapterCheckoutAddons.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.recycler_checkout_addons, parent, false);
        return new recyclerViewAdapterCheckoutAddons.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterCheckoutAddons.MyViewHolder holder, int position) {
        holder.addon.setText(addon.get(position));


    }

    @Override
    public int getItemCount() {
        return addon.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView addon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            addon = itemView.findViewById(R.id.checkoutAddon);
        }
    }
}
