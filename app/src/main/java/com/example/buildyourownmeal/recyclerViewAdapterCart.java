package com.example.buildyourownmeal;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class recyclerViewAdapterCart extends RecyclerView.Adapter<recyclerViewAdapterCart.MyViewHolder> {

    public interface OnPriceUpdateListener {
        void OnPriceUpdate(int newTotalPrice);
    }

    private databaseFunctions databaseFunctions;
    private Context context;
    private  ArrayList<String> addonGroupId;
    private ArrayList<String> mealType, cartItemName, minusBtn, addBtn;
    private ArrayList<Integer> cartItemPrice, mealQuantity, trashBtn, userOrderId, cartItemPerTotalPrice;
    private ArrayList<Bitmap> cartItemImg;
    private static int cartItemTotalPrice = 0;
    private static OnPriceUpdateListener OnPriceUpdateListener;

    public static int getMealTotalPrice() {
        return cartItemTotalPrice;
    }

    public static void setOnPriceUpdatedListener(OnPriceUpdateListener listener) {
        OnPriceUpdateListener = listener;
    }

    public void recalculateTotalPriceAndNotify() {
        cartItemPerTotalPrice.clear();
        cartItemTotalPrice = 0;

        for (int i = 0; i < cartItemPrice.size(); i++) {
            int itemTotal = cartItemPrice.get(i) * mealQuantity.get(i);
            cartItemPerTotalPrice.add(itemTotal);
            cartItemTotalPrice += itemTotal;
        }

        if (OnPriceUpdateListener != null) {
            OnPriceUpdateListener.OnPriceUpdate(cartItemTotalPrice);
        }
    }


    public recyclerViewAdapterCart(Context context, ArrayList<String> addonGroupId, ArrayList<String> mealType, ArrayList<String> cartItemName, ArrayList<String> minusBtn, ArrayList<String> addBtn, ArrayList<Integer> cartItemPrice, ArrayList<Integer> mealQuantity, ArrayList<Integer> trashBtn, ArrayList<Integer> userOrderId, ArrayList<Bitmap> cartItemImg) {
        this.context = context;
        this.addonGroupId = addonGroupId;
        this.mealType = mealType;
        this.cartItemName = cartItemName;
        this.minusBtn = minusBtn;
        this.addBtn = addBtn;
        this.cartItemPrice = cartItemPrice;
        this.mealQuantity = mealQuantity;
        this.trashBtn = trashBtn;
        this.userOrderId = userOrderId;
        this.cartItemImg = cartItemImg;

        this.cartItemPerTotalPrice = new ArrayList<>();

        for (int i = 0; i < cartItemPrice.size(); i++) {
            int itemTotal = cartItemPrice.get(i) * mealQuantity.get(i);
            this.cartItemPerTotalPrice.add(itemTotal);
        }

        cartItemTotalPrice = 0;
        for (int item : cartItemPerTotalPrice) {
            cartItemTotalPrice += item;
        }
    }

    @NonNull
    @Override
    public recyclerViewAdapterCart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_cart, parent, false);
        return new recyclerViewAdapterCart.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterCart.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.cartItemName.setText(cartItemName.get(position));
        holder.cartItemPrice.setText(String.valueOf(cartItemPrice.get(position)));
        holder.cartItemImg.setImageBitmap(cartItemImg.get(position));
        holder.quantityValue.setText(String.valueOf(mealQuantity.get(position)));
        holder.addBtn.setText(addBtn.get(position));
        holder.minusBtn.setText(minusBtn.get(position));
        holder.trashBtn.setImageResource(trashBtn.get(position));

        databaseFunctions = new databaseFunctions(context);

        int quantity = mealQuantity.get(position);

        holder.cartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                SharedPreferences addonGroupIdSP = context.getSharedPreferences("addonGroupId", MODE_PRIVATE);
                SharedPreferences.Editor edit = addonGroupIdSP.edit();

                if (pos != RecyclerView.NO_POSITION && mealType.get(pos).equalsIgnoreCase("Crafted Meal")) {
                    Log.d("Cart", addonGroupId.get(position));
                    String addonId = addonGroupId.get(position);
                    Intent intent = new Intent(context, craftedMeal.class);
                    edit.putString("addonGroupId", addonId);
                    edit.apply();
                    intent.putExtra("addonGroupId", addonId);
                    intent.putExtra("mealTotalPrice", cartItemPrice.get(position));
                    intent.putExtra("editMeal", true);
                    context.startActivity(intent);
                }
            }
        });

        //ADD AND MINUS BUTTON
        if (quantity == 1) {
            holder.minusBtn.setVisibility(View.GONE);
            holder.trashBtn.setVisibility(View.VISIBLE);
        } else {
            holder.minusBtn.setVisibility(View.VISIBLE);
            holder.trashBtn.setVisibility(View.GONE);
        }

        // Add click listener
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    int qty = mealQuantity.get(pos) + 1;
                    mealQuantity.set(pos, qty);

                    int itemPerTotalPrice = cartItemPrice.get(pos) * qty;
                    Log.d("may error ka", String.valueOf(cartItemTotalPrice));

                    cartItemPerTotalPrice.set(pos, itemPerTotalPrice);

                    cartItemTotalPrice = 0;
                    for (int item : cartItemPerTotalPrice) {
                        cartItemTotalPrice += item;
                    }

                    if (OnPriceUpdateListener != null) {
                        OnPriceUpdateListener.OnPriceUpdate(cartItemTotalPrice);
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

                    int itemPerTotalPrice = cartItemPrice.get(pos) * qty;

                    cartItemPerTotalPrice.set(pos, itemPerTotalPrice);

                    cartItemTotalPrice = 0;
                    for (int item : cartItemPerTotalPrice) {
                        cartItemTotalPrice += item;
                    }

                    if (OnPriceUpdateListener != null) {
                        OnPriceUpdateListener.OnPriceUpdate(cartItemTotalPrice);
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
                    boolean deleteOrderAddon = databaseFunctions.deleteOrderAddon(addonGroupId.get(pos));
                    if (deleted && deleteOrderAddon) {
                        cartItemName.remove(pos);
                        cartItemImg.remove(pos);
                        cartItemPrice.remove(pos);
                        mealType.remove(pos);
                        mealQuantity.remove(pos);
                        addonGroupId.remove(pos);
                        userOrderId.remove(pos);
                        addBtn.remove(pos);
                        minusBtn.remove(pos);
                        trashBtn.remove(pos);

                        int removeItem = cartItemPerTotalPrice.get(pos);
                        cartItemTotalPrice -= removeItem;

                        if (OnPriceUpdateListener != null) {
                            OnPriceUpdateListener.OnPriceUpdate(cartItemTotalPrice);
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
        return cartItemName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cartItemImg, trashBtn;
        TextView cartItemName, cartItemPrice, minusBtn, addBtn, quantityValue;
        LinearLayout cartItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cartItem = itemView.findViewById(R.id.cartItem);
            cartItemImg = itemView.findViewById(R.id.cartItemImg);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            trashBtn = itemView.findViewById(R.id.trashBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            quantityValue = itemView.findViewById(R.id.quantityValue);
            addBtn = itemView.findViewById(R.id.addBtn);
        }
    }
}
