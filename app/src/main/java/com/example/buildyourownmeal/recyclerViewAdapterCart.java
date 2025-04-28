package com.example.buildyourownmeal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapterCart extends RecyclerView.Adapter<recyclerViewAdapterCart.MyViewHolder> {
    private Context context;
    private ArrayList<String> cartItemName;
    private ArrayList<Integer> cartItemPrice;
    private ArrayList<Bitmap> cartItemImg;

    public recyclerViewAdapterCart(Context context, ArrayList<String> cartItemName, ArrayList<Integer> cartItemPrice, ArrayList<Bitmap> cartItemImg) {
        this.context = context;
        this.cartItemName = cartItemName;
        this.cartItemPrice = cartItemPrice;
        this.cartItemImg = cartItemImg;
    }

    @NonNull
    @Override
    public recyclerViewAdapterCart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_cart, parent, false);
        return new recyclerViewAdapterCart.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterCart.MyViewHolder holder, int position) {
        holder.cartItemName.setText(cartItemName.get(position));
        holder.cartItemPrice.setText(String.valueOf(cartItemPrice.get(position)));
        holder.cartItemImg.setImageBitmap(cartItemImg.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItemName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cartItemImg;
        TextView cartItemName, cartItemPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cartItemImg = itemView.findViewById(R.id.cartItemImg);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
        }
    }
}
