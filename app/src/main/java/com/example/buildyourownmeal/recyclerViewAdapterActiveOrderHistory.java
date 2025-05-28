package com.example.buildyourownmeal;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapterActiveOrderHistory extends RecyclerView.Adapter<recyclerViewAdapterActiveOrderHistory.MyViewHolder> {

    //DATABASE
    private databaseFunctions databaseFunctions;

    private Context context;
    private ArrayList<String> orderName, orderDate, orderAddonId, orderGroupId, orderPickUp, orderPayment, addonName;
    private ArrayList<Integer> orderPrice, orderId, addonQuantity;
    private ArrayList<Bitmap> orderImg;
    private int userId;

    public recyclerViewAdapterActiveOrderHistory(Context context, int userId, ArrayList<String> orderName, ArrayList<String> orderAddonId, ArrayList<String> orderGroupId, ArrayList<Integer> orderPrice, ArrayList<Bitmap> orderImg, ArrayList<Integer> orderId) {
        this.context = context;
        this.userId = userId;
        this.orderName = orderName;
        this.orderAddonId = orderAddonId;
        this.orderGroupId = orderGroupId;
        this.orderPrice = orderPrice;
        this.orderImg = orderImg;
        this.orderId = orderId;

        orderPickUp = new ArrayList<>();
        orderDate = new ArrayList<>();
        orderPayment = new ArrayList<>();
    }


    @NonNull
    @Override
    public recyclerViewAdapterActiveOrderHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_order_history, parent, false);
        return new recyclerViewAdapterActiveOrderHistory.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterActiveOrderHistory.MyViewHolder holder, int position) {
        databaseFunctions = new databaseFunctions(context);

        setUpAdminOrder(orderGroupId.get(position));

        holder.orderName.setText(orderName.get(position));
        holder.orderImg.setImageBitmap(orderImg.get(position));
        holder.orderDate.setText(orderDate.get(position));
        holder.orderPickUp.setText(orderPickUp.get(position));
        holder.orderPayment.setText(orderPayment.get(position));
        holder.orderPrice.setText(String.valueOf(orderPrice.get(position)));

        addonName = new ArrayList<>();
        addonQuantity = new ArrayList<>();

        setUpAddonModel(userId, orderAddonId.get(position));

        recyclerViewAdapterAdminUserOrderAddon addonAdapter = new recyclerViewAdapterAdminUserOrderAddon(context, addonName, addonQuantity);
        holder.addonRecycler.setLayoutManager(new LinearLayoutManager(context));
        holder.addonRecycler.setAdapter(addonAdapter);

    }

    @Override
    public int getItemCount() {
        return orderName.size();
    }

    private void setUpAdminOrder(String orderGroupId) {
        Cursor getAdminOrder = databaseFunctions.getAdminOrder(orderGroupId);

        if (getAdminOrder != null && getAdminOrder.moveToFirst()) {
            do {
                orderPickUp.add(getAdminOrder.getString(getAdminOrder.getColumnIndexOrThrow("pickUp")));
                orderPayment.add(getAdminOrder.getString(getAdminOrder.getColumnIndexOrThrow("paymentMethod")));
                orderDate.add(getAdminOrder.getString(getAdminOrder.getColumnIndexOrThrow("orderedDate")));
            } while (getAdminOrder.moveToNext());
        }
    }

    private void setUpAddonModel(int userId, String orderAddonId) {
        Cursor getOrderAddon = databaseFunctions.getAdminUserOrderAddon(userId, orderAddonId);

        if (getOrderAddon != null && getOrderAddon.moveToFirst()) {
            do {
                addonName.add(getOrderAddon.getString(getOrderAddon.getColumnIndexOrThrow("addon")));
                addonQuantity.add(getOrderAddon.getInt(getOrderAddon.getColumnIndexOrThrow("quantity")));
            } while (getOrderAddon.moveToNext());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView  orderName, orderPrice, orderDate, orderPickUp, orderPayment;
        ImageView orderImg;
        RecyclerView addonRecycler;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.orderName);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderImg = itemView.findViewById(R.id.orderImg);
            orderPickUp = itemView.findViewById(R.id.orderPickUp);
            orderPayment = itemView.findViewById(R.id.orderPayment);

            addonRecycler = itemView.findViewById(R.id.addonsRecycler);
        }
    }
}
