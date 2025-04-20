package com.example.buildyourownmeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapterHomeCombos extends RecyclerView.Adapter<recyclerViewAdapterHomeCombos.MyViewHolder> {
    public Context context;
    private ArrayList<recyclerHomeCombosModel> recyclerHomeCombosModels;

    public recyclerViewAdapterHomeCombos(Context context, ArrayList<recyclerHomeCombosModel> recyclerHomeCombosModels) {
        this.context = context;
        this.recyclerHomeCombosModels = recyclerHomeCombosModels;
    }
    @NonNull
    @Override
    public recyclerViewAdapterHomeCombos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_home_combos, parent, false);
        return new recyclerViewAdapterHomeCombos.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterHomeCombos.MyViewHolder holder, int position) {
        holder.comboMealName.setText(recyclerHomeCombosModels.get(position).getComboMealName());
        holder.comboMealDescription.setText(recyclerHomeCombosModels.get(position).getComboMealDescription());
        holder.comboMealImg.setImageResource(recyclerHomeCombosModels.get(position).getComboMealImg());
    }

    @Override
    public int getItemCount() {
        return recyclerHomeCombosModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView comboMealImg;
        TextView comboMealName, comboMealDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            comboMealImg = itemView.findViewById(R.id.comboMealImg);
            comboMealName = itemView.findViewById(R.id.comboMealName);
            comboMealDescription = itemView.findViewById(R.id.comboMealDescription);


        }
    }

}
