package com.example.buildyourownmeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class recyclerViewAdapterAdminAddAddonMeals extends RecyclerView.Adapter<recyclerViewAdapterAdminAddAddonMeals.MyViewHolder> {

    private Context context;
    private ArrayList<String> addonName;
    public static ArrayList<String> addonNameStatic;
    private ArrayList<String> addonNameSpinner;
    private ArrayList<Integer> addonQuantity, addonPrice, totalPrices, selectedIndexOnSpinner;
    public static ArrayList<Integer> addonQuantityStatic, totalPricesStatic, selectedIndexOnSpinnerStatic, addonPriceStatic;

    public recyclerViewAdapterAdminAddAddonMeals(Context context, ArrayList<String> addonName, ArrayList<String> addonNameSpinner, ArrayList<Integer> addonQuantity, ArrayList<Integer> addonPrice) {
        this.context = context;
        this.addonName = addonName;
        this.addonNameSpinner = addonNameSpinner;
        this.addonQuantity = addonQuantity;
        this.addonPrice = addonPrice;

        selectedIndexOnSpinner = new ArrayList<>();
        for (int i = 0; i < addonName.size(); i++) {
            selectedIndexOnSpinner.add(0);
        }

    }

    public void syncStaticList() {
        addonNameStatic = new ArrayList<>(addonName);
        addonQuantityStatic = new ArrayList<>(addonQuantity);
        addonPriceStatic = new ArrayList<>(addonPrice);
        selectedIndexOnSpinnerStatic = new ArrayList<>(selectedIndexOnSpinner);
        totalPricesStatic = getEachAddonTotalPrice();
    }

    public ArrayList<Integer> getEachAddonTotalPrice() {
        ArrayList<Integer> totalPrices = new ArrayList<>();
        for (int i = 0; i < addonQuantity.size(); i++) {
            int selectedIndex = selectedIndexOnSpinner.get(i);
            int unitPrice = addonPrice.get(selectedIndex);
            int qty = addonQuantity.get(i);
            totalPrices.add(unitPrice * qty);
        }
        return totalPrices;
    }

    public static int getTotalAddonPrice() {
        int total = 0;
        for (int price : totalPricesStatic) {
            total += price;
        }
        return total;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.recycler_admin_add_addon_meals, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        dropdownAdapter addonAdapter = new dropdownAdapter(context, R.layout.custom_spinner_bg, addonNameSpinner);
        addonAdapter.setDropDownViewResource(R.layout.custom_dropdown_bg);
        holder.addonSpinner.setAdapter(addonAdapter);

        Log.d("may error ka", "addon rpice" + String.valueOf(addonPrice));

        String selectedName = addonName.get(position);
        int selectedIndex = addonNameSpinner.indexOf(selectedName);
        if (selectedIndex != -1) {
            holder.addonSpinner.setSelection(selectedIndex);
            selectedIndexOnSpinner.add(selectedIndex);
        }

        holder.addonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    addonName.set(adapterPos, addonNameSpinner.get(spinnerPosition));
                    selectedIndexOnSpinner.set(adapterPos, spinnerPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        holder.addonQuantity.setText(String.valueOf(addonQuantity.get(position)));

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    int currentQty = addonQuantity.get(pos);
                    if (currentQty > 0) {
                        addonQuantity.set(pos, currentQty - 1);
                        holder.addonQuantity.setText(String.valueOf(addonQuantity.get(pos)));
                        notifyItemChanged(pos);
                        Log.d("may error ka", "addonName: " + addonName.get(pos) + " addonQuantity: " + String.valueOf(addonQuantity.get(pos)));
                    }
                }
            }
        });

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    int currentQty = addonQuantity.get(pos);
                    addonQuantity.set(pos, currentQty + 1);
                    holder.addonQuantity.setText(String.valueOf(addonQuantity.get(pos)));
                    notifyItemChanged(pos);
                }
            }
        });

        holder.trashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    addonName.remove(pos);
                    addonQuantity.remove(pos);
                    notifyItemRemoved(pos);
                    Log.d("may error ka", addonName + " quantity: " + String.valueOf(addonQuantity) + " spinner: " + addonNameSpinner);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addonQuantity.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView minusBtn, addonQuantity, addBtn;
        ImageView trashBtn;
        Spinner addonSpinner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            minusBtn = itemView.findViewById(R.id.minusBtn);
            addonQuantity = itemView.findViewById(R.id.addonQuantity);
            addBtn = itemView.findViewById(R.id.addBtn);
            addonSpinner = itemView.findViewById(R.id.addonSpinner);
            trashBtn = itemView.findViewById(R.id.trashBtn);
        }
    }
}
