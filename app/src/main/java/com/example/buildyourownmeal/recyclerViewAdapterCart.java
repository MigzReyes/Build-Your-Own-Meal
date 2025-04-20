package com.example.buildyourownmeal;

import android.content.Context;
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
    private ArrayList<recyclerCartModel> recyclerCartModels;

    public recyclerViewAdapterCart(Context context, ArrayList<recyclerCartModel> recyclerCartModels) {
        this.context = context;
        this.recyclerCartModels = recyclerCartModels;
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
        holder.cartItemName.setText(recyclerCartModels.get(position).getCartItemName());
        holder.cartItemPrice.setText(recyclerCartModels.get(position).getCartItemPrice());
        holder.cartItemImg.setImageResource(recyclerCartModels.get(position).getCartItemImg());
    }

    @Override
    public int getItemCount() {
        return recyclerCartModels.size();
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
