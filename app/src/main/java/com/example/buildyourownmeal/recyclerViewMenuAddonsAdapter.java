package com.example.buildyourownmeal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewMenuAddonsAdapter extends RecyclerView.Adapter<recyclerViewMenuAddonsAdapter.MyViewHolder> {
    
    //DATABASE
    databaseFunctions databaseFunctions;

    private Context context;
    private ArrayList<Bitmap> addonImg;
    private ArrayList<String> addonName, addonEdit, addonDelete, addonCategory, addonUri;
    private ArrayList<Integer> addonPrice, addonId;

    public recyclerViewMenuAddonsAdapter(Context context, ArrayList<Bitmap> addonImg, ArrayList<String> addonName, ArrayList<String> addonEdit, ArrayList<String> addonDelete, ArrayList<String> addonCategory, ArrayList<String> addonUri, ArrayList<Integer> addonPrice, ArrayList<Integer> addonId) {
        this.context = context;
        this.addonImg = addonImg;
        this.addonName = addonName;
        this.addonEdit = addonEdit;
        this.addonDelete = addonDelete;
        this.addonCategory = addonCategory;
        this.addonUri = addonUri;
        this.addonPrice = addonPrice;
        this.addonId = addonId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_menu_addons, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.addonImg.setImageBitmap(addonImg.get(position));
        holder.addonName.setText(addonName.get(position));
        holder.addonPrice.setText(String.valueOf(addonPrice.get(position)));
        holder.addonEdit.setText(addonEdit.get(position));
        holder.addonDelete.setText(addonDelete.get(position));

        holder.addonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, adminEditMenuAddon.class);

                    intent.putExtra("addonImg", addonImg.get(position));
                    intent.putExtra("addonName", addonName.get(position));
                    intent.putExtra("addonPrice", addonPrice.get(position));
                    intent.putExtra("addonEdit", addonEdit.get(position));
                    intent.putExtra("addonDelete", addonDelete.get(position));
                    intent.putExtra("addonId", addonId.get(position));
                    intent.putExtra("addonCategory", addonCategory.get(position));
                    intent.putExtra("addonUri", addonUri.get(position));

                    context.startActivity(intent);
                }
            }
        });
        
        holder.addonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int getPosition = holder.getAdapterPosition();
                
                if (getPosition != RecyclerView.NO_POSITION) {
                    databaseFunctions = new databaseFunctions(context);
                    Dialog popUpAlert;
                    Button cancelBtn, deleteAddonBtn;
                    
                    popUpAlert = new Dialog(context);
                    popUpAlert.setContentView(R.layout.pop_up_delete_addon);
                    popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                    popUpAlert.setCancelable(true);
                    popUpAlert.show();
                    
                    cancelBtn = popUpAlert.findViewById(R.id.cancelBtn);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUpAlert.dismiss();
                        }
                    });
                    
                    deleteAddonBtn = popUpAlert.findViewById(R.id.deleteAddonBtn);
                    deleteAddonBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String getCategory = addonCategory.get(position);
                            String addonTable = "";
                            String addonIdColName = "";
                            switch (getCategory) {
                                case "rice":
                                    addonTable = "rice";
                                    addonIdColName = "riceId";
                                    
                                    break;
                                case "main dish":
                                    addonTable = "main_dish";
                                    addonIdColName = "mainDishId";
                                    
                                    break;
                                case "sides":
                                    addonTable = "side_dish";
                                    addonIdColName = "sideDishId";
                                    
                                    break;
                                case "sauces":
                                    addonTable = "sauce";
                                    addonIdColName = "sauceId";
                                    
                                    break;
                                case "desserts":
                                    addonTable = "dessert";
                                    addonIdColName = "dessertId";
                                    
                                    break;
                                case "drinks":
                                    addonTable = "drink";
                                    addonIdColName = "drinkId";
                                    
                                    break;
                            }
                            
                            boolean deleteAddon = databaseFunctions.deleteAddon(addonTable, addonIdColName, addonId.get(position), addonCategory.get(position));

                            if (deleteAddon) {
                                Intent intent = new Intent(context, adminMenu.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                        }
                    });
                    
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addonImg.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView addonImg;
        TextView addonName, addonPrice, addonEdit, addonDelete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            addonImg = itemView.findViewById(R.id.addonImg);
            addonName = itemView.findViewById(R.id.addonName);
            addonPrice = itemView.findViewById(R.id.addonPrice);
            addonEdit = itemView.findViewById(R.id.addonEdit);
            addonDelete = itemView.findViewById(R.id.addonDelete);
        }
    }
}
