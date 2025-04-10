package com.example.buildyourownmeal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class craftedMeal extends AppCompatActivity {

    //VARIABLE DECLARATION
    private Button addBtn, plusBtn, minusBtn;
    private TextView itemCount;
    private EditText mealName;
    private String getMealNameText;
    private CheckBox karaage, sisig, veggie, corn, coleslaw, hashBrown, gravy, vinegar,
            soySauce, mochi, japFruitSand, water, coffeeJelly, cucumberLemon;
    private TextView karaagePrice, sisigPrice, veggiePrice, cornPrice, coleslawPrice, hashPrice, gravyPrice, vinegarPrice,
            soySaucePrice, mochiPrice, japFruitSandPrice, waterPrice, coffeeJellyPrice, cucumberLemonPrice;
    private int count = 1; // Initialize count to 1
    private SharedPreferences menuItem;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crafted_meal);

        //NAME MEAL
        mealName = findViewById(R.id.mealName);
        getMealNameText = mealName.getText().toString().trim();

        //ORDER BTN
        addBtn = findViewById(R.id.addBtn);

        //CHECKBOX
        karaage = findViewById(R.id.checkBoxKaraage);
        sisig = findViewById(R.id.checkBoxSisig);
        veggie = findViewById(R.id.checkBoxVeggie);
        corn = findViewById(R.id.checkBoxCorn);
        coleslaw = findViewById(R.id.checkBoxColeslaw);
        hashBrown = findViewById(R.id.checkBoxHash);
        gravy = findViewById(R.id.checkBoxGravy);
        vinegar = findViewById(R.id.checkBoxVinegar);
        soySauce = findViewById(R.id.checkBoxSoySauce);
        mochi = findViewById(R.id.checkBoxMochi);
        japFruitSand = findViewById(R.id.checkBoxJapFruitSand);
        water = findViewById(R.id.checkBoxWater);
        coffeeJelly = findViewById(R.id.checkBoxCoffeeJelly);
        cucumberLemon = findViewById(R.id.checkBoxCucumberLemon);

        //PRICES
        karaagePrice = findViewById(R.id.karaagePrice);
        sisigPrice = findViewById(R.id.sisigPrice);
        veggiePrice = findViewById(R.id.veggiePrice);
        cornPrice = findViewById(R.id.cornPrice);
        coleslawPrice = findViewById(R.id.coleslawPrice);
        hashPrice = findViewById(R.id.hashPrice);
        gravyPrice = findViewById(R.id.gravyPrice);
        vinegarPrice = findViewById(R.id.vinegarPrice);
        soySaucePrice = findViewById(R.id.soySaucePrice);
        mochiPrice = findViewById(R.id.mochiPrice);
        japFruitSandPrice = findViewById(R.id.japFruitSandPrice);
        waterPrice = findViewById(R.id.waterPrice);
        coffeeJellyPrice = findViewById(R.id.coffeeJellyPrice);
        cucumberLemonPrice = findViewById(R.id.cucumberPrice);

        //SHARED PREFERENCE
        menuItem = getSharedPreferences("craftedMeal", MODE_PRIVATE);
        editor = menuItem.edit();

        //SHARED PREFERENCE LOG IN SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        boolean isUserLoggedIn = userSession.getBoolean("isUserLoggedIn", false);

        //LOGIC STATEMENT
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn) {
                    if (isAnyItemChecked()) {
                        saveSelectedItems();
                        Intent intent = new Intent(craftedMeal.this, Navbar.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(craftedMeal.this, getString(R.string.chooseAnAddOn), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(craftedMeal.this, getString(R.string.logInSignUpFirst), Toast.LENGTH_SHORT).show();
                }
            } 
        });


        //ITEM COUNT MENU
        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);
        itemCount = findViewById(R.id.itemCount);

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;

                itemCount.setText(String.valueOf(count));
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count--;
                }
                itemCount.setText(String.valueOf(count));
            }
        });

        //BACK BUTTOM
        FloatingActionButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackPress();
            }
        });

    }

    private boolean isAnyItemChecked() {
        return karaage.isChecked() || sisig.isChecked() || veggie.isChecked() || corn.isChecked() ||
                coleslaw.isChecked() || hashBrown.isChecked() || gravy.isChecked() || vinegar.isChecked() ||
                soySauce.isChecked() || mochi.isChecked() || japFruitSand.isChecked() || water.isChecked() ||
                coffeeJelly.isChecked() || cucumberLemon.isChecked();
    }

    private void saveSelectedItems() {
        StringBuilder selectedItems = new StringBuilder();
        double totalPrice = 0.0;

        if (karaage.isChecked()) {
            selectedItems.append(karaage.getText().toString()).append(", ");
            totalPrice += getPrice(karaagePrice);
        }
        if (sisig.isChecked()) {
            selectedItems.append(sisig.getText().toString()).append(", ");
            totalPrice += getPrice(sisigPrice);
        }
        if (veggie.isChecked()) {
            selectedItems.append(veggie.getText().toString()).append(", ");
            totalPrice += getPrice(veggiePrice);
        }
        if (corn.isChecked()) {
            selectedItems.append(corn.getText().toString()).append(", ");
            totalPrice += getPrice(cornPrice);
        }
        if (coleslaw.isChecked()) {
            selectedItems.append(coleslaw.getText().toString()).append(", ");
            totalPrice += getPrice(coleslawPrice);
        }
        if (hashBrown.isChecked()) {
            selectedItems.append(hashBrown.getText().toString()).append(", ");
            totalPrice += getPrice(hashPrice);
        }
        if (gravy.isChecked()) {
            selectedItems.append(gravy.getText().toString()).append(", ");
            totalPrice += getPrice(gravyPrice);
        }
        if (vinegar.isChecked()) {
            selectedItems.append(vinegar.getText().toString()).append(", ");
            totalPrice += getPrice(vinegarPrice);
        }
        if (soySauce.isChecked()) {
            selectedItems.append(soySauce.getText().toString()).append(", ");
            totalPrice += getPrice(soySaucePrice);
        }
        if (mochi.isChecked()) {
            selectedItems.append(mochi.getText().toString()).append(", ");
            totalPrice += getPrice(mochiPrice);
        }
        if (japFruitSand.isChecked()) {
            selectedItems.append(japFruitSand.getText().toString()).append(", ");
            totalPrice += getPrice(japFruitSandPrice);
        }
        if (water.isChecked()) {
            selectedItems.append(water.getText().toString()).append(", ");
            totalPrice += getPrice(waterPrice);
        }
        if (coffeeJelly.isChecked()) {
            selectedItems.append(coffeeJelly.getText().toString()).append(", ");
            totalPrice += getPrice(coffeeJellyPrice);
        }
        if (cucumberLemon.isChecked()) {
            selectedItems.append(cucumberLemon.getText().toString()).append(", ");
            totalPrice += getPrice(cucumberLemonPrice);
        }

        if (selectedItems.length() > 0) {
            String userSelectedItems = selectedItems.substring(0, selectedItems.length() - 2);
            editor.putBoolean("menuSession", true);
            editor.putString("selectedItems", userSelectedItems);
            editor.putInt("itemCount", count);
            editor.putString("mealName", getMealNameText);
            editor.putFloat("totalPrice", (float) totalPrice);

            editor.apply();
        }
    }

    private double getPrice(TextView priceView) {
        String priceText = priceView.getText().toString();
        return priceText.isEmpty() ? 0.0 : Double.parseDouble(priceText);
    }

    public void handleBackPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager();
        } else {
            finish();
        }
    }
}