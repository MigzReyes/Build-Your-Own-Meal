package com.example.buildyourownmeal;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class recyclerViewAdapterCheckout extends RecyclerView.Adapter<recyclerViewAdapterCheckout.MyViewHolder> {

    public interface OnPriceUpdateListener {
        void OnPriceUpdate(int newTotalPrice);
    }

    //DATABASE
    databaseFunctions databaseFunctions;
    private Context context;
    private ArrayList<Bitmap> mealImg;
    private ArrayList<String> mealType, addBtn, minusBtn, editBtn, orderAddonId, addons;
    private ArrayList<Integer> mealTotalPrice, mealQuantity, trashBtn, checkoutItemPerTotalPrice, userOrderId;
    private static int checkoutItemTotalPrice = 0;
    private static recyclerViewAdapterCart.OnPriceUpdateListener OnPriceUpdateListener;

    public static int getMealTotalPrice() {
        return checkoutItemTotalPrice;
    }

    public static void setOnPriceUpdatedListener(recyclerViewAdapterCart.OnPriceUpdateListener listener) {
        OnPriceUpdateListener = listener;
    }

    public void recalculateTotalPriceAndNotify() {
        checkoutItemPerTotalPrice.clear();
        checkoutItemTotalPrice = 0;

        for (int i = 0; i < mealTotalPrice.size(); i++) {
            int itemTotal = mealTotalPrice.get(i) * mealQuantity.get(i);
            checkoutItemPerTotalPrice.add(itemTotal);
            checkoutItemTotalPrice += itemTotal;
        }

        if (OnPriceUpdateListener != null) {
            OnPriceUpdateListener.OnPriceUpdate(checkoutItemTotalPrice);
        }
    }

    public recyclerViewAdapterCheckout(Context context, ArrayList<String> orderAddonId, ArrayList<Integer> userOrderId, ArrayList<Bitmap> mealImg, ArrayList<String> mealType, ArrayList<String> addBtn, ArrayList<String> minusBtn, ArrayList<String> editBtn, ArrayList<Integer> mealTotalPrice, ArrayList<Integer> mealQuantity, ArrayList<Integer> trashBtn) {
        this.context = context;
        this.orderAddonId = orderAddonId;
        this.userOrderId = userOrderId;
        this.mealImg = mealImg;
        this.mealType = mealType;
        this.addBtn = addBtn;
        this.minusBtn = minusBtn;
        this.editBtn = editBtn;
        this.mealTotalPrice = mealTotalPrice;
        this.mealQuantity = mealQuantity;
        this.trashBtn = trashBtn;

        this.checkoutItemPerTotalPrice = new ArrayList<>();

        for (int i = 0; i < recyclerViewAdapterCheckout.this.mealTotalPrice.size(); i++) {
            int itemTotal = recyclerViewAdapterCheckout.this.mealTotalPrice.get(i) * mealQuantity.get(i);
            this.checkoutItemPerTotalPrice.add(itemTotal);
        }

        checkoutItemTotalPrice = 0;
        for (int item : checkoutItemPerTotalPrice) {
            checkoutItemTotalPrice += item;
        }
    }

    @NonNull
    @Override
    public recyclerViewAdapterCheckout.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_checkout, parent, false);
        return new recyclerViewAdapterCheckout.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterCheckout.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.checkoutMealImg.setImageBitmap(mealImg.get(position));
        holder.checkoutMealName.setText(mealType.get(position));
        holder.mealTotalPrice.setText(String.valueOf(mealTotalPrice.get(position)));
        holder.trashBtn.setImageResource(trashBtn.get(position));
        holder.minusBtn.setText(minusBtn.get(position));
        holder.mealQuantity.setText(String.valueOf(mealQuantity.get(position)));
        holder.addBtn.setText(addBtn.get(position));
        holder.editBtn.setText(editBtn.get(position));

        //DATABASE
        databaseFunctions = new databaseFunctions(context);

        int quantity = mealQuantity.get(position);

        addons = new ArrayList<>();

        setUpCheckoutAddon(orderAddonId.get(position));

        recyclerViewAdapterCheckoutAddons recyclerViewAdapterCheckoutAddons = new recyclerViewAdapterCheckoutAddons(context, addons);
        holder.addonsRecycler.setLayoutManager(new LinearLayoutManager(context));
        holder.addonsRecycler.setAdapter(recyclerViewAdapterCheckoutAddons);


        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                SharedPreferences addonGroupIdSP = context.getSharedPreferences("addonGroupId", MODE_PRIVATE);
                SharedPreferences.Editor edit = addonGroupIdSP.edit();

                if (pos != RecyclerView.NO_POSITION && mealType.get(pos).equalsIgnoreCase("Crafted Meal")) {
                    Log.d("Cart", orderAddonId.get(position));
                    String addonId = orderAddonId.get(position);
                    Intent intent = new Intent(context, craftedMeal.class);
                    edit.putString("addonGroupId", addonId);
                    edit.apply();
                    intent.putExtra("addonGroupId", addonId);
                    intent.putExtra("mealTotalPrice", mealTotalPrice.get(position));
                    intent.putExtra("editMeal", true);
                    context.startActivity(intent);
                }
            }
        });

        if (quantity == 1) {
            holder.trashBtn.setVisibility(View.VISIBLE);
            holder.minusBtn.setVisibility(View.GONE);
        } else {
            holder.trashBtn.setVisibility(View.GONE);
            holder.minusBtn.setVisibility(View.VISIBLE);
        }

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    int qty = mealQuantity.get(pos) + 1;
                    mealQuantity.set(pos, qty);

                    int itemPerTotalPrice = mealTotalPrice.get(pos) * qty;
                    Log.d("may error ka", String.valueOf(checkoutItemTotalPrice));

                    checkoutItemPerTotalPrice.set(pos, itemPerTotalPrice);

                    checkoutItemTotalPrice = 0;
                    for (int item : checkoutItemPerTotalPrice) {
                        checkoutItemTotalPrice += item;
                    }

                    if (OnPriceUpdateListener != null) {
                        OnPriceUpdateListener.OnPriceUpdate(checkoutItemTotalPrice);
                    }

                    notifyItemChanged(pos);
                }
            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && mealQuantity.get(pos) > 1) {
                    int qty = mealQuantity.get(pos) - 1;
                    mealQuantity.set(pos, qty);

                    int itemPerTotalPrice = mealTotalPrice.get(pos) * qty;

                    checkoutItemPerTotalPrice.set(pos, itemPerTotalPrice);

                    checkoutItemTotalPrice = 0;
                    for (int item : checkoutItemPerTotalPrice) {
                        checkoutItemTotalPrice += item;
                    }

                    if (OnPriceUpdateListener != null) {
                        OnPriceUpdateListener.OnPriceUpdate(checkoutItemTotalPrice);
                    }

                    notifyItemChanged(pos);
                }
            }
        });

        holder.trashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    boolean deleted = databaseFunctions.deleteUserOrder(userOrderId.get(pos));
                    boolean deleteOrderAddon = databaseFunctions.deleteOrderAddon(orderAddonId.get(pos));
                    if (deleted && deleteOrderAddon) {
                        mealImg.remove(pos);
                        mealTotalPrice.remove(pos);
                        mealType.remove(pos);
                        mealQuantity.remove(pos);
                        addBtn.remove(pos);
                        minusBtn.remove(pos);
                        trashBtn.remove(pos);

                        int removeItem = checkoutItemPerTotalPrice.get(pos);
                        checkoutItemTotalPrice -= removeItem;

                        if (OnPriceUpdateListener != null) {
                            OnPriceUpdateListener.OnPriceUpdate(checkoutItemTotalPrice);
                        }

                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, getItemCount());
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mealImg.size();
    }

    private void setUpCheckoutAddon(String orderAddonId) {
        Cursor getOrderAddonData = databaseFunctions.getOrderAddonName(orderAddonId);

        if (getOrderAddonData != null && getOrderAddonData.moveToFirst()) {
            do {
                addons.add(getOrderAddonData.getString(getOrderAddonData.getColumnIndexOrThrow("addon")));
            } while(getOrderAddonData.moveToNext());
            getOrderAddonData.close();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView checkoutMealImg, trashBtn;
        TextView checkoutMealName, addBtn, minusBtn, mealQuantity, mealTotalPrice, editBtn;
        RecyclerView addonsRecycler;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);

            checkoutMealImg = itemView.findViewById(R.id.checkoutMealImg);
            checkoutMealName = itemView.findViewById(R.id.checkoutMealName);
            mealTotalPrice = itemView.findViewById(R.id.checkoutMealPrice);
            trashBtn = itemView.findViewById(R.id.trashBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            mealQuantity = itemView.findViewById(R.id.quantityValue);
            addBtn = itemView.findViewById(R.id.addBtn);
            editBtn = itemView.findViewById(R.id.editBtn);

            addonsRecycler = itemView.findViewById(R.id.addonsRecycler);
        }
    }
}
