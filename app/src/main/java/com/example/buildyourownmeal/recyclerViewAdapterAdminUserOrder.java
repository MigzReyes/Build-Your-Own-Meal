package com.example.buildyourownmeal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapterAdminUserOrder extends RecyclerView.Adapter<recyclerViewAdapterAdminUserOrder.MyViewHolder> {

    //DATABASE
    private databaseFunctions databaseFunctions;
    private Context context;
    private ArrayList<String> customerName, mealName, orderGroupId, orderAddonId, addonName;
    private ArrayList<Integer> mealQuantity, mealTotalPrice, userId, addonQuantity;
    private ArrayList<Bitmap> mealImg;

    public recyclerViewAdapterAdminUserOrder(Context context, ArrayList<String> customerName, ArrayList<String> mealName, ArrayList<String> orderGroupId, ArrayList<String> orderAddonId, ArrayList<Integer> mealQuantity, ArrayList<Integer> mealTotalPrice, ArrayList<Integer> userId, ArrayList<Bitmap> mealImg) {
        this.context = context;
        this.customerName = customerName;
        this.mealName = mealName;
        this.orderGroupId = orderGroupId;
        this.orderAddonId = orderAddonId;
        this.mealQuantity = mealQuantity;
        this.mealTotalPrice = mealTotalPrice;
        this.userId = userId;
        this.mealImg = mealImg;
    }

    @NonNull
    @Override
    public recyclerViewAdapterAdminUserOrder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.recycler_admin_user_order, parent, false);
        return new recyclerViewAdapterAdminUserOrder.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterAdminUserOrder.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //DATABASE
        databaseFunctions = new databaseFunctions(context);

        setUpAdminOrderUserInfo(userId.get(position));

        holder.mealImg.setImageBitmap(mealImg.get(position));
        holder.customerName.setText(customerName.get(position));
        holder.mealName.setText(mealName.get(position));
        holder.mealQuantity.setText(String.valueOf(mealQuantity.get(position)));
        holder.mealTotalPrice.setText(String.valueOf(mealTotalPrice.get(position)));

        //RECYCLER ADDON
        addonName = new ArrayList<>();
        addonQuantity = new ArrayList<>();

        setUpMealAddon(userId.get(position), orderGroupId.get(position));

        recyclerViewAdapterAdminUserOrderAddon orderAddonAdapter = new recyclerViewAdapterAdminUserOrderAddon(context, addonName, addonQuantity);
        holder.mealAddonRecycler.setLayoutManager(new LinearLayoutManager(context));
        holder.mealAddonRecycler.setAdapter(orderAddonAdapter);

        holder.deleteMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    Dialog popUpAlert1;
                    TextView alertText1;
                    Button cancelBtn, deleteBtn;

                    popUpAlert1 = new Dialog(context);
                    popUpAlert1.setContentView(R.layout.pop_up_delete_addon);
                    popUpAlert1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popUpAlert1.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                    popUpAlert1.setCancelable(true);
                    popUpAlert1.show();

                    alertText1 = popUpAlert1.findViewById(R.id.alertText);
                    alertText1.setText("Are you sure you want to delete this order?");

                    cancelBtn = popUpAlert1.findViewById(R.id.cancelBtn);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUpAlert1.dismiss();
                            Log.d("may error ka", "order group id sa user order: " + orderGroupId.get(position));
                        }
                    });

                    deleteBtn = popUpAlert1.findViewById(R.id.deleteAddonBtn);
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean deleteAdminUserOrderAddon = databaseFunctions.deleteAdminOrderAddon(orderAddonId.get(position));
                            boolean deleteAdminUserOrder = databaseFunctions.deleteAdminUserOrder(orderGroupId.get(position), orderAddonId.get(position));


                            if (deleteAdminUserOrderAddon && deleteAdminUserOrder) {
                                popUpAlert1.dismiss();

                                mealImg.remove(position);
                                customerName.remove(position);
                                mealName.remove(position);
                                mealQuantity.remove(position);
                                mealTotalPrice.remove(position);
                                orderGroupId.remove(position);
                                orderAddonId.remove(position);
                                userId.remove(position);
                                notifyItemRemoved(position);

                                Dialog popUpAlert = new Dialog(context);
                                popUpAlert.setContentView(R.layout.pop_up_alerts);
                                popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                                popUpAlert.setCancelable(true);
                                popUpAlert.show();

                                TextView alertText = popUpAlert.findViewById(R.id.alertText);
                                alertText.setText("Successfully removed order");

                                if (getItemCount() == 0) {
                                    boolean deleteAdminOrder = databaseFunctions.deleteAdminOrder(orderGroupId.get(position));

                                    if (deleteAdminOrder) {
                                        Intent intent = new Intent(context, adminOrders.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("deletedOrder", true);
                                        context.startActivity(intent);
                                    }
                                }

                                int newTotalPrice = 0;
                                for (int price : mealTotalPrice) {
                                    newTotalPrice += price;
                                }

                                Activity activity = (Activity) context;
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("orderGroupId", orderGroupId.get(0));
                                resultIntent.putExtra("newTotalPrice", newTotalPrice);
                                activity.setResult(Activity.RESULT_OK, resultIntent);
                                activity.finish();

                                Button closeBtn = popUpAlert.findViewById(R.id.closeBtn);
                                closeBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popUpAlert.dismiss();
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealImg.size();
    }

    private int recalculateTotalOrderPrice(String orderGroupId) {
        int totalPrice = 0;
        Cursor cursor = databaseFunctions.getAdminUserOrder(orderGroupId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                totalPrice += cursor.getInt(cursor.getColumnIndexOrThrow("orderTotalPrice"));
            } while (cursor.moveToNext());
        }

        return totalPrice;
    }

    private void setUpAdminOrderUserInfo(int userId) {
        Cursor getUserInfo = databaseFunctions.adminGetUserInfo(userId);

        if (getUserInfo != null && getUserInfo.moveToFirst()) {
            do {
                customerName.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("username")));
            } while (getUserInfo.moveToNext());
        }
    }

    private void setUpMealAddon(int userId, String orderGroupId) {
        Cursor getMealAddon = databaseFunctions.getAdminUserOrderAddon(userId, orderGroupId);

        if (getMealAddon != null && getMealAddon.moveToFirst()) {
            do {
                addonName.add(getMealAddon.getString(getMealAddon.getColumnIndexOrThrow("addon")));
                addonQuantity.add(getMealAddon.getInt(getMealAddon.getColumnIndexOrThrow("quantity")));
            } while (getMealAddon.moveToNext());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView customerName, mealName, mealQuantity, mealTotalPrice, deleteMealBtn;
        ImageView mealImg;
        RecyclerView mealAddonRecycler;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mealImg = itemView.findViewById(R.id.mealImg);
            customerName = itemView.findViewById(R.id.customerName);
            mealName = itemView.findViewById(R.id.mealName);
            mealQuantity = itemView.findViewById(R.id.mealQuantity);
            mealTotalPrice = itemView.findViewById(R.id.totalPrice);
            deleteMealBtn = itemView.findViewById(R.id.deleteMealBtn);

            mealAddonRecycler = itemView.findViewById(R.id.mealAddonRecycler);
        }
    }
}
