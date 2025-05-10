package com.example.buildyourownmeal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class adminEditMenuAddon extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;
    //DATABASE
    databaseFunctions databaseFunctions;

    //VARIABLES
    private Button cancelBtn, editBtn;
    private EditText addonName, addonPrice;
    private TextView addonImg;
    private Bitmap bitAddonImg;
    private byte intentAddonImg;
    private String intentAddonName, intentAddonCategory, intentAddonUri;
    private int intentAddonPrice, intentAddonId;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_menu_addon);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //ADDON DATA
        Bitmap getAddonImg = databaseFunctions.getAddonImg("rice");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getAddonImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        byte byteImg = byteArray[1];
        intentAddonImg = getIntent().getByteExtra("addonImg", byteImg);
        intentAddonName = getIntent().getStringExtra("addonName");
        intentAddonPrice = getIntent().getIntExtra("addonPrice", 0);
        intentAddonId = getIntent().getIntExtra("addonId", 0);
        intentAddonCategory = getIntent().getStringExtra("addonCategory");
        intentAddonUri = getIntent().getStringExtra("addonUri");

        //REFERENCE
        cancelBtn = findViewById(R.id.cancelBtn);
        editBtn = findViewById(R.id.editBtn);
        addonName = findViewById(R.id.addonName);
        addonPrice = findViewById(R.id.addonPrice);
        addonImg = findViewById(R.id.addonImg);

        //SET ADDON DATA
        addonImg.setText(String.valueOf(intentAddonImg));
        addonName.setText(intentAddonName);
        addonPrice.setText(String.valueOf(intentAddonPrice));


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

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getAddonName = addonName.getText().toString().trim();
                String getAddonPrice = addonPrice.getText().toString().trim();
                int intPrice = Integer.parseInt(getAddonPrice);

                switch (intentAddonCategory) {
                    case "rice":
                        if (!addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddonNoImg("rice", "riceId" , intentAddonId, "rice", getAddonName, intPrice);

                            if (insertAddon) {
                                intentMenu();
                            }
                        } else if (addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddon("rice", "riceId", intentAddonId, "rice", getAddonName, intPrice, bitAddonImg);

                            if (insertAddon) {
                                intentMenu();
                            }
                        }

                        break;
                    case "main dish":
                        if (!addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddonNoImg("main_dish", "mainDishId" , intentAddonId, "main dish", getAddonName, intPrice);

                            if (insertAddon) {
                                intentMenu();
                            }
                        } else if (addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddon("main_dish", "mainDishId", intentAddonId, "main dish", getAddonName, intPrice, bitAddonImg);

                            if (insertAddon) {
                                intentMenu();
                            }
                        }
                        break;
                    case "sides":
                        if (!addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddonNoImg("side_dish", "sideDishId" , intentAddonId, "sides", getAddonName, intPrice);

                            if (insertAddon) {
                                intentMenu();
                            }
                        } else if (addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddon("side_dish", "sideDishId", intentAddonId, "sides", getAddonName, intPrice, bitAddonImg);

                            if (insertAddon) {
                                intentMenu();
                            }
                        }
                        break;
                    case "sauces":
                        if (!addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddonNoImg("sauce", "sauceId" , intentAddonId, "sauces", getAddonName, intPrice);

                            if (insertAddon) {
                                intentMenu();
                            }
                        } else if (addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddon("sauce", "sauceId", intentAddonId, "sauces", getAddonName, intPrice, bitAddonImg);

                            if (insertAddon) {
                                intentMenu();
                            }
                        }
                        break;
                    case "desserts":
                        if (!addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddonNoImg("dessert", "dessertId" , intentAddonId, "desserts", getAddonName, intPrice);

                            if (insertAddon) {
                                intentMenu();
                            }
                        } else if (addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddon("dessert", "dessertId", intentAddonId, "desserts", getAddonName, intPrice, bitAddonImg);

                            if (insertAddon) {
                                intentMenu();
                            }
                        }
                        break;
                    case "drinks":
                        if (!addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddonNoImg("drink", "drinkId" , intentAddonId, "drinks", getAddonName, intPrice);

                            if (insertAddon) {
                                intentMenu();
                            }
                        } else if (addonImg.getText().toString().trim().contains("content")) {
                            boolean insertAddon = databaseFunctions.updateAddon("drink", "drinkId", intentAddonId, "main dish", getAddonName, intPrice, bitAddonImg);

                            if (insertAddon) {
                                intentMenu();
                            }
                        }
                        break;
                }
            }
        });


    }

    private void intentMenu() {
        Intent intent = new Intent(adminEditMenuAddon.this, adminMenu.class);
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

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedUri = data.getData();
            if (selectedUri == null) return;

            final int takeFlags = data.getFlags() &
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            getContentResolver().takePersistableUriPermission(selectedUri, takeFlags);
        }
    }

}