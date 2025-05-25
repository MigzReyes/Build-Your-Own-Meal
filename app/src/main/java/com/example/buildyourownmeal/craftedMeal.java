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
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class craftedMeal extends AppCompatActivity {

    //DATABASE
    private databaseFunctions databaseFunctions;

    //VARIABLE DECLARATION
    private static final String MEAL_TYPE = "Crafted Meal";
    private TextView totalPrice;
    private Dialog popUpLogInWarning;
    private Button backBtn, addBtn;
    private ImageView emptyBentoBox;
    private ArrayList<String> getOrderAddonName;
    private ArrayList<Integer> getOrderAddonQuantity;
    public ArrayList<String> getAddonTable;
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
    private Intent addUserOrder;
    private boolean editMeal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crafted_meal);

        // CHECK IF USER CLICKED FROM CART
        editMeal = getIntent().getBooleanExtra("editMeal", false);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //ORDER ADDON GROUP ID
        SharedPreferences getOrderAddonId = getSharedPreferences("addonGroupId", MODE_PRIVATE);
        SharedPreferences.Editor edit = getOrderAddonId.edit();


        //SHARE PREFERENCES USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        String userRole = userSession.getString("role", "guest");
        int userId = userSession.getInt("userId", 0);

        //ORDER BTN
        addBtn = findViewById(R.id.addBtn);
        if (editMeal) {
            addBtn.setText("Edit");
        }

        //REFERENCE
        totalPrice = findViewById(R.id.totalPrice);

        //MEAL IMAGE
        emptyBentoBox = findViewById(R.id.emptyBentoBox);
        BitmapDrawable getBitmap = (BitmapDrawable) emptyBentoBox.getDrawable();
        Bitmap bitmap = getBitmap.getBitmap();

        //getOrderAddonId();
        //getAddon(getOrderAddonName.get());
        String orderAddonGroupId = "";
        if (editMeal) {
            orderAddonGroupId = getIntent().getStringExtra("addonGroupId");
            Log.d("may error ka", "edit meal:true " + orderAddonGroupId);
        } else {
            orderAddonGroupId = getOrderAddonId.getString("addonGroupId", "");
            Log.d("may error ka", "edit meal:false " + orderAddonGroupId);
        }

        if (orderAddonGroupId.isBlank() && !editMeal) {
            Log.d("may error ka", "Empty addon group id");
            getOrderAddonQuantity = new ArrayList<>();
            getOrderAddonName = new ArrayList<>();
            getAddonTable = new ArrayList<>();
            //CLEAR ARRAY LIST
            if (!getOrderAddonQuantity.isEmpty()) {
                getOrderAddonQuantity.clear();
                getOrderAddonName.clear();
                getAddonTable.clear();
            }
            recyclerViewAdapterMealAddon.hashAddonQuantity.clear();
            recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.clear();
        } else {
            Log.d("may error ka", orderAddonGroupId);

            if (getOrderAddonQuantity != null) getOrderAddonQuantity.clear();
            else getOrderAddonQuantity = new ArrayList<>();

            if (getOrderAddonName != null) getOrderAddonName.clear();
            else getOrderAddonName = new ArrayList<>();

            if (getAddonTable != null) getAddonTable.clear();
            else getAddonTable = new ArrayList<>();

            recyclerViewAdapterMealAddon.hashAddonQuantity.clear();
            recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.clear();

            getAddon(orderAddonGroupId);
            getAddonQuantity(orderAddonGroupId);
            getAddonTable(orderAddonGroupId);
            Log.d("may error ka", String.valueOf(getOrderAddonName));
            Log.d("may error ka", String.valueOf(getOrderAddonQuantity));
            Log.d("may error ka", "Addon table: " + String.valueOf(getAddonTable));

            edit.putString("addonGroupId", null);
            edit.apply();
        }
        
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

        for (int i = 0; i < riceName.size(); i++) {
            riceQuantity.add(0);
        }

        populateAddonQuantity(orderAddonGroupId, "rice", riceName, riceQuantity);

        for (int i = 0; i < riceName.size(); i++) {
            if (riceQuantity.get(i) > 0) {
                recyclerViewAdapterMealAddon.hashAddonQuantity.put(riceName.get(i), riceQuantity.get(i));
                recyclerViewAdapterMealAddon.hashAddonPrice.put(riceName.get(i), ricePrice.get(i));
            }
        }

        for (int i = 0; i < riceName.size(); i++) {
            int quantity = riceQuantity.get(i);
            if (quantity > 0) {
                int price = ricePrice.get(i);
                recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.put(riceName.get(i), price * quantity);
            }
        }

        Log.d("may error ka", " addonName: " + String.valueOf(riceName) + " addonQuantity: " + String.valueOf(riceQuantity));

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

        for (int i = 0; i < mainDishName.size(); i++) {
            mainDishQuantity.add(0);
        }

        populateAddonQuantity(orderAddonGroupId, "main_dish", mainDishName, mainDishQuantity);

        for (int i = 0; i < mainDishName.size(); i++) {
            if (mainDishQuantity.get(i) > 0) {
                recyclerViewAdapterMealAddon.hashAddonQuantity.put(mainDishName.get(i), mainDishQuantity.get(i));
                recyclerViewAdapterMealAddon.hashAddonPrice.put(mainDishName.get(i), mainDishPrice.get(i));
            }
        }

        for (int i = 0; i < mainDishName.size(); i++) {
            int quantity = mainDishQuantity.get(i);
            if (quantity > 0) {
                int price = mainDishPrice.get(i);
                recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.put(mainDishName.get(i), price * quantity);
            }
        }

        Log.d("may error ka", " addonName: " + String.valueOf(mainDishName) + " addonQuantity: " + String.valueOf(mainDishQuantity));

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

        for (int i = 0; i < sideName.size(); i++) {
            sideQuantity.add(0);
        }

        populateAddonQuantity(orderAddonGroupId, "side_dish", sideName, sideQuantity);

        for (int i = 0; i < sideName.size(); i++) {
            if (sideQuantity.get(i) > 0) {
                recyclerViewAdapterMealAddon.hashAddonQuantity.put(sideName.get(i), sideQuantity.get(i));
                recyclerViewAdapterMealAddon.hashAddonPrice.put(sideName.get(i), sidePrice.get(i));
            }
        }

        for (int i = 0; i < sideName.size(); i++) {
            int quantity = sideQuantity.get(i);
            if (quantity > 0) {
                int price = sidePrice.get(i);
                recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.put(sideName.get(i), price * quantity);
            }
        }

        Log.d("may error ka", " addonName: " + String.valueOf(sideName) + " addonQuantity: " + String.valueOf(sideQuantity));

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

        for (int i = 0; i < sauceName.size(); i++) {
            sauceQuantity.add(0);
        }

        populateAddonQuantity(orderAddonGroupId, "sauce", sauceName, sauceQuantity);

        for (int i = 0; i < sauceName.size(); i++) {
            if (sauceQuantity.get(i) > 0) {
                recyclerViewAdapterMealAddon.hashAddonQuantity.put(sauceName.get(i), sauceQuantity.get(i));
                recyclerViewAdapterMealAddon.hashAddonPrice.put(sauceName.get(i), saucePrice.get(i));
            }
        }

        for (int i = 0; i < sauceName.size(); i++) {
            int quantity = sauceQuantity.get(i);
            if (quantity > 0) {
                int price = saucePrice.get(i);
                recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.put(sauceName.get(i), price * quantity);
            }
        }

        Log.d("may error ka", " addonName: " + String.valueOf(sauceName) + " addonQuantity: " + String.valueOf(sauceQuantity));


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

        for (int i = 0; i < dessertName.size(); i++) {
            dessertQuantity.add(0);
        }

        populateAddonQuantity(orderAddonGroupId, "dessert", dessertName, dessertQuantity);

        for (int i = 0; i < dessertName.size(); i++) {
            if (dessertQuantity.get(i) > 0) {
                recyclerViewAdapterMealAddon.hashAddonQuantity.put(dessertName.get(i), dessertQuantity.get(i));
                recyclerViewAdapterMealAddon.hashAddonPrice.put(dessertName.get(i), dessertPrice.get(i));
            }
        }

        for (int i = 0; i < dessertName.size(); i++) {
            int quantity = dessertQuantity.get(i);
            if (quantity > 0) {
                int price = dessertPrice.get(i);
                recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.put(dessertName.get(i), price * quantity);
            }
        }

        Log.d("may error ka", " addonName: " + String.valueOf(dessertName) + " addonQuantity: " + String.valueOf(dessertQuantity));


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

        for (int i = 0; i < drinkName.size(); i++) {
            drinkQuantity.add(0);
        }

        populateAddonQuantity(orderAddonGroupId, "drink", drinkName, drinkQuantity);

        for (int i = 0; i < drinkName.size(); i++) {
            if (drinkQuantity.get(i) > 0) {
                recyclerViewAdapterMealAddon.hashAddonQuantity.put(drinkName.get(i), drinkQuantity.get(i));
                recyclerViewAdapterMealAddon.hashAddonPrice.put(drinkName.get(i), drinkPrice.get(i));
            }
        }

        for (int i = 0; i < drinkName.size(); i++) {
            int quantity = drinkQuantity.get(i);
            if (quantity > 0) {
                int price = drinkPrice.get(i);
                recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.put(drinkName.get(i), price * quantity);
            }
        }

        Log.d("may error ka", " addonName: " + String.valueOf(drinkName) + " addonQuantity: " + String.valueOf(drinkQuantity));


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


        int mealTotalPrice = getIntent().getIntExtra("mealTotalPrice", 0);
        totalPrice.setText(String.valueOf(mealTotalPrice));


        //LOGIC STATEMENT
        String finalOrderAddonGroupId = orderAddonGroupId;
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
                    if (totalPrice.getText().toString().trim().equals("0")) {
                        Dialog popUpAlert;
                        Button closeBtn;
                        TextView alertText;

                        popUpAlert = new Dialog(craftedMeal.this);
                        popUpAlert.setContentView(R.layout.pop_up_alerts);
                        popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                        popUpAlert.setCancelable(true);
                        popUpAlert.show();

                        alertText = popUpAlert.findViewById(R.id.alertText);
                        alertText.setText(getString(R.string.pleaseChooseAnAddon));

                        closeBtn = popUpAlert.findViewById(R.id.closeBtn);
                        closeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpAlert.dismiss();
                            }
                        });
                    } else {

                        if (editMeal) {
                            boolean updateAddonData = false;
                            boolean checkAddonGroup = databaseFunctions.checkAddonGroup(userId, finalOrderAddonGroupId);

                            if (checkAddonGroup) {
                                getAddonQuantity.putAll(recyclerViewAdapterMealAddon.hashAddonQuantity);
                                Log.d("may error ka", "addon quanityt list: " + String.valueOf(getAddonQuantity));

                                for (Map.Entry<String, Integer> item : recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.entrySet()) {
                                    getAddonName = item.getKey();
                                    getAddonPrice = item.getValue();

                                    updateAddonData = databaseFunctions.updateOrderAddon(userId, finalOrderAddonGroupId, getAddonName, getAddonQuantity.get(getAddonName), getAddonPrice);
                                    Log.d("may error ka", "addon name: " + getAddonName + " addonQuantity: " + String.valueOf(getAddonQuantity.get(getAddonName)) + " Addon price: " + String.valueOf(getAddonPrice));
                                }

                                int getMealTotalPrice = getMealTotalPrice();

                                if (updateAddonData) {
                                    Cursor getAddonData = databaseFunctions.getAddonData(userId);

                                    if (getAddonData != null && getAddonData.moveToFirst()) {
                                        boolean updateUserOrder = databaseFunctions.updateUserOrder(finalOrderAddonGroupId, userId, bitmap, MEAL_TYPE, 1, getMealTotalPrice);
                                        if (updateUserOrder) {
                                            addUserOrder = new Intent(craftedMeal.this, cart.class);
                                            Log.d("may error ka", "addon group id: " + finalOrderAddonGroupId);
                                            addUserOrder.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(addUserOrder);
                                            finish();
                                        }
                                    }
                                } else {
                                    Log.d("may error ka", "addon group id does not exists");
                                }
                            }
                        } else {
                            boolean insertAddonData = false;

                            getAddonQuantity.putAll(recyclerViewAdapterMealAddon.hashAddonQuantity);
                            String addonGroupId = UUID.randomUUID().toString();

                            for (Map.Entry<String, Integer> item : recyclerViewAdapterMealAddon.hashAddonPerTotalPrice.entrySet()) {
                                getAddonName = item.getKey();
                                getAddonPrice = item.getValue();

                                insertAddonData = databaseFunctions.insertOrderAddonData(userId, addonGroupId, getAddonName, getAddonQuantity.get(getAddonName), getAddonPrice);
                            }

                            int getMealTotalPrice = getMealTotalPrice();

                            if (insertAddonData) {
                                Cursor getAddonData = databaseFunctions.getAddonData(userId);

                                if (getAddonData != null && getAddonData.moveToFirst()) {
                                    boolean insertOrderData = databaseFunctions.insertOrderData(addonGroupId, userId, bitmap, MEAL_TYPE, 1, getMealTotalPrice);
                                    if (insertOrderData) {
                                        addUserOrder = new Intent(craftedMeal.this, cart.class);
                                        addUserOrder.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(addUserOrder);
                                        finish();
                                    }
                                }
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

    public void populateAddonQuantity(String orderAddonGroup, String table, ArrayList<String> addonNames, ArrayList<Integer> addonQuantity) {
        Cursor cursor = databaseFunctions.getOrderAddonWithQuantity(orderAddonGroup);

        Log.d("may error ka", "addon group id in populate addon quanityt: " + orderAddonGroup);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String addonName = cursor.getString(cursor.getColumnIndexOrThrow("addon"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

                boolean checkAddonTable = databaseFunctions.checkAddonTable(table, addonName);

                if (checkAddonTable) {
                    for (int i = 0; i < addonNames.size(); i++) {
                        if (addonNames.get(i).equals(addonName)) {
                            addonQuantity.set(i, quantity);
                            break;
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    public void getAddonTable(String addonGroupId) {
        getAddonTable = new ArrayList<>();
        String[] tables = {"rice", "main_dish", "side_dish", "sauce", "dessert", "drink"};

        Cursor cursor = databaseFunctions.getOrderAddonName(addonGroupId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String addonName = cursor.getString(cursor.getColumnIndexOrThrow("addon"));

                for (String table : tables) {
                    if (databaseFunctions.checkAddonTable(table, addonName)) {
                        getAddonTable.add(table);
                        break;
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    public void getAddonQuantity(String addonGroupId) {
        Cursor getAddonQuantity = databaseFunctions.getOrderAddonQuantity(addonGroupId);
        getOrderAddonQuantity = new ArrayList<>();

        if (getAddonQuantity != null && getAddonQuantity.moveToFirst()) {
            do {
            getOrderAddonQuantity.add(getAddonQuantity.getInt(getAddonQuantity.getColumnIndexOrThrow("quantity")));
            } while (getAddonQuantity.moveToNext());
            getAddonQuantity.close();
        }
    }

    public void getAddon(String orderAddonId) {
        Cursor getAddon = databaseFunctions.getOrderAddonName(orderAddonId);
        getOrderAddonName = new ArrayList<>();

        if (getAddon != null && getAddon.moveToFirst()) {
            do {
                getOrderAddonName.add(getAddon.getString(getAddon.getColumnIndexOrThrow("addon")));
            } while (getAddon.moveToNext());
            getAddon.close();
        }
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