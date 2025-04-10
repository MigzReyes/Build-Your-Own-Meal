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
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class craftedMeal extends AppCompatActivity {

    //VARIABLE DECLARATION
    private Button addBtn, plusBtn, minusBtn;
    private TextView itemCount;
    private String getMealNameText;
    private TextView karaage, sisig, veggie, corn, coleslaw, hashBrown, gravy, vinegar,
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

        //ORDER BTN
        addBtn = findViewById(R.id.addBtn);

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
                Intent intent = new Intent(craftedMeal.this, cart.class);
                startActivity(intent);
            }
        });
        /*addBtn.setOnClickListener(new View.OnClickListener() {
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
        });    */


        //ITEM COUNT MENU
        /*plusBtn = findViewById(R.id.plusBtn);
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
        });*/

        //BACK BUTTON
        CardView backBtn = findViewById(R.id.fabBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackPress();
            }
        });

    }

    /*public void addMinusQuantity(TextView idMinusBtn, TextView idQuantityCount, TextView idPlusBtn) {
        //REFERENCE
        minusBtn = findViewById(R.id.idMinusBtn);


    } */


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