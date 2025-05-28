package com.example.buildyourownmeal;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class recyclerViewAdapterAdminOrders extends RecyclerView.Adapter<recyclerViewAdapterAdminOrders.MyViewHolder> {

    //DATABASE
    private databaseFunctions databaseFunctions;

    private Context context;
    private ArrayList<String> customerName, customerEmail, customerNumber, orderDate, orderStatus, orderGroupId, pickUp, paymentMethod;
    private ArrayList<Integer> orderTotalPrice, orderCount, userId;


    public recyclerViewAdapterAdminOrders(Context context, ArrayList<Integer> userId, ArrayList<String> orderGroupId, ArrayList<String> customerName, ArrayList<String> customerEmail, ArrayList<String> customerNumber, ArrayList<String> orderDate, ArrayList<String> orderStatus, ArrayList<Integer> orderTotalPrice, ArrayList<Integer> orderCount, ArrayList<String> pickUp, ArrayList<String> paymentMethod) {
        this.context = context;
        this.userId = userId;
        this.orderGroupId = orderGroupId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerNumber = customerNumber;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderTotalPrice = orderTotalPrice;
        this.orderCount = orderCount;
        this.pickUp = pickUp;
        this.paymentMethod = paymentMethod;
    }


    @NonNull
    @Override
    public recyclerViewAdapterAdminOrders.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.recycler_admin_order, parent, false);
        return new recyclerViewAdapterAdminOrders.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterAdminOrders.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //DATABASE
        databaseFunctions = new databaseFunctions(context);

        setUpAdminOrderUserInfo(userId.get(position));

        holder.orderId.setText(String.valueOf(orderCount.get(position)));
        holder.customerName.setText(customerName.get(position));
        holder.customerContact.setText(customerNumber.get(position));
        holder.customerEmail.setText(customerEmail.get(position));
        holder.customerDate.setText(orderDate.get(position));
        holder.pickUp.setText(pickUp.get(position));
        holder.paymentMethod.setText(paymentMethod.get(position));
        holder.customerTotalPrice.setText(String.valueOf(orderTotalPrice.get(position)));
        holder.customerStatus.setText(orderStatus.get(position));

        holder.setStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    Dialog popUpAlert;
                    Button cancelBtn, saveBtn;
                    Spinner setStatusSpinner;

                    popUpAlert = new Dialog(context);
                    popUpAlert.setContentView(R.layout.pop_up_set_status);
                    popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                    popUpAlert.setCancelable(true);
                    popUpAlert.show();

                    setStatusSpinner = popUpAlert.findViewById(R.id.setStatusSpinner);
                    List<String> statusDropdown = Arrays.asList("Processing", "Meal in progress", "Order is ready", "Completed");
                    dropdownAdapter statusAdapter = new dropdownAdapter(context, R.layout.custom_spinner_bg, statusDropdown);
                    statusAdapter.setDropDownViewResource(R.layout.custom_dropdown_bg);
                    setStatusSpinner.setAdapter(statusAdapter);

                    cancelBtn = popUpAlert.findViewById(R.id.cancelBtn);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUpAlert.dismiss();
                        }
                    });

                    saveBtn = popUpAlert.findViewById(R.id.saveBtn);
                    saveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String getStatus = setStatusSpinner.getSelectedItem().toString().trim();


                            if (getStatus.equals("Completed")) {
                                Dialog popUpAlert1;
                                Button noBtn, yesBtn;
                                TextView alertText;

                                popUpAlert1 = new Dialog(context);
                                popUpAlert1.setContentView(R.layout.pop_up_order_confirmation);
                                popUpAlert1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                popUpAlert1.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                                popUpAlert1.setCancelable(true);
                                popUpAlert1.show();

                                alertText = popUpAlert1.findViewById(R.id.alertText);
                                alertText.setText("Are you sure you want to set this order status to Completed?");

                                noBtn = popUpAlert1.findViewById(R.id.cancelBtn);
                                noBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popUpAlert1.dismiss();
                                    }
                                });

                                yesBtn = popUpAlert1.findViewById(R.id.proceedBtn);
                                yesBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Cursor getUserOrder = databaseFunctions.getAdminUserOrderHistory(userId.get(position), orderGroupId.get(position));

                                        if (getUserOrder != null && getUserOrder.moveToFirst()) {
                                            do {
                                                String getOrderAddonId = getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("orderAddonId"));
                                                int getUserId = getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("userId"));
                                                byte[] byteArray = getUserOrder.getBlob(getUserOrder.getColumnIndexOrThrow("mealImg"));
                                                Bitmap getMealImg = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                                String getMealType = getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("mealType"));
                                                int getMealQuantity = getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("mealQuantity"));
                                                int getOrderTotalPrice = getUserOrder.getInt(getUserOrder.getColumnIndexOrThrow("orderTotalPrice"));
                                                String getOrderDate = getUserOrder.getString(getUserOrder.getColumnIndexOrThrow("orderedDate"));
                                                databaseFunctions.insertOrderHistory(getOrderAddonId, orderGroupId.get(position), getUserId, getMealImg, getMealType, getMealQuantity, getOrderTotalPrice, getOrderDate);
                                            } while (getUserOrder.moveToNext());
                                        }

                                        Cursor getUserOrderInfo = databaseFunctions.getAdminOrder(orderGroupId.get(position));

                                        if (getUserOrderInfo != null && getUserOrderInfo.moveToFirst()) {
                                            do {
                                                String pickUp = getUserOrderInfo.getString(getUserOrderInfo.getColumnIndexOrThrow("pickUp"));
                                                String paymentMethod = getUserOrderInfo.getString(getUserOrderInfo.getColumnIndexOrThrow("paymentMethod"));
                                                String orderedDate = getUserOrderInfo.getString(getUserOrderInfo.getColumnIndexOrThrow("orderedDate"));
                                                databaseFunctions.updateOrderHistoryInfo(userId.get(position), orderGroupId.get(position), pickUp, paymentMethod, orderedDate);
                                            } while (getUserOrderInfo.moveToNext());
                                        } else {
                                            Log.d("may error ka", "Admin order info insert into order history failed");
                                        }

                                        Log.d("may error ka", "user id: " + String.valueOf(userId.get(position)) + " addon group id: " + orderGroupId.get(position));
                                        Cursor getAddonData = databaseFunctions.getAdminUserOrderAddon(userId.get(position), orderGroupId.get(position));

                                        if (getAddonData != null && getAddonData.moveToFirst()) {
                                            do {
                                                int getUserId = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("userId"));
                                                String getAddonGroupId = getAddonData.getString(getAddonData.getColumnIndexOrThrow("addonGroupId"));
                                                String getAddon = getAddonData.getString(getAddonData.getColumnIndexOrThrow("addon"));
                                                int getQuantity = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("quantity"));
                                                int getPrice = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("price"));
                                                String getOrderedDateDb = getAddonData.getString(getAddonData.getColumnIndexOrThrow("orderedDate"));

                                                databaseFunctions.insertOrderAddonHistory(getUserId, getAddonGroupId, orderGroupId.get(position), getAddon, getQuantity, getPrice, getOrderedDateDb);
                                            } while (getAddonData.moveToNext());
                                            getAddonData.close();
                                        } else {
                                            Log.d("may error ka", "Addon transfer to order addon history failed");
                                        }

                                        boolean deleteAdminOrder = databaseFunctions.deleteAdminOrder(orderGroupId.get(position));

                                        if (deleteAdminOrder) {
                                            customerName.remove(position);
                                            customerEmail.remove(position);
                                            customerNumber.remove(position);
                                            orderDate.remove(position);
                                            orderStatus.remove(position);
                                            orderGroupId.remove(position);
                                            pickUp.remove(position);
                                            paymentMethod.remove(position);
                                            orderTotalPrice.remove(position);
                                            orderCount.remove(position);
                                            userId.remove(position);
                                            notifyItemRemoved(position);
                                            popUpAlert1.dismiss();
                                            popUpAlert.dismiss();
                                        }
                                    }
                                });
                            } else {
                                boolean updateAdminOrderStatus = databaseFunctions.updateAdminOrderStatus(userId.get(position), orderGroupId.get(position), getStatus);
                                if (updateAdminOrderStatus) {
                                    orderStatus.set(position, getStatus);
                                    holder.customerStatus.setText(orderStatus.get(position));
                                    notifyItemChanged(position);
                                    popUpAlert.dismiss();
                                }
                            }
                        }
                    });
                }
            }
        });

        holder.seeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, adminUserOrdersList.class);
                    String getOrderGroupId = orderGroupId.get(position);
                    intent.putExtra("orderGroupId", getOrderGroupId);
                    intent.putExtra("userId", userId.get(position));
                    ((Activity) context).startActivityForResult(intent, 1001);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return orderCount.size();
    }

    private void setUpAdminOrderUserInfo(int userId) {
        Cursor getUserInfo = databaseFunctions.adminGetUserInfo(userId);

        if (getUserInfo != null && getUserInfo.moveToFirst()) {
            do {
                customerName.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("username")));
                customerEmail.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("email")));
            } while (getUserInfo.moveToNext());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderId, customerName, customerContact, customerEmail, customerDate, customerTotalPrice, customerStatus, setStatusBtn, pickUp, paymentMethod;
        Button seeOrderBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.orderId);
            customerName = itemView.findViewById(R.id.customerName);
            customerContact = itemView.findViewById(R.id.customerContact);
            customerEmail = itemView.findViewById(R.id.customerEmail);
            customerDate = itemView.findViewById(R.id.customerDate);
            customerTotalPrice = itemView.findViewById(R.id.customerTotalPrice);
            customerStatus = itemView.findViewById(R.id.customerStatus);
            setStatusBtn = itemView.findViewById(R.id.setStatusBtn);
            seeOrderBtn = itemView.findViewById(R.id.seeOrderBtn);
            pickUp = itemView.findViewById(R.id.pickUp);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
        }
    }
}
