package com.example.buildyourownmeal;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class recyclerViewAdapterMenuCombos extends RecyclerView.Adapter<recyclerViewAdapterMenuCombos.MyViewHolder> {
    private Context context;
    private ArrayList<recyclerMenuCombosModel> recyclerMenuCombosModels;


    public recyclerViewAdapterMenuCombos(Context context, ArrayList<recyclerMenuCombosModel> recyclerMenuCombosModels) {
        this.context = context;
        this.recyclerMenuCombosModels = recyclerMenuCombosModels;
    }

    @NonNull
    @Override
    public recyclerViewAdapterMenuCombos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_menu_combos, parent, false);
        return new recyclerViewAdapterMenuCombos.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterMenuCombos.MyViewHolder holder, int position) {
        holder.comboMealName.setText(recyclerMenuCombosModels.get(position).getComboMealName());
        holder.comboMealDescription.setText(recyclerMenuCombosModels.get(position).getComboMealDescription());
        holder.comboMealImg.setImageResource(recyclerMenuCombosModels.get(position).getComboMealImg());
    }

    @Override
    public int getItemCount() {
        return recyclerMenuCombosModels.size();
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
