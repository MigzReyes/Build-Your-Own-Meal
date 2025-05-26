package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class adminAddMeals extends AppCompatActivity {

    //DATABASE
    private databaseFunctions databaseFunctions;

    private ImageView backBtn;
    private TextView activityName, mealImg, addItemBtnRice, addItemBtnMainDish, addItemBtnSide, addItemBtnSauce, addItemBtnDessert, addItemBtnDrink;
    private EditText mealName, mealDescription;
    private Button cancelBtn, createBtn;
    private Bitmap bitMealImg;

    //RECYCLER
    private RecyclerView riceRecyclerView, mainDishRecyclerView, sideRecyclerView, sauceRecyclerView, dessertRecyclerView, drinkRecyclerView;
    private ArrayList<String> riceSpinner, mainDishSpinner, sideSpinner, sauceSpinner, dessertSpinner, drinkSpinner;
    private ArrayList<String> riceName, mainDishName, sideName, sauceName, dessertName, drinkName;
    private ArrayList<Integer> riceQuantity, mainDishQuantity, sideQuantity, sauceQuantity, dessertQuantity, drinkQuantity,
                                ricePrice, mainDishPrice, sidePrice, saucePrice, dessertPrice, drinkPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_meals);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //REFERENCE
        backBtn = findViewById(R.id.backBtn);
        activityName = findViewById(R.id.sideFragName);
        mealName = findViewById(R.id.mealName);
        mealImg = findViewById(R.id.mealImage);
        mealDescription = findViewById(R.id.mealDescription);
        addItemBtnRice = findViewById(R.id.addItemBtnRice);
        addItemBtnMainDish = findViewById(R.id.addItemBtnMainDish);
        addItemBtnSide = findViewById(R.id.addItemBtnSide);
        addItemBtnSauce = findViewById(R.id.addItemBtnSauce);
        addItemBtnDessert = findViewById(R.id.addItemBtnDessert);
        addItemBtnDrink = findViewById(R.id.addItemBtnDrink);
        cancelBtn = findViewById(R.id.cancelBtn);
        createBtn = findViewById(R.id.createBtn);

        activityName.setText(getString(R.string.meals));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //GET IMAGE
        ActivityResultLauncher<String> getImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {

                        if (o == null) {

                            return;
                        }

                        String imageUri = String.valueOf(o);
                        mealImg.setText(imageUri);

                        try {
                            downloadImg(o);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

        mealImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage.launch("image/*");
            }
        });

        //RECYCLER RICE
        riceRecyclerView = findViewById(R.id.riceRecyclerView);
        riceSpinner = new ArrayList<>();
        riceName = new ArrayList<>();
        riceQuantity = new ArrayList<>();
        ricePrice = new ArrayList<>();

        setUpAdminMealAddons("rice", riceName, riceQuantity, riceSpinner, ricePrice);

        Log.d("may error ka", riceName + " " + String.valueOf(riceQuantity));

        recyclerViewAdapterAdminAddAddonMeals riceAdapter = new recyclerViewAdapterAdminAddAddonMeals(adminAddMeals.this, riceName, riceSpinner, riceQuantity, ricePrice);
        riceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        riceRecyclerView.setAdapter(riceAdapter);

        addItemBtnRice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (riceSpinner.isEmpty()) {
                    popUpAlert(getString(R.string.thereIsCurrentlyNoAddon));
                } else {
                    riceAdapter.addItem(riceSpinner.get(0), 1);
                    Log.d("may error ka", riceName + " " + String.valueOf(riceQuantity));
                }
            }
        });

        //RECYCLER MAIN DISH
        mainDishRecyclerView = findViewById(R.id.mainDishRecyclerView);
        mainDishSpinner = new ArrayList<>();
        mainDishName = new ArrayList<>();
        mainDishQuantity = new ArrayList<>();
        mainDishPrice = new ArrayList<>();

        setUpAdminMealAddons("main_dish", mainDishName, mainDishQuantity, mainDishSpinner, mainDishPrice);

        recyclerViewAdapterAdminAddAddonMeals mainAdapter = new recyclerViewAdapterAdminAddAddonMeals(adminAddMeals.this, mainDishName, mainDishSpinner, mainDishQuantity, mainDishPrice);
        mainDishRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainDishRecyclerView.setAdapter(mainAdapter);

        addItemBtnMainDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainDishSpinner.isEmpty()) {
                    popUpAlert(getString(R.string.thereIsCurrentlyNoAddon));
                } else {
                    mainAdapter.addItem(mainDishSpinner.get(0), 1);
                    Log.d("may error ka", mainDishName + " " + String.valueOf(mainDishQuantity));
                }
            }
        });

        //RECYCLER SIDE
        sideRecyclerView = findViewById(R.id.sideRecyclerView);
        sideSpinner = new ArrayList<>();
        sideName = new ArrayList<>();
        sideQuantity = new ArrayList<>();
        sidePrice = new ArrayList<>();

        setUpAdminMealAddons("side_dish", sideName, sideQuantity, sideSpinner, sidePrice);

        recyclerViewAdapterAdminAddAddonMeals sideAdapter = new recyclerViewAdapterAdminAddAddonMeals(adminAddMeals.this, sideName, sideSpinner, sideQuantity, sidePrice);
        sideRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sideRecyclerView.setAdapter(sideAdapter);

        addItemBtnSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sideSpinner.isEmpty()) {
                    popUpAlert(getString(R.string.thereIsCurrentlyNoAddon));
                } else {
                    sideAdapter.addItem(sideSpinner.get(0), 1);
                    Log.d("may error ka", sideName + " " + String.valueOf(sideQuantity));
                }
            }
        });

        //RECYCLER SAUCE
        sauceRecyclerView = findViewById(R.id.sauceRecyclerView);
        sauceSpinner = new ArrayList<>();
        sauceName = new ArrayList<>();
        sauceQuantity = new ArrayList<>();
        saucePrice = new ArrayList<>();

        setUpAdminMealAddons("sauce", sauceName, sauceQuantity, sauceSpinner, saucePrice);

        recyclerViewAdapterAdminAddAddonMeals sauceAdapter = new recyclerViewAdapterAdminAddAddonMeals(adminAddMeals.this, sauceName, sauceSpinner, sauceQuantity, saucePrice);
        sauceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sauceRecyclerView.setAdapter(sauceAdapter);

        addItemBtnSauce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sauceSpinner.isEmpty()) {
                popUpAlert(getString(R.string.thereIsCurrentlyNoAddon));
                } else {
                    sauceAdapter.addItem(dessertSpinner.get(0), 1);
                    Log.d("may error ka", sauceName + " " + String.valueOf(sauceQuantity));
                }
            }
        });

        //RECYCLER DESSERT
        dessertRecyclerView = findViewById(R.id.dessertRecyclerView);
        dessertSpinner = new ArrayList<>();
        dessertName = new ArrayList<>();
        dessertQuantity = new ArrayList<>();
        dessertPrice = new ArrayList<>();

        setUpAdminMealAddons("dessert", dessertName, dessertQuantity, dessertSpinner, dessertPrice);

        recyclerViewAdapterAdminAddAddonMeals dessertAdapter = new recyclerViewAdapterAdminAddAddonMeals(adminAddMeals.this, dessertName, dessertSpinner, dessertQuantity, dessertPrice);
        dessertRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dessertRecyclerView.setAdapter(dessertAdapter);

        addItemBtnDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dessertSpinner.isEmpty()) {
                    popUpAlert(getString(R.string.thereIsCurrentlyNoAddon));
                } else {
                    dessertAdapter.addItem(dessertSpinner.get(0), 1);
                    Log.d("may error ka", dessertName + " " + String.valueOf(dessertQuantity));
                }
            }
        });

        //RECYCLER DRINK
        drinkRecyclerView = findViewById(R.id.drinkRecyclerView);
        drinkSpinner = new ArrayList<>();
        drinkName = new ArrayList<>();
        drinkQuantity = new ArrayList<>();
        drinkPrice = new ArrayList<>();

        setUpAdminMealAddons("drink", drinkName, drinkQuantity, drinkSpinner, drinkPrice);

        recyclerViewAdapterAdminAddAddonMeals drinkAdapter = new recyclerViewAdapterAdminAddAddonMeals(adminAddMeals.this, drinkName, drinkSpinner, drinkQuantity, drinkPrice);
        drinkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        drinkRecyclerView.setAdapter(drinkAdapter);

        addItemBtnDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkSpinner.isEmpty()) {
                    popUpAlert(getString(R.string.thereIsCurrentlyNoAddon));
                } else {
                    drinkAdapter.addItem(drinkSpinner.get(0), 1);
                    Log.d("may error ka", drinkName + " " + String.valueOf(drinkQuantity));
                }
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //CREATE MEAL
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("may error ka", "addon name static: " + recyclerViewAdapterAdminAddAddonMeals.addonNameStatic + " addonQuantity: " + String.valueOf(recyclerViewAdapterAdminAddAddonMeals.addonQuantityStatic));
                String adminAddonId = UUID.randomUUID().toString();
                String getMealName = mealName.getText().toString().trim();
                String getMealText = mealImg.getText().toString().trim();
                String getMealDescription = mealDescription.getText().toString().trim();

                ArrayList<String> allNames = new ArrayList<>();
                ArrayList<Integer> allQuantities = new ArrayList<>();
                ArrayList<Integer> allPrices = new ArrayList<>();

                addFromAdapter(riceAdapter, allNames, allQuantities, allPrices);
                addFromAdapter(mainAdapter, allNames, allQuantities, allPrices);
                addFromAdapter(sideAdapter, allNames, allQuantities, allPrices);
                addFromAdapter(sauceAdapter, allNames, allQuantities, allPrices);
                addFromAdapter(dessertAdapter, allNames, allQuantities, allPrices);
                addFromAdapter(drinkAdapter, allNames, allQuantities, allPrices);


                if (getMealName.isBlank() || getMealDescription.isBlank()) {
                    popUpAlert(getString(R.string.pleaseFillUpTheInputField));
                } else if (getMealText.equals("Choose File")) {
                    popUpAlert(getString(R.string.pleaseChooseAnImage));
                } else if (allNames.isEmpty()) {
                    popUpAlert(getString(R.string.pleaseChooseAnAddon));
                } else {
                    for (int i = 0; i < allNames.size(); i++) {
                        databaseFunctions.insertOrderAddonData(1, adminAddonId, allNames.get(i), allQuantities.get(i), allPrices.get(i));
                    }

                    int mealTotalPrice = 0;
                    for (int price : allPrices) {
                        mealTotalPrice += price;
                    }

                    boolean insertAdminMealData = databaseFunctions.insertAdminMeal(adminAddonId, getMealName, getMealDescription, bitMealImg, mealTotalPrice);

                    if (insertAdminMealData) {
                        Intent intent = new Intent(adminAddMeals.this, adminMeals.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("may error ka", "insert admin meal failed");
                    }
                }
            }
        });

    }

    private void addFromAdapter(recyclerViewAdapterAdminAddAddonMeals adapter, ArrayList<String> names, ArrayList<Integer> quantities, ArrayList<Integer> prices) {
        names.addAll(adapter.getAddonNames());
        quantities.addAll(adapter.getAddonQuantities());
        prices.addAll(adapter.getTotalPrices());
    }

    private void popUpAlert(String setAlertText) {
        Dialog popUpAlert;
        TextView alertText;
        Button closeBtn;

        popUpAlert = new Dialog(this);
        popUpAlert.setContentView(R.layout.pop_up_alerts);
        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpAlert.setCancelable(true);
        popUpAlert.show();

        alertText = popUpAlert.findViewById(R.id.alertText);
        alertText.setText(setAlertText);

        closeBtn = popUpAlert.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpAlert.dismiss();
            }
        });
    }

    private void setUpAdminMealAddons(String addonTable, ArrayList<String> addonName, ArrayList<Integer> addonQuantity, ArrayList<String> addonNameSpinner, ArrayList<Integer> addonPrice) {
        Cursor getAddonTable = databaseFunctions.getAddonTable(addonTable);

        if (getAddonTable != null && getAddonTable.moveToFirst()) {
            do {
                addonNameSpinner.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("name")));
                addonPrice.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("price")));
            } while (getAddonTable.moveToNext());
            getAddonTable.close();

            if (!addonNameSpinner.isEmpty()) {
                addonQuantity.add(1);
                addonName.add(addonNameSpinner.get(0));
            }
        }
    }

    private void downloadImg(Uri imgUrl) throws IOException {
        BitmapFactory.Options uri = new BitmapFactory.Options();
        uri.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUrl), null, uri);

        final int REQUIRED_SIZE = 140;

        int width = uri.outWidth, height = uri.outHeight;
        int scale = 1;
        while (width / 2 >= REQUIRED_SIZE && height / 2 >= REQUIRED_SIZE) {
            width /= 2;
            height /= 2;
            scale *= 2;
        }

        BitmapFactory.Options uri2 = new BitmapFactory.Options();
        uri2.inSampleSize = scale;

        //MediaStore.Images.Media.getBitmap(getContentResolver(), imgUrl)
        this.bitMealImg = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUrl), null, uri2);
    }
}