package com.example.buildyourownmeal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapterAdminMeals extends RecyclerView.Adapter<recyclerViewAdapterAdminMeals.MyViewHolder> {

    //DATABASE
    private databaseFunctions databaseFunctions;

    private Context context;
    private ArrayList<String> mealName, mealDescription, adminAddonId, addonName;
    private ArrayList<Bitmap> mealImg;
    private ArrayList<Integer> mealPrice, addonQuantity, adminMealId;

    public recyclerViewAdapterAdminMeals(Context context,ArrayList<String> adminAddonId,  ArrayList<String> mealName, ArrayList<String> mealDescription, ArrayList<Bitmap> mealImg, ArrayList<Integer> mealPrice, ArrayList<Integer> adminMealId) {
        this.context = context;
        this.adminAddonId = adminAddonId;
        this.mealName = mealName;
        this.mealDescription = mealDescription;
        this.mealImg = mealImg;
        this.mealPrice = mealPrice;
        this.adminMealId = adminMealId;
    }

    @NonNull
    @Override
    public recyclerViewAdapterAdminMeals.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.recycler_admin_meals, parent, false);
        return new recyclerViewAdapterAdminMeals.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterAdminMeals.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mealName.setText(mealName.get(position));
        holder.mealDescription.setText(mealDescription.get(position));
        holder.mealImg.setImageBitmap(mealImg.get(position));
        holder.mealPrice.setText(String.valueOf(mealPrice.get(position)));

        //DATABASE
        databaseFunctions = new databaseFunctions(context);

        addonName = new ArrayList<>();
        addonQuantity = new ArrayList<>();

        setUpAdminMealAddon(adminAddonId.get(position));

        recyclerViewAdapterAdminUserOrderAddon addonAdapter = new recyclerViewAdapterAdminUserOrderAddon(context, addonName, addonQuantity);
        holder.adminMealAddonRecycler.setLayoutManager(new LinearLayoutManager(context));
        holder.adminMealAddonRecycler.setAdapter(addonAdapter);


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    Dialog popUpAlert;
                    Button cancelBtn, deleteBtn;
                    TextView alertText;

                    popUpAlert = new Dialog(context);
                    popUpAlert.setContentView(R.layout.pop_up_delete_addon);
                    popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                    popUpAlert.setCancelable(true);
                    popUpAlert.show();

                    alertText = popUpAlert.findViewById(R.id.alertText);
                    alertText.setText("Are you sure you want to delete this meal?");

                    cancelBtn = popUpAlert.findViewById(R.id.cancelBtn);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUpAlert.dismiss();
                        }
                    });

                    deleteBtn = popUpAlert.findViewById(R.id.deleteAddonBtn);
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean deleteAdminMeal = databaseFunctions.deleteAdminMeal(adminMealId.get(position));
                            boolean deleteAdminMealAddon = databaseFunctions.deleteOrderAddon(adminAddonId.get(position));

                            if (deleteAdminMeal && deleteAdminMealAddon) {
                                popUpAlert.dismiss();

                                mealName.remove(position);
                                mealDescription.remove(position);
                                mealImg.remove(position);
                                mealPrice.remove(position);
                                adminMealId.remove(position);
                                adminAddonId.remove(position);
                                addonName.clear();
                                addonQuantity.clear();
                                notifyItemRemoved(position);

                                Dialog popUpSucess;
                                Button closeBtn;
                                TextView alertText1;

                                popUpSucess = new Dialog(context);
                                popUpSucess.setContentView(R.layout.pop_up_alerts);
                                popUpSucess.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                popUpSucess.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                                popUpSucess.setCancelable(true);
                                popUpSucess.show();

                                alertText1 = popUpSucess.findViewById(R.id.alertText);
                                alertText1.setText("Successfully deleted meal");

                                closeBtn = popUpSucess.findViewById(R.id.closeBtn);
                                closeBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popUpSucess.dismiss();
                                    }
                                });

                            } else {
                                Log.d("may ereor ka", "Failed to delete meal");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealName.size();
    }

    private void setUpAdminMealAddon(String adminAddonId) {
        Cursor getAdminMealAddon = databaseFunctions.getOrderAddon(adminAddonId);

        if (getAdminMealAddon != null && getAdminMealAddon.moveToFirst()) {
            do {
                addonName.add(getAdminMealAddon.getString(getAdminMealAddon.getColumnIndexOrThrow("addon")));
                addonQuantity.add(getAdminMealAddon.getInt(getAdminMealAddon.getColumnIndexOrThrow("quantity")));
            } while (getAdminMealAddon.moveToNext());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mealName, mealDescription, mealPrice, editBtn, deleteBtn;
        ImageView mealImg;
        RecyclerView adminMealAddonRecycler;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mealName = itemView.findViewById(R.id.mealName);
            mealDescription = itemView.findViewById(R.id.mealDescription);
            mealPrice = itemView.findViewById(R.id.mealPrice);
            mealImg = itemView.findViewById(R.id.mealImg);
            editBtn = itemView.findViewById(R.id.editMealBtn);
            deleteBtn = itemView.findViewById(R.id.deleteMealBtn);

            adminMealAddonRecycler = itemView.findViewById(R.id.adminMealAddonRecycler);
        }
    }
}
