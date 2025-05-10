package com.example.buildyourownmeal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class adminAddMenuAddon extends AppCompatActivity {

    //DATABASE
    databaseFunctions databaseFunctions;

    //VARIABLES
    private Button cancelBtn, addBtn;
    private Spinner addonCategory;
    private EditText addonName, addonPrice;
    private TextView addonImg;
    private Bitmap bitAddonImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_menu_addon);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //REFERENCE
        cancelBtn = findViewById(R.id.cancelBtn);
        addBtn = findViewById(R.id.addBtn);
        addonCategory = findViewById(R.id.addonCategory);
        addonName = findViewById(R.id.addonName);
        addonPrice = findViewById(R.id.addonPrice);
        addonImg = findViewById(R.id.addonImg);

        //DROPDOWN CATEGORY
        List<String> addonDropdown = Arrays.asList("Rice", "Main Dish", "Sides", "Sauces", "Desserts", "Drinks");
        dropdownAdapter dropdownAdapter = new dropdownAdapter(this, R.layout.custom_spinner_bg, addonDropdown);
        dropdownAdapter.setDropDownViewResource(R.layout.custom_dropdown_bg);
        addonCategory.setAdapter(dropdownAdapter);

        ActivityResultLauncher<String> getImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        String imageUri = String.valueOf(o);
                        addonImg.setText(imageUri);
                        try {
                            downloadImg(o);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

        addonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage.launch("image/*");
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getCategory = addonCategory.getSelectedItem().toString().trim().toLowerCase();
                String getAddonName = addonName.getText().toString().trim();
                String getAddonPrice = addonPrice.getText().toString().trim();
                String getAddonImgUri = addonImg.getText().toString().trim();
                int intPrice = Integer.parseInt(getAddonPrice);

                switch (getCategory) {
                    case "rice":
                        boolean insertRiceAddon = databaseFunctions.insertAdminAddonData("rice", bitAddonImg, getAddonName, intPrice, getAddonImgUri, "rice");

                        if (insertRiceAddon) {
                            intentMenu();
                        }
                        break;
                    case "main dish":
                        boolean insertMainDishAddon = databaseFunctions.insertAdminAddonData("main_dish", bitAddonImg, getAddonName, intPrice, getAddonImgUri, "main dish");

                        if (insertMainDishAddon) {
                            intentMenu();
                        }
                        break;
                    case "sides":
                        boolean insertSideAddon = databaseFunctions.insertAdminAddonData("side_dish", bitAddonImg, getAddonName, intPrice, getAddonImgUri, "sides");

                        if (insertSideAddon) {
                            intentMenu();
                        }
                        break;
                    case "sauces":
                        boolean insertSauceAddon = databaseFunctions.insertAdminAddonData("sauce", bitAddonImg, getAddonName, intPrice, getAddonImgUri, "sauces");

                        if (insertSauceAddon) {
                            intentMenu();
                        }
                        break;
                    case "desserts":
                        boolean insertDessertAddon = databaseFunctions.insertAdminAddonData("dessert", bitAddonImg, getAddonName, intPrice, getAddonImgUri, "desserts");

                        if (insertDessertAddon) {
                            intentMenu();
                        }
                        break;
                    case "drinks":
                        boolean insertDrinkAddon = databaseFunctions.insertAdminAddonData("drink", bitAddonImg, getAddonName, intPrice, getAddonImgUri, "drinks");

                        if (insertDrinkAddon) {
                            intentMenu();
                        }
                        break;
                }
            }
        });


    }

    private void intentMenu() {
        Intent intent = new Intent(adminAddMenuAddon.this, adminMenu.class);
        startActivity(intent);
        finish();
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
        this.bitAddonImg = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUrl), null, uri2);
    }
}