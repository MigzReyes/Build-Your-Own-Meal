package com.example.buildyourownmeal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

        totalPrices = new ArrayList<>();
        for (int i = 0; i < addonName.size(); i++) {
            totalPrices.add(0);
        }

        selectedIndexOnSpinner = new ArrayList<>();
        for (int i = 0; i < addonName.size(); i++) {
            selectedIndexOnSpinner.add(0);
        }
    }

    public void clearAll() {
        addonName.clear();
        addonQuantity.clear();
        totalPrices.clear();
        selectedIndexOnSpinner.clear();
        notifyDataSetChanged();
    }


    public void updateQuantity(String name, int quantity) {
        int index = addonName.indexOf(name);
        if (index != -1) {
            addonQuantity.set(index, quantity);

            // Recalculate total price
            int spinnerIndex = selectedIndexOnSpinner.get(index);
            int unitPrice = addonPrice.get(spinnerIndex);
            totalPrices.set(index, unitPrice * quantity);

            notifyItemChanged(index);
        } else {
            addonName.add(name);
            addonQuantity.add(quantity);

            int spinnerIndex = addonNameSpinner.indexOf(name);
            if (spinnerIndex == -1) spinnerIndex = 0;
            selectedIndexOnSpinner.add(spinnerIndex);

            int unitPrice = addonPrice.get(spinnerIndex);
            totalPrices.add(unitPrice * quantity);

            notifyItemInserted(addonName.size() - 1);
        }
    }


    private int getPriceForName(String name) {
        int index = addonNameSpinner.indexOf(name);
        if (index != -1 && index < addonPrice.size()) {
            return addonPrice.get(index);
        }
        return 0;
    }

    public boolean addItem(String name, int quantity) {
        if (!addonName.contains(name)) {
            int spinnerIndex = addonNameSpinner.indexOf(name);
            if (spinnerIndex == -1) spinnerIndex = 0;

            addonName.add(name);
            addonQuantity.add(quantity);
            selectedIndexOnSpinner.add(spinnerIndex);

            int unitPrice = addonPrice.get(spinnerIndex);
            totalPrices.add(unitPrice * quantity);

            notifyItemInserted(addonName.size() - 1);
            return true;
        }

        for (int i = 0; i < addonNameSpinner.size(); i++) {
            String candidate = addonNameSpinner.get(i);
            if (!addonName.contains(candidate)) {
                int spinnerIndex = i;

                addonName.add(candidate);
                addonQuantity.add(quantity);
                selectedIndexOnSpinner.add(spinnerIndex);

                int unitPrice = addonPrice.get(spinnerIndex);
                totalPrices.add(unitPrice * quantity);

                notifyItemInserted(addonName.size() - 1);
                return true;
            }
        }

        return false;
    }

    public ArrayList<String> getAddonNames() {
        return new ArrayList<>(addonName);
    }

    public ArrayList<Integer> getAddonQuantities() {
        return new ArrayList<>(addonQuantity);
    }

    public ArrayList<Integer> getTotalPrices() {
        // Always recalculate to ensure accuracy
        totalPrices = new ArrayList<>();
        for (int i = 0; i < addonQuantity.size(); i++) {
            int spinnerIndex = selectedIndexOnSpinner.get(i);
            int unitPrice = addonPrice.get(spinnerIndex);
            int qty = addonQuantity.get(i);
            totalPrices.add(unitPrice * qty);
        }
        return new ArrayList<>(totalPrices);
    }

    public void syncStaticList() {
        addonNameStatic = new ArrayList<>(addonName);
        addonQuantityStatic = new ArrayList<>(addonQuantity);
        addonPriceStatic = new ArrayList<>(addonPrice);
        selectedIndexOnSpinnerStatic = new ArrayList<>(selectedIndexOnSpinner);
        totalPricesStatic = getEachAddonTotalPrice();
    }

    private void updateTotalPriceAt(int index) {
        if (index >= 0 && index < addonQuantity.size()) {
            int spinnerIndex = selectedIndexOnSpinner.get(index);
            int unitPrice = addonPrice.get(spinnerIndex);
            int qty = addonQuantity.get(index);
            totalPrices.set(index, unitPrice * qty);
        }
    }


    public ArrayList<Integer> getEachAddonTotalPrice() {
        totalPrices = new ArrayList<>();
        for (int i = 0; i < addonQuantity.size(); i++) {
            int selectedIndex = selectedIndexOnSpinner.get(i);
            int unitPrice = addonPrice.get(selectedIndex);
            int qty = addonQuantity.get(i);
            totalPrices.add(unitPrice * qty);
        }
        return totalPrices;
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
        }

        holder.addonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    String selectedAddon = addonNameSpinner.get(spinnerPosition);

                    boolean isDuplicate = false;
                    for (int i = 0; i < addonName.size(); i++) {
                        if (i != adapterPos && addonName.get(i).equals(selectedAddon)) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    if (isDuplicate) {
                        int previousIndex = selectedIndexOnSpinner.get(adapterPos);
                        holder.addonSpinner.setSelection(previousIndex);

                        Dialog popUpAlert;
                        Button closeBtn;
                        TextView alertText;

                        popUpAlert = new Dialog(context);
                        popUpAlert.setContentView(R.layout.pop_up_alerts);
                        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                        popUpAlert.setCancelable(true);
                        popUpAlert.show();

                        alertText = popUpAlert.findViewById(R.id.alertText);
                        alertText.setText("Addon has already been chosen");

                        closeBtn = popUpAlert.findViewById(R.id.closeBtn);
                        closeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpAlert.dismiss();
                            }
                        });

                    } else {
                        addonName.set(adapterPos, selectedAddon);
                        selectedIndexOnSpinner.set(adapterPos, spinnerPosition);
                        updateTotalPriceAt(adapterPos);
                    }

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
                        updateTotalPriceAt(pos);
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
                    updateTotalPriceAt(pos);
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
                    totalPrices.remove(pos);
                    selectedIndexOnSpinner.remove(pos);
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
