package com.example.buildyourownmeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapterAdminUserOrderAddon extends RecyclerView.Adapter<recyclerViewAdapterAdminUserOrderAddon.MyViewHolder>{

    private Context context;
    private ArrayList<String> addonName;
    private ArrayList<Integer> addonQuantity;

    public recyclerViewAdapterAdminUserOrderAddon(Context context, ArrayList<String> addonName, ArrayList<Integer> addonQuantity) {
        this.context = context;
        this.addonName = addonName;
        this.addonQuantity = addonQuantity;
    }

    @NonNull
    @Override
    public recyclerViewAdapterAdminUserOrderAddon.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.recycler_admin_user_order_addon, parent, false);
        return new recyclerViewAdapterAdminUserOrderAddon.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterAdminUserOrderAddon.MyViewHolder holder, int position) {
        holder.addonName.setText(addonName.get(position));
        holder.addonQuantity.setText(String.valueOf(addonQuantity.get(position)));
    }

    @Override
    public int getItemCount() {
        return addonName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView addonName, addonQuantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            addonName = itemView.findViewById(R.id.addonName);
            addonQuantity = itemView.findViewById(R.id.addonQuantity);
        }
    }
}
