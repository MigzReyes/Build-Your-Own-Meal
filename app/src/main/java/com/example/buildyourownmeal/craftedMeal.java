package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class craftedMeal extends AppCompatActivity {

    //DATABASE
    databaseFunctions databaseFunctions;

    //VARIABLE DECLARATION
    private static final String MEAL_TYPE = "Crafted Meal";
    private Dialog popUpLogInWarning;
    private Button backBtn, addBtn, plusBtn, minusBtn;
    private String getMealNameText;
    private TextView itemCount, karaage, sisig, veggie, corn, coleslaw, hashBrown, gravy, vinegar,
            soySauce, mochi, japFruitSand, water, coffeeJelly, cucumberLemon;
    private TextView karaagePrice, sisigPrice, veggiePrice, cornPrice, coleslawPrice, hashPrice, gravyPrice, vinegarPrice,
            soySaucePrice, mochiPrice, japFruitSandPrice, waterPrice, coffeeJellyPrice, cucumberLemonPrice;
    private TextView addKaraage, addSisig, addVeggie, addCorn, addColeslaw, addHash, addGravy, addVinegar,
            addSoySauce, addMochi, addJapFruitSand, addWater, addCoffeeJelly, addCucumberLemon;
    private TextView minusKaraage, minusSisig, minusVeggie, minusCorn, minusColeslaw, minusHash, minusGravy, minusVinegar,
            minusSoySauce, minusMochi, minusJapFruitSand, minusWater, minusCoffeeJelly, minusCucumberLemon;
    private ImageView emptyBentoBox, trashKaraage, trashSisig, trashVeggie, trashCorn, trashColeslaw, trashHash, trashGravy, trashVinegar,
            trashSoySauce, trashMochi, trashJapFruitSand, trashWater, trashCoffeeJelly, trashCucumberLemon;
    private int quantityValue = 0 , quantityKaraage = 0, quantitySisig = 0, quantityVeggie = 0, quantityCorn = 0, quantityColeslaw = 0, quantityHash = 0,
                quantityGravy = 0, quantityVinegar = 0, quantitySoySauce = 0, quantityMochi = 0, quantityJapFruitSand = 0, quantityWater = 0,
                quantityCoffeeJelly = 0, quantityCucumberLemon = 0; // Initialize count to 1

    private LinearLayout preMadeMealTopSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crafted_meal);

        //DATABASE
        databaseFunctions = new databaseFunctions(this);

        //SHARE PREFERENCES USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        String userRole = userSession.getString("role", "guest");
        int userId = userSession.getInt("userId", 0);

        //ORDER BTN
        addBtn = findViewById(R.id.addBtn);

        //MEAL IMAGE
        emptyBentoBox = findViewById(R.id.emptyBentoBox);
        BitmapDrawable getBitmap = (BitmapDrawable) emptyBentoBox.getDrawable();
        Bitmap bitmap = getBitmap.getBitmap();


        //QUANTITY
        karaage = findViewById(R.id.quantityValueChickenKaraage);
        sisig = findViewById(R.id.quantityValueTunaSisig);
        veggie = findViewById(R.id.quantityValueVeggieBall);
        corn = findViewById(R.id.quantityValueCorn);
        coleslaw = findViewById(R.id.quantityValueColeslaw);
        hashBrown = findViewById(R.id.quantityValueHashBrown);
        gravy = findViewById(R.id.quantityValueGravy);
        vinegar = findViewById(R.id.quantityValueVinegar);
        soySauce = findViewById(R.id.quantityValueSoySauce);
        mochi = findViewById(R.id.quantityValueMochi);
        japFruitSand = findViewById(R.id.quantityValueJapFruitSand);
        water = findViewById(R.id.quantityValueWater);
        coffeeJelly = findViewById(R.id.quantityValueCoffeeJelly);
        cucumberLemon = findViewById(R.id.quantityValueCucumberLemon);

        //PLUS BUTTON
        addKaraage = findViewById(R.id.addBtnChickenKaraage);
        addSisig = findViewById(R.id.addBtnTunaSisig);
        addVeggie = findViewById(R.id.addBtnVeggieBall);
        addCorn = findViewById(R.id.addBtnCorn);
        addColeslaw = findViewById(R.id.addBtnColeslaw);
        addHash = findViewById(R.id.addBtnHashBrown);
        addGravy = findViewById(R.id.addBtnGravy);
        addVinegar = findViewById(R.id.addBtnVinegar);
        addSoySauce = findViewById(R.id.addBtnSoySauce);
        addMochi = findViewById(R.id.addBtnMochi);
        addJapFruitSand = findViewById(R.id.addBtnJapFruitSand);
        addWater = findViewById(R.id.addBtnWater);
        addCoffeeJelly = findViewById(R.id.addBtnCoffeeJelly);
        addCucumberLemon = findViewById(R.id.addBtnCucumberLemon);

        //MINUS BTN
        minusKaraage = findViewById(R.id.minusBtnChickenKaraage);
        minusSisig = findViewById(R.id.minusBtnTunaSisig);
        minusVeggie = findViewById(R.id.minusBtnVeggieBall);
        minusCorn = findViewById(R.id.minusBtnCorn);
        minusColeslaw = findViewById(R.id.minusBtnColeslaw);
        minusHash = findViewById(R.id.minusBtnHashBrown);
        minusGravy = findViewById(R.id.minusBtnGravy);
        minusVinegar = findViewById(R.id.minusBtnVinegar);
        minusSoySauce = findViewById(R.id.minusBtnSoySauce);
        minusMochi = findViewById(R.id.minusBtnMochi);
        minusJapFruitSand = findViewById(R.id.minusBtnJapFruitSand);
        minusWater = findViewById(R.id.minusBtnWater);
        minusCoffeeJelly = findViewById(R.id.minusBtnCoffeeJelly);
        minusCucumberLemon = findViewById(R.id.minusBtnCucumberLemon);

        //TRASH
        trashKaraage = findViewById(R.id.trashChickenKaraage);
        trashSisig = findViewById(R.id.trashTunaSisig);
        trashVeggie = findViewById(R.id.trashVeggieBall);
        trashCorn = findViewById(R.id.trashCorn);
        trashColeslaw = findViewById(R.id.trashColeslaw);
        trashHash = findViewById(R.id.trashHashBrown);
        trashGravy = findViewById(R.id.trashGravy);
        trashVinegar = findViewById(R.id.trashVinegar);
        trashSoySauce = findViewById(R.id.trashSoySauce);
        trashMochi = findViewById(R.id.trashMochi);
        trashJapFruitSand = findViewById(R.id.trashJapFruitSand);
        trashWater = findViewById(R.id.trashWater);
        trashCoffeeJelly = findViewById(R.id.trashCoffeeJelly);
        trashCucumberLemon = findViewById(R.id.trashCucumberLemon);

        //PRICES
        karaagePrice = findViewById(R.id.priceChickenKaraage);
        sisigPrice = findViewById(R.id.priceTunaSisig);
        veggiePrice = findViewById(R.id.priceVeggieBalls);
        cornPrice = findViewById(R.id.priceCorn);
        coleslawPrice = findViewById(R.id.priceColeslaw);
        hashPrice = findViewById(R.id.priceHashBrown);
        gravyPrice = findViewById(R.id.priceGravy);
        vinegarPrice = findViewById(R.id.priceVinegar);
        soySaucePrice = findViewById(R.id.priceSoySauce);
        mochiPrice = findViewById(R.id.priceMochi);
        japFruitSandPrice = findViewById(R.id.priceJapFruitSand);
        waterPrice = findViewById(R.id.priceWater);
        coffeeJellyPrice = findViewById(R.id.priceCoffeeJelly);
        cucumberLemonPrice = findViewById(R.id.priceCucumberLemon);

        //ADDON LAYOUT
        preMadeMealTopSec = findViewById(R.id.preMadeMealTopSec);
        preMadeMealTopSec.setVisibility(View.GONE);

        //ADDON QUANTITY
        addMinusQuantity(trashKaraage, minusKaraage, karaage, addKaraage);
        addMinusQuantity(trashSisig, minusSisig, sisig, addSisig);
        addMinusQuantity(trashVeggie, minusVeggie, veggie, addVeggie);
        addMinusQuantity(trashCorn, minusCorn, corn, addCorn);
        addMinusQuantity(trashColeslaw, minusColeslaw, coleslaw, addColeslaw);
        addMinusQuantity(trashHash, minusHash, hashBrown, addHash);
        addMinusQuantity(trashGravy, minusGravy, gravy, addGravy);
        addMinusQuantity(trashVinegar, minusVinegar, vinegar, addVinegar);
        addMinusQuantity(trashSoySauce, minusSoySauce, soySauce, addSoySauce);
        addMinusQuantity(trashMochi, minusMochi, mochi, addMochi);
        addMinusQuantity(trashJapFruitSand, minusJapFruitSand, japFruitSand, addJapFruitSand);
        addMinusQuantity(trashWater, minusWater, water, addWater);
        addMinusQuantity(trashCoffeeJelly, minusCoffeeJelly, coffeeJelly, addCoffeeJelly);
        addMinusQuantity(trashCucumberLemon, minusCucumberLemon, cucumberLemon, addCucumberLemon);


        //POP UP ALERT
        popUpLogInWarning = new Dialog(this);
        popUpLogInWarning.setContentView(R.layout.pop_up_login_signup_alert);
        popUpLogInWarning.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpLogInWarning.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
        popUpLogInWarning.setCancelable(true);

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
                    //GET TEXT
                    String getKaraage = karaage.getText().toString().trim();

                    //PARSE INT
                    int karaageInt = Integer.parseInt(getKaraage);

                    //PARSE PRICE INT

                        int ricePrice = databaseFunctions.getAddonPrice("rice");
                        String addonName = databaseFunctions.getAddonName("rice");
                        if (karaageInt >= 1) {
                            boolean insertAddonData = databaseFunctions.insertAddonData(userId, addonName, karaageInt, ricePrice);

                            if (insertAddonData) {
                                Cursor getAddonData = databaseFunctions.getAddonData(userId);

                                if (getAddonData != null && getAddonData.moveToFirst()) {
                                    int getAddonId = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("orderAddonId"));
                                    String addon = getAddonData.getString(getAddonData.getColumnIndexOrThrow("addon"));
                                    String quantity = getAddonData.getString(getAddonData.getColumnIndexOrThrow("quantity"));
                                    int price = getAddonData.getInt(getAddonData.getColumnIndexOrThrow("price"));

                                    boolean insertOrderData = databaseFunctions.insertOrderData(getAddonId, userId, bitmap, MEAL_TYPE, price);
                                    if (insertOrderData) {
                                        Intent intent = new Intent(craftedMeal.this, cart.class);
                                        startActivity(intent);
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

    public void addMinusQuantity(ImageView trashBtn, TextView minusBtn, TextView quantity, TextView plusBtn) {
        String quantityString = quantity.getText().toString().trim();
        final int[] quantityInt = {Integer.parseInt(quantityString)};

        String quantityText = quantity.getText().toString().trim();
        int quantityCount = Integer.parseInt(quantityText);

        if (quantityCount <= 1) {
            trashBtn.setVisibility(View.VISIBLE);
            trashBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantityInt[0] > 1) {
                        quantityInt[0]--;
                    }

                    quantity.setText(String.valueOf(quantityInt[0]));
                }
            });
        } else {
            minusBtn.setVisibility(View.VISIBLE);
            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantityInt[0] > 1) {
                        quantityInt[0]--;
                    }

                    quantity.setText(String.valueOf(quantityInt[0]));
                }
            });
        }

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityInt[0]++;

                quantity.setText(String.valueOf(quantityInt[0]));
            }
        });
    }


    private double getPrice(TextView priceView) {
        String priceText = priceView.getText().toString();
        return priceText.isEmpty() ? 0.0 : Double.parseDouble(priceText);
    }
}