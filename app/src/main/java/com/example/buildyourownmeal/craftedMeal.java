package com.example.buildyourownmeal;

import static com.example.buildyourownmeal.recyclerViewAdapterMealAddon.getMealTotalPrice;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class craftedMeal extends AppCompatActivity {

    //DATABASE
    private databaseFunctions databaseFunctions;

    //VARIABLE DECLARATION
    private static final String MEAL_TYPE = "Crafted Meal";
    private TextView totalPrice;
    private Dialog popUpLogInWarning;
    private Button backBtn, addBtn;
    private ImageView emptyBentoBox;
    private RecyclerView riceRecyclerView, mainDishRecyclerView, sideRecyclerView, sauceRecyclerView, dessertRecyclerView, drinkRecyclerView;
    private ArrayList<String>riceCategory, riceName, riceAddBtn, riceMinusBtn,
            mainDishCategory, mainDishName, mainDishAddBtn, mainDishMinusBtn,
            sideCategory, sideName, sideAddBtn, sideMinusBtn,
            sauceCategory, sauceName, sauceAddBtn, sauceMinusBtn,
            dessertCategory, dessertName, dessertAddBtn, dessertMinusBtn,
            drinkCategory, drinkName, drinkAddBtn, drinkMinusBtn;

    private ArrayList<Integer>
            riceId, ricePrice, riceQuantity,
            mainDishId, mainDishPrice, mainDishQuantity,
            sideId, sidePrice, sideQuantity,
            sauceId, saucePrice, sauceQuantity,
            dessertId, dessertPrice, dessertQuantity,
            drinkId, drinkPrice, drinkQuantity; 
    private ArrayList<Bitmap> riceImg, mainDishImg, sideImg, sauceImg, dessertImg, drinkImg;
    private HashMap<String, Integer> getAddonQuantity = new HashMap<String, Integer>();
    private int getAddonPrice = 0;

    private String getAddonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crafted_meal);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //SHARE PREFERENCES USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        String userRole = userSession.getString("role", "guest");
        int userId = userSession.getInt("userId", 0);

        //ORDER BTN
        addBtn = findViewById(R.id.addBtn);

        //REFERENCE
        totalPrice = findViewById(R.id.totalPrice);

        //MEAL IMAGE
        emptyBentoBox = findViewById(R.id.emptyBentoBox);
        BitmapDrawable getBitmap = (BitmapDrawable) emptyBentoBox.getDrawable();
        Bitmap bitmap = getBitmap.getBitmap();
        
        //RICE RECYCLER VIEW
        riceRecyclerView = findViewById(R.id.riceRecyclerView);
        riceName = new ArrayList<>();
        ricePrice = new ArrayList<>();
        riceImg = new ArrayList<>();
        riceQuantity = new ArrayList<>();
        riceId = new ArrayList<>();
        riceCategory = new ArrayList<>();
        riceAddBtn = new ArrayList<>();
        riceMinusBtn = new ArrayList<>();

        getAddonIdModel("rice", riceId);
        setUpAddonModel("rice", riceName, riceImg, ricePrice, riceQuantity, riceCategory, riceMinusBtn, riceAddBtn);

        riceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterMealAddon riceAdapter = new recyclerViewAdapterMealAddon(this, userId, riceName, riceMinusBtn, riceAddBtn, riceCategory, riceImg, ricePrice, riceQuantity, riceId);
        riceRecyclerView.setAdapter(riceAdapter);


        //MAIN DISH RECYCLER VIEW
        mainDishRecyclerView = findViewById(R.id.mainDishRecyclerView);
        mainDishName = new ArrayList<>();
        mainDishPrice = new ArrayList<>();
        mainDishImg = new ArrayList<>();
        mainDishQuantity = new ArrayList<>();
        mainDishId = new ArrayList<>();
        mainDishCategory = new ArrayList<>();
        mainDishAddBtn = new ArrayList<>();
        mainDishMinusBtn = new ArrayList<>();

        getAddonIdModel("main_dish", mainDishId);
        setUpAddonModel("main_dish", mainDishName, mainDishImg, mainDishPrice, mainDishQuantity, mainDishCategory, mainDishMinusBtn, mainDishAddBtn);

        mainDishRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterMealAddon mainDishAdapter = new recyclerViewAdapterMealAddon(this, userId, mainDishName, mainDishMinusBtn, mainDishAddBtn, mainDishCategory, mainDishImg, mainDishPrice, mainDishQuantity, mainDishId);
        mainDishRecyclerView.setAdapter(mainDishAdapter);


        //SIDE DISH RECYCLER VIEW
        sideRecyclerView = findViewById(R.id.sideRecyclerView);
        sideName = new ArrayList<>();
        sidePrice = new ArrayList<>();
        sideImg = new ArrayList<>();
        sideQuantity = new ArrayList<>();
        sideId = new ArrayList<>();
        sideCategory = new ArrayList<>();
        sideAddBtn = new ArrayList<>();
        sideMinusBtn = new ArrayList<>();

        getAddonIdModel("side_dish", sideId);
        setUpAddonModel("side_dish", sideName, sideImg, sidePrice, sideQuantity, sideCategory, sideMinusBtn, sideAddBtn);

        sideRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterMealAddon sideAdapter = new recyclerViewAdapterMealAddon(this, userId, sideName, sideMinusBtn, sideAddBtn, sideCategory, sideImg, sidePrice, sideQuantity, sideId);
        sideRecyclerView.setAdapter(sideAdapter);


        //SAUCE RECYCLER VIEW
        sauceRecyclerView = findViewById(R.id.sauceRecyclerView);
        sauceName = new ArrayList<>();
        saucePrice = new ArrayList<>();
        sauceImg = new ArrayList<>();
        sauceQuantity = new ArrayList<>();
        sauceId = new ArrayList<>();
        sauceCategory = new ArrayList<>();
        sauceAddBtn = new ArrayList<>();
        sauceMinusBtn = new ArrayList<>();

        getAddonIdModel("sauce", sauceId);
        setUpAddonModel("sauce", sauceName, sauceImg, saucePrice, sauceQuantity, sauceCategory, sauceMinusBtn, sauceAddBtn);

        sauceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterMealAddon sauceAdapter = new recyclerViewAdapterMealAddon(this, userId, sauceName, sauceMinusBtn, sauceAddBtn, sauceCategory, sauceImg, saucePrice, sauceQuantity, sauceId);
        sauceRecyclerView.setAdapter(sauceAdapter);


        //DESSERT RECYCLER VIEW
        dessertRecyclerView = findViewById(R.id.dessertRecyclerView);
        dessertName = new ArrayList<>();
        dessertPrice = new ArrayList<>();
        dessertImg = new ArrayList<>();
        dessertQuantity = new ArrayList<>();
        dessertId = new ArrayList<>();
        dessertCategory = new ArrayList<>();
        dessertAddBtn = new ArrayList<>();
        dessertMinusBtn = new ArrayList<>();

        getAddonIdModel("dessert", dessertId);
        setUpAddonModel("dessert", dessertName, dessertImg, dessertPrice, dessertQuantity, dessertCategory, dessertMinusBtn, dessertAddBtn);

        dessertRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterMealAddon dessertAdapter = new recyclerViewAdapterMealAddon(this, userId, dessertName, dessertMinusBtn, dessertAddBtn, dessertCategory, dessertImg, dessertPrice, dessertQuantity, dessertId);
        dessertRecyclerView.setAdapter(dessertAdapter);


        //DRINK RECYCLER VIEW
        drinkRecyclerView = findViewById(R.id.drinkRecyclerView);
        drinkName = new ArrayList<>();
        drinkPrice = new ArrayList<>();
        drinkImg = new ArrayList<>();
        drinkQuantity = new ArrayList<>();
        drinkId = new ArrayList<>();
        drinkCategory = new ArrayList<>();
        drinkAddBtn = new ArrayList<>();
        drinkMinusBtn = new ArrayList<>();

        getAddonIdModel("drink", drinkId);
        setUpAddonModel("drink", drinkName, drinkImg, drinkPrice, drinkQuantity, drinkCategory, drinkMinusBtn, drinkAddBtn);

        drinkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapterMealAddon drinkAdapter = new recyclerViewAdapterMealAddon(this, userId, drinkName, drinkMinusBtn, drinkAddBtn, drinkCategory, drinkImg, drinkPrice, drinkQuantity, drinkId);
        drinkRecyclerView.setAdapter(drinkAdapter);

        //POP UP ALERT
        popUpLogInWarning = new Dialog(this);
        popUpLogInWarning.setContentView(R.layout.pop_up_login_signup_alert);
        popUpLogInWarning.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpLogInWarning.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpLogInWarning.setCancelable(true);

        recyclerViewAdapterMealAddon.setOnPriceUpdatedListener(new recyclerViewAdapterMealAddon.OnPriceUpdateListener() {
            @Override
            public void onPriceUpdated(int newTotalPrice) {
                totalPrice.setText(String.valueOf(newTotalPrice));
            }
        });

        //LOGIC STATEMENT
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button popUpAlertLogInBtn, popUpAlertSignUpBtn;

                if (userRole.equals("guest")) {
                    popUpLogInWarning.show();

                    popUpAlertLogInBtn = popUpLogInWarning.findViewById(R.id.popUpAlertLogInBtn);
                    popUpAlertLogInBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(craftedMeal.this, logIn.class);
                            popUpLogInWarning.dismiss();
                            startActivity(intent);
                        }
                    });

                    popUpAlertSignUpBtn = popUpLogInWarning.findViewById(R.id.popUpAlertSignUpBtn);

                    popUpAlertSignUpBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(craftedMeal.this, signUp.class);
                            popUpLogInWarning.dismiss();
                            startActivity(intent);
                        }
                    });
                } else if (userRole.equals("user")) {
                    boolean insertAddonData = false;
                    int addonQuantity = 0;

                    getAddonQuantity.putAll(recyclerViewAdapterMealAddon.hashAddonQuantity);

                    for (Map.Entry<String, Integer> item : recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.entrySet()) {
                        getAddonName = item.getKey();
                        getAddonPrice = item.getValue();

                        insertAddonData = databaseFunctions.insertOrderAddonData(userId, getAddonName, getAddonQuantity.get(getAddonName), getAddonPrice);
                    }

                    int getMealTotalPrice = recyclerViewAdapterMealAddon.getMealTotalPrice();

                    if (insertAddonData) {
                        Cursor getAddonData = databaseFunctions.getAddonData(userId);

                        if (getAddonData != null && getAddonData.moveToFirst()) {
                            int getAddonId = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("orderAddonId"));
                            String addon = getAddonData.getString(getAddonData.getColumnIndexOrThrow("addon"));
                            String quantity = getAddonData.getString(getAddonData.getColumnIndexOrThrow("quantity"));
                            int price = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("price"));

                            boolean insertOrderData = databaseFunctions.insertOrderData(getAddonId, userId, bitmap, MEAL_TYPE, getMealTotalPrice);
                            if (insertOrderData) {
                                Intent intent = new Intent(craftedMeal.this, cart.class);
                                startActivity(intent);
                            }
                        }
                    }

                }
            }
        });

        //BACK BUTTON
        backBtn = findViewById(R.id.fabBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    
    public void setUpAddonModel(String addonTable, ArrayList<String> addonName, ArrayList<Bitmap> addonImg, ArrayList<Integer> addonPrice, ArrayList<Integer> addonQuantity, ArrayList<String> addonCategory, ArrayList<String> minusBtnAddon, ArrayList<String> addBtnAddon) {
        Cursor getAddonData = databaseFunctions.getAddonTable(addonTable);
        
        if (getAddonData.moveToFirst() && getAddonData != null) {
            do {
                addonName.add(getAddonData.getString(getAddonData.getColumnIndexOrThrow("name")));
                addonPrice.add(getAddonData.getInt(getAddonData.getColumnIndexOrThrow("price")));
                byte[] byteArray = getAddonData.getBlob(getAddonData.getColumnIndexOrThrow("img"));
                addonImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                addonQuantity.add(0);
                addonCategory.add(getAddonData.getString(getAddonData.getColumnIndexOrThrow("category")));
                minusBtnAddon.add("-");
                addBtnAddon.add("+");
            } while(getAddonData.moveToNext());
            getAddonData.close();
        }
    }

    public void getAddonIdModel(String tableName, ArrayList<Integer> addonId) {
        Cursor getAddonId = databaseFunctions.getAddonTable(tableName);

        String addonTableId = "";

        switch (tableName) {
            case "rice":
                addonTableId = "riceId";
                if (getAddonId.moveToFirst() && getAddonId != null) {
                    do {
                        int addonRiceTable = getAddonId.getInt(getAddonId.getColumnIndexOrThrow(addonTableId));
                        addonId.add(addonRiceTable);
                    } while (getAddonId.moveToNext());
                    getAddonId.close();
                }

                break;
            case "main_dish":
                addonTableId = "mainDishId";
                if (getAddonId.moveToFirst() && getAddonId != null) {
                    do {
                        int addonMainDishTable = getAddonId.getInt(getAddonId.getColumnIndexOrThrow(addonTableId));
                        addonId.add(addonMainDishTable);
                    } while (getAddonId.moveToNext());
                    getAddonId.close();
                }
                break;
            case "side_dish":
                addonTableId = "sideDishId";
                if (getAddonId.moveToFirst() && getAddonId != null) {
                    do {
                        int addonSideTable = getAddonId.getInt(getAddonId.getColumnIndexOrThrow(addonTableId));
                        addonId.add(addonSideTable);
                    } while (getAddonId.moveToNext());
                    getAddonId.close();
                }
                break;
            case "sauce":
                addonTableId = "sauceId";
                if (getAddonId.moveToFirst() && getAddonId != null) {
                    do {
                        int addonSauceTable = getAddonId.getInt(getAddonId.getColumnIndexOrThrow(addonTableId));
                        addonId.add(addonSauceTable);
                    } while (getAddonId.moveToNext());
                    getAddonId.close();
                }
                break;
            case "dessert":
                addonTableId = "dessertId";
                if (getAddonId.moveToFirst() && getAddonId != null) {
                    do {
                        int addonDessertTable = getAddonId.getInt(getAddonId.getColumnIndexOrThrow(addonTableId));
                        addonId.add(addonDessertTable);
                    } while (getAddonId.moveToNext());
                    getAddonId.close();
                }
                break;
            case "drink":
                addonTableId = "drinkId";
                if (getAddonId.moveToFirst() && getAddonId != null) {
                    do {
                        int addonDrinkTable = getAddonId.getInt(getAddonId.getColumnIndexOrThrow(addonTableId));
                        addonId.add(addonDrinkTable);
                    } while (getAddonId.moveToNext());
                    getAddonId.close();
                }
                break;

        }
    }
}