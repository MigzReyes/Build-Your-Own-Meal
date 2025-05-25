package com.example.buildyourownmeal;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ArrayList<String> customerName, customerEmail, customerNumber, orderDate, orderStatus, orderGroupId;
    private ArrayList<Integer> orderTotalPrice, orderCount, userId;


    public recyclerViewAdapterAdminOrders(Context context, ArrayList<Integer> userId, ArrayList<String> orderGroupId, ArrayList<String> customerName, ArrayList<String> customerEmail, ArrayList<String> customerNumber, ArrayList<String> orderDate, ArrayList<String> orderStatus, ArrayList<Integer> orderTotalPrice, ArrayList<Integer> orderCount) {
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

                            boolean updateAdminOrderStatus = databaseFunctions.updateAdminOrderStatus(userId.get(position), orderGroupId.get(position), getStatus);
                            if (updateAdminOrderStatus) {
                                orderStatus.set(position, getStatus);
                                holder.customerStatus.setText(orderStatus.get(position));
                                notifyItemChanged(position);
                                popUpAlert.dismiss();
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

    public void setUpAdminOrderUserInfo(int userId) {
        Cursor getUserInfo = databaseFunctions.adminGetUserInfo(userId);

        if (getUserInfo != null && getUserInfo.moveToFirst()) {
            do {
                customerName.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("username")));
                customerEmail.add(getUserInfo.getString(getUserInfo.getColumnIndexOrThrow("email")));
            } while (getUserInfo.moveToNext());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderId, customerName, customerContact, customerEmail, customerDate, customerTotalPrice, customerStatus, setStatusBtn;
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
        }
    }
}
