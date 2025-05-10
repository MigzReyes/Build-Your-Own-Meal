package com.example.buildyourownmeal;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class adminMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //DATABASE
    private databaseFunctions databaseFunctions;

    private DrawerLayout drawerLayout;
    private Button addNewMealBtn;
    private RecyclerView riceRecyclerView, mainDishRecyclerView, sideRecyclerView, sauceRecyclerView, dessertRecyclerView, drinkRecyclerView;
    private ArrayList<String> riceUri, riceCategory, riceName, riceEdit, riceDelete,
                                mainDishUri, mainDishCategory, mainDishName, mainDishEdit, mainDishDelete,
                                sideUri, sideCategory, sideName, sideEdit, sideDelete,
                                sauceUri, sauceCategory, sauceName, sauceEdit, sauceDelete,
                                dessertUri, dessertCategory, dessertName, dessertEdit, dessertDelete,
                                drinkUri, drinkCategory, drinkName, drinkEdit, drinkDelete;

    private ArrayList<Integer>
            riceId, ricePrice,
            mainDishId, mainDishPrice,
            sideId, sidePrice,
            sauceId, saucePrice,
            dessertId, dessertPrice,
            drinkId, drinkPrice;
    private ArrayList<Bitmap> riceImg, mainDishImg, sideImg, sauceImg, dessertImg, drinkImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_menu);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //REFERENCE
        addNewMealBtn = findViewById(R.id.addNewMealBtn);

        //STATUS BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        //SIDEBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.adminMenu);
        NavigationView navigationView = findViewById(R.id.sidebar);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_sidebar, R.string.close_sidebar);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        toolbar.setTitleTextColor(getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //ADD A NEW MEAL BUTTON
        addNewMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminMenu.this, adminAddMenuAddon.class);
                startActivity(intent);
            }
        });

        //RECYCLER VIEW RICE
        riceRecyclerView = findViewById(R.id.riceRecyclerView);
        riceCategory = new ArrayList<>();
        riceUri = new ArrayList<>();
        riceId = new ArrayList<>();
        riceImg = new ArrayList<>();
        riceName = new ArrayList<>();
        ricePrice = new ArrayList<>();
        riceEdit = new ArrayList<>();
        riceDelete = new ArrayList<>();

        setUpRiceModel();

        riceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMenuAddonsAdapter riceAdapter = new recyclerViewMenuAddonsAdapter(adminMenu.this, riceImg, riceName, riceEdit, riceDelete, riceCategory, riceUri, ricePrice, riceId);
        riceRecyclerView.setAdapter(riceAdapter);



        //RECYCLER VIEW MAIN DISH
        mainDishRecyclerView = findViewById(R.id.mainDishRecyclerView);
        mainDishCategory = new ArrayList<>();
        mainDishUri = new ArrayList<>();
        mainDishId = new ArrayList<>();
        mainDishImg = new ArrayList<>();
        mainDishName = new ArrayList<>();
        mainDishPrice = new ArrayList<>();
        mainDishEdit = new ArrayList<>();
        mainDishDelete = new ArrayList<>();

        setUpMainDishModel();

        mainDishRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMenuAddonsAdapter mainDishAdapter = new recyclerViewMenuAddonsAdapter(adminMenu.this, mainDishImg, mainDishName, mainDishEdit, mainDishDelete, mainDishCategory, mainDishUri, mainDishPrice, mainDishId);
        mainDishRecyclerView.setAdapter(mainDishAdapter);



        //RECYCLER VIEW SIDE DISH
        sideRecyclerView = findViewById(R.id.sideRecyclerView);
        sideCategory = new ArrayList<>();
        sideUri = new ArrayList<>();
        sideId = new ArrayList<>();
        sideImg = new ArrayList<>();
        sideName = new ArrayList<>();
        sidePrice = new ArrayList<>();
        sideEdit = new ArrayList<>();
        sideDelete = new ArrayList<>();

        setUpSideDishModel();

        sideRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMenuAddonsAdapter sideDishAdapter = new recyclerViewMenuAddonsAdapter(adminMenu.this, sideImg, sideName, sideEdit, sideDelete, sideCategory, sideUri, sidePrice, sideId);
        sideRecyclerView.setAdapter(sideDishAdapter);



        //RECYCLER VIEW SAUCE
        sauceRecyclerView = findViewById(R.id.sauceRecyclerView);
        sauceCategory = new ArrayList<>();
        sauceUri = new ArrayList<>();
        sauceId = new ArrayList<>();
        sauceImg = new ArrayList<>();
        sauceName = new ArrayList<>();
        saucePrice = new ArrayList<>();
        sauceEdit = new ArrayList<>();
        sauceDelete = new ArrayList<>();

        setUpSauceModel();

        sauceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMenuAddonsAdapter sauceAdapter = new recyclerViewMenuAddonsAdapter(adminMenu.this, sauceImg, sauceName, sauceEdit, sauceDelete, sauceCategory, sauceUri, saucePrice, sauceId);
        sauceRecyclerView.setAdapter(sauceAdapter);



        //RECYCLER VIEW DESSERT
        dessertRecyclerView = findViewById(R.id.dessertRecyclerView);
        dessertCategory = new ArrayList<>();
        dessertUri = new ArrayList<>();
        dessertId = new ArrayList<>();
        dessertImg = new ArrayList<>();
        dessertName = new ArrayList<>();
        dessertPrice = new ArrayList<>();
        dessertEdit = new ArrayList<>();
        dessertDelete = new ArrayList<>();

        setUpDessertModel();

        dessertRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMenuAddonsAdapter dessertAdapter = new recyclerViewMenuAddonsAdapter(adminMenu.this, dessertImg, dessertName, dessertEdit, dessertDelete, dessertCategory, dessertUri, dessertPrice, dessertId);
        dessertRecyclerView.setAdapter(dessertAdapter);



        //RECYCLER VIEW DRINK
        drinkRecyclerView = findViewById(R.id.drinkRecyclerView);
        drinkCategory = new ArrayList<>();
        drinkUri = new ArrayList<>();
        drinkId = new ArrayList<>();
        drinkImg = new ArrayList<>();
        drinkName = new ArrayList<>();
        drinkPrice = new ArrayList<>();
        drinkEdit = new ArrayList<>();
        drinkDelete = new ArrayList<>();

        setUpDrinkModel();

        drinkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMenuAddonsAdapter drinkAdapter = new recyclerViewMenuAddonsAdapter(adminMenu.this, drinkImg, drinkName, drinkEdit, drinkDelete, drinkCategory, drinkUri, drinkPrice, drinkId);
        drinkRecyclerView.setAdapter(drinkAdapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.adminDashboard) {
            Intent intent = new Intent(this, admin.class);
            startActivity(intent);
        } else if (id == R.id.adminOrders) {
            Intent intent = new Intent(this, adminOrders.class);
            startActivity(intent);
        } else if (id == R.id.adminMeals) {
            Intent intent = new Intent(this, adminMeals.class);
            startActivity(intent);
        } else if (id == R.id.adminAccount) {
            Intent intent = new Intent(this, adminAccounts.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpRiceModel() {
        Cursor getAddonTable = databaseFunctions.getAddonTable("rice");

        if (getAddonTable.moveToFirst() && getAddonTable != null) {
            do {
                byte[] byteArray = getAddonTable.getBlob(getAddonTable.getColumnIndexOrThrow("img"));
                riceImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                riceName.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("name")));
                ricePrice.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("price")));
                riceEdit.add("Edit");
                riceDelete.add("Delete");
                riceId.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("riceId")));
                riceCategory.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("category")));
                riceUri.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("imgUri")));
            } while (getAddonTable.moveToNext());
            getAddonTable.close();
        }
    }

    private void setUpMainDishModel() {
        Cursor getAddonTable = databaseFunctions.getAddonTable("main_dish");

        if (getAddonTable.moveToFirst() && getAddonTable != null) {
            do {
                byte[] byteArray = getAddonTable.getBlob(getAddonTable.getColumnIndexOrThrow("img"));
                mainDishImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                mainDishName.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("name")));
                mainDishPrice.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("price")));
                mainDishEdit.add("Edit");
                mainDishDelete.add("Delete");
                mainDishId.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("mainDishId")));
                mainDishCategory.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("category")));
                mainDishUri.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("imgUri")));
            } while (getAddonTable.moveToNext());
            getAddonTable.close();
        }
    }

    private void setUpSideDishModel() {
        Cursor getAddonTable = databaseFunctions.getAddonTable("side_dish");

        if (getAddonTable.moveToFirst() && getAddonTable != null) {
            do {
                byte[] byteArray = getAddonTable.getBlob(getAddonTable.getColumnIndexOrThrow("img"));
                sideImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                sideName.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("name")));
                sidePrice.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("price")));
                sideEdit.add("Edit");
                sideDelete.add("Delete");
                sideId.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("sideDishId")));
                sideCategory.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("category")));
                sideUri.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("imgUri")));
            } while (getAddonTable.moveToNext());
            getAddonTable.close();
        }
    }

    private void setUpSauceModel() {
        Cursor getAddonTable = databaseFunctions.getAddonTable("sauce");

        if (getAddonTable.moveToFirst() && getAddonTable != null) {
            do {
                byte[] byteArray = getAddonTable.getBlob(getAddonTable.getColumnIndexOrThrow("img"));
                sauceImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                sauceName.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("name")));
                saucePrice.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("price")));
                sauceEdit.add("Edit");
                sauceDelete.add("Delete");
                sauceId.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("sauceId")));
                sauceCategory.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("category")));
                sauceUri.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("imgUri")));
            } while (getAddonTable.moveToNext());
            getAddonTable.close();
        }
    }

    private void setUpDessertModel() {
        Cursor getAddonTable = databaseFunctions.getAddonTable("dessert");

        if (getAddonTable.moveToFirst() && getAddonTable != null) {
            do {
                byte[] byteArray = getAddonTable.getBlob(getAddonTable.getColumnIndexOrThrow("img"));
                dessertImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                dessertName.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("name")));
                dessertPrice.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("price")));
                dessertEdit.add("Edit");
                dessertDelete.add("Delete");
                dessertId.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("dessertId")));
                dessertCategory.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("category")));
                dessertUri.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("imgUri")));
            } while (getAddonTable.moveToNext());
            getAddonTable.close();
        }
    }

    private void setUpDrinkModel() {
        Cursor getAddonTable = databaseFunctions.getAddonTable("drink");

        if (getAddonTable.moveToFirst() && getAddonTable != null) {
            do {
                byte[] byteArray = getAddonTable.getBlob(getAddonTable.getColumnIndexOrThrow("img"));
                drinkImg.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                drinkName.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("name")));
                drinkPrice.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("price")));
                drinkEdit.add("Edit");
                drinkDelete.add("Delete");
                drinkId.add(getAddonTable.getInt(getAddonTable.getColumnIndexOrThrow("drinkId")));
                drinkCategory.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("category")));
                drinkUri.add(getAddonTable.getString(getAddonTable.getColumnIndexOrThrow("imgUri")));
            } while (getAddonTable.moveToNext());
            getAddonTable.close();
        }
    }
}