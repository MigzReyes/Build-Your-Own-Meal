package com.example.buildyourownmeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class recyclerViewAdapterCheckout extends RecyclerView.Adapter<recyclerViewAdapterCheckout.MyViewHolder> {
    private Context context;
    private ArrayList<recyclerCheckoutModel> recyclerCheckoutModels;

    public recyclerViewAdapterCheckout(Context context, ArrayList<recyclerCheckoutModel> recyclerCheckoutModels) {
        this.context = context;
        this.recyclerCheckoutModels = recyclerCheckoutModels;
    }

    @NonNull
    @Override
    public recyclerViewAdapterCheckout.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_checkout, parent, false);
        return new recyclerViewAdapterCheckout.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterCheckout.MyViewHolder holder, int position) {
        holder.checkoutMealImg.setImageResource(recyclerCheckoutModels.get(position).getCheckoutMealImg());
        holder.checkoutMealName.setText(recyclerCheckoutModels.get(position).getCheckoutMealName());
        holder.checkoutMealMainDish.setText(recyclerCheckoutModels.get(position).getCheckoutMealMainDish());
        holder.checkoutMealSideDish.setText(recyclerCheckoutModels.get(position).getCheckoutMealSideDish());
        holder.checkoutSauces.setText(recyclerCheckoutModels.get(position).getCheckoutSauces());
        holder.checkoutDesserts.setText(recyclerCheckoutModels.get(position).getCheckoutDesserts());
        holder.checkoutDrinks.setText(recyclerCheckoutModels.get(position).getCheckoutDrinks());
    }

    @Override
    public int getItemCount() {
        return recyclerCheckoutModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView checkoutMealImg;
        TextView checkoutMealName, checkoutMealMainDish, checkoutMealSideDish, checkoutSauces, checkoutDesserts, checkoutDrinks;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);

            checkoutMealImg = itemView.findViewById(R.id.checkoutMealImg);
            checkoutMealName = itemView.findViewById(R.id.checkoutMealName);
            checkoutMealMainDish = itemView.findViewById(R.id.checkoutMainDish);
            checkoutMealSideDish = itemView.findViewById(R.id.checkoutSideDish);
            checkoutSauces = itemView.findViewById(R.id.checkoutSauces);
            checkoutDesserts = itemView.findViewById(R.id.checkoutDesserts);
            checkoutDrinks = itemView.findViewById(R.id.checkoutDrinks);
        }
    }
}
