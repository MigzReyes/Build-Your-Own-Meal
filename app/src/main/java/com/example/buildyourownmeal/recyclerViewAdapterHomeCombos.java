package com.example.buildyourownmeal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class recyclerViewAdapterHomeCombos extends RecyclerView.Adapter<recyclerViewAdapterHomeCombos.MyViewHolder> {

    private Context context;
    private ArrayList<String> comboMealName, comboMealDescription, addonGroupId;
    private ArrayList<Bitmap> comboMealImg;
    private ArrayList<Integer> comboMealId;

    public recyclerViewAdapterHomeCombos(Context context, ArrayList<String> comboMealName, ArrayList<String> comboMealDescription, ArrayList<String> addonGroupId, ArrayList<Bitmap> comboMealImg, ArrayList<Integer> comboMealId) {
        this.context = context;
        this.comboMealName = comboMealName;
        this.comboMealDescription = comboMealDescription;
        this.addonGroupId = addonGroupId;
        this.comboMealImg = comboMealImg;
        this.comboMealId = comboMealId;
    }


    @NonNull
    @Override
    public recyclerViewAdapterHomeCombos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_home_combos, parent, false);
        return new recyclerViewAdapterHomeCombos.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterHomeCombos.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.comboMealName.setText(comboMealName.get(position));
        holder.comboMealDescription.setText(comboMealDescription.get(position));
        holder.comboMealImg.setImageBitmap(comboMealImg.get(position));

        holder.comboMealLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, preMadeMeal.class);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap getMealImg = comboMealImg.get(position);
                    getMealImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("comboMealImg", byteArray);
                    intent.putExtra("comboMealName", comboMealName.get(position));
                    intent.putExtra("comboMealDescription", comboMealDescription.get(position));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comboMealName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView comboMealImg;
        TextView comboMealName, comboMealDescription;
        LinearLayout comboMealLink;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            comboMealImg = itemView.findViewById(R.id.comboMealImg);
            comboMealName = itemView.findViewById(R.id.comboMealName);
            comboMealDescription = itemView.findViewById(R.id.comboMealDescription);

            comboMealLink = itemView.findViewById(R.id.comboMealLink);
        }
    }

}
