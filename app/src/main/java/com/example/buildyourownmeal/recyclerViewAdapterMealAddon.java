package com.example.buildyourownmeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class recyclerViewAdapterMealAddon extends RecyclerView.Adapter<recyclerViewAdapterMealAddon.MyViewHolder> {

    public interface OnPriceUpdateListener {
        void onPriceUpdated(int newTotalPrice);
    }

    //DATABASE
    private databaseFunctions databaseFunctions;
    public static HashMap<String, Integer> hashAddonQuantity = new HashMap<String, Integer>();
    public static HashMap<String, Integer> hashAddonPrice = new HashMap<String, Integer>();
    public static HashMap<String, Integer> hashAddonPerTotalPrice = new HashMap<String, Integer>();
    private Context context;
    private int userId;
    private ArrayList<String> addonName, minusBtnAddon, addBtnAddon, addonCategory;
    private ArrayList<Bitmap> addonImg;
    private ArrayList<Integer> addonPrice, addonQuantity, addonId;
    private int addAddonPrice = 0;
    private int minusAddonPrice = 0;
    public static int mealTotalPrice = 0;
    private static OnPriceUpdateListener OnPriceUpdatedListener;

    public static int getMealTotalPrice() {
        return mealTotalPrice;
    }

    public static void setOnPriceUpdatedListener(OnPriceUpdateListener listener) {
        OnPriceUpdatedListener = listener;
    }

    public recyclerViewAdapterMealAddon(Context context, int userId, ArrayList<String> addonName, ArrayList<String> minusBtnAddon, ArrayList<String> addBtnAddon, ArrayList<String> addonCategory, ArrayList<Bitmap> addonImg, ArrayList<Integer> addonPrice, ArrayList<Integer> addonQuantity, ArrayList<Integer> addonId) {
        this.context = context;
        this.userId = userId;
        this.addonName = addonName;
        this.minusBtnAddon = minusBtnAddon;
        this.addBtnAddon = addBtnAddon;
        this.addonCategory = addonCategory;
        this.addonImg = addonImg;
        this.addonPrice = addonPrice;
        this.addonQuantity = addonQuantity;
        this.addonId = addonId;
    }

    @NonNull
    @Override
    public recyclerViewAdapterMealAddon.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_meal_addons, parent, false);
        return new recyclerViewAdapterMealAddon.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterMealAddon.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.addonName.setText(addonName.get(position));
        holder.addonImg.setImageBitmap(addonImg.get(position));
        holder.addonPrice.setText(String.valueOf(addonPrice.get(position)));
        holder.minusBtnAddon.setText(minusBtnAddon.get(position));
        holder.addBtnAddon.setText(addBtnAddon.get(position));

        int[] quantityValue = {0};

        holder.minusBtnAddon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                    if (quantityValue[0] > 0) {
                        quantityValue[0]--;
                    }

                //DISPLAY THE QUANTITY VALUE IN THE LAYOUT
                holder.addonQuantity.setText(String.valueOf(quantityValue[0]));

                hashAddonQuantity.put(addonName.get(position), Integer.parseInt(String.valueOf(quantityValue[0])));

                //SUBTRACTION OF TOTAL PRICE TO THE ADDON PRICE
                if (quantityValue[0] > 0) {
                    minusAddonPrice = hashAddonPerTotalPrice.get(addonName.get(position)) - hashAddonPrice.get(addonName.get(position));
                    hashAddonPerTotalPrice.put(addonName.get(position), minusAddonPrice);
                }

                //REMOVE THE ITEM FROM THE HASHMAP
                if (quantityValue[0] == 0) {
                    hashAddonQuantity.remove(addonName.get(position));
                    hashAddonPrice.remove(addonName.get(position));
                    hashAddonPerTotalPrice.remove(addonName.get(position));
                    mealTotalPrice = 0;
                }

                //TOTAL PRICE OF ALL ADDON
                int result = 0;
                for (int value : hashAddonPerTotalPrice.values()) {
                      result -= value;

                      mealTotalPrice = Math.abs(result);
                }

                if (OnPriceUpdatedListener != null) {
                    OnPriceUpdatedListener.onPriceUpdated(mealTotalPrice);
                }
                Log.d("AddonMinusTotalPriceForloop", "total price: " + String.valueOf(mealTotalPrice));


                //DISPLAY PER ADDON QUANTITY
                for (Map.Entry<String, Integer> item : hashAddonQuantity.entrySet()) {
                    Log.d("AddonHashMapRemove", "key: " + item.getKey() + " Quantity: " + item.getValue());
                }

                //DISPLAY PER ADDON PRICE
                for (Map.Entry<String, Integer> item : hashAddonPrice.entrySet()) {
                    Log.d("AddonHashMapPrice", "key: " + item.getKey() + " price: " + item.getValue());
                }

                //DISPLAY PER ADDON TOTAL PRICE
                for (Map.Entry<String, Integer> item : hashAddonPerTotalPrice.entrySet()) {
                    Log.d("AddonHashTotalPrice", "Addon: " + item.getKey() + " totalPrice: " + item.getValue());
                }
            }
        });

        holder.addBtnAddon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    quantityValue[0]++;

                    //DISPLAY THE QUANTITY VALUE IN THE LAYOUT
                    holder.addonQuantity.setText(String.valueOf(quantityValue[0]));

                    if (quantityValue[0] > 0) {
                        hashAddonQuantity.put(addonName.get(position), Integer.parseInt(String.valueOf(quantityValue[0])));
                        hashAddonPrice.put(addonName.get(position), addonPrice.get(position));

                        // MULTIPLICATION OF PER ADDON PRICE
                        if (hashAddonQuantity.get(addonName.get(position)) > 0) {
                            int getAddonPrice = hashAddonPrice.get(addonName.get(position));
                            addAddonPrice = getAddonPrice * hashAddonQuantity.get(addonName.get(position));
                        }
                        hashAddonPerTotalPrice.put(addonName.get(position), addAddonPrice);

                        //TOTAL PRICE OF ALL ADDONS
                        mealTotalPrice = hashAddonPerTotalPrice.values().stream().mapToInt(integer -> integer).sum();
                        Log.d("AddonHashMap", "total price: " + String.valueOf(mealTotalPrice));

                        if (OnPriceUpdatedListener != null) {
                            OnPriceUpdatedListener.onPriceUpdated(mealTotalPrice);
                        }


                        //DISPLAY PER ADDON QUANTITY
                        for (Map.Entry<String, Integer> item : hashAddonQuantity.entrySet()) {
                            Log.d("AddonHashMap", "key: " + item.getKey() + " Quantity: " + item.getValue());
                        }

                        //DISPLAY PER ADDON PRICE
                        for (Map.Entry<String, Integer> item : hashAddonPrice.entrySet()) {
                            Log.d("AddonHashMapPrice", "key: " + item.getKey() + " price: " + item.getValue());
                        }

                        //DISPLAY PER ADDON TOTAL PRICE
                        for (Map.Entry<String, Integer> item : hashAddonPerTotalPrice.entrySet()) {
                            Log.d("AddonHashTotalPrice", "Addon: " + item.getKey() + " totalPrice: " + item.getValue());
                        }
                    }

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return addonName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView addonName, addonPrice, minusBtnAddon, addBtnAddon, addonQuantity;
        ImageView addonImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            addonName = itemView.findViewById(R.id.addonName);
            addonPrice = itemView.findViewById(R.id.addonPrice);
            minusBtnAddon = itemView.findViewById(R.id.minusBtnAddon);
            addBtnAddon = itemView.findViewById(R.id.addBtnAddon);
            addonQuantity = itemView.findViewById(R.id.quantityValueAddon);
            addonImg = itemView.findViewById(R.id.addonImg);

        }
    }
}
