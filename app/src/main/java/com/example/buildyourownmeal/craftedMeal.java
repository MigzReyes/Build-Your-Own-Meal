package com.example.buildyourownmeal;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class craftedMeal extends AppCompatActivity {

    //VARIABLE DECLARATION
    private Dialog popUpLogInWarning;
    private Button backBtn, addBtn, plusBtn, minusBtn;
    private String getMealNameText;
    private TextView itemCount, karaage, sisig, veggie, corn, coleslaw, hashBrown, gravy, vinegar,
            soySauce, mochi, japFruitSand, water, coffeeJelly, cucumberLemon;
    private TextView karaagePrice, sisigPrice, veggiePrice, cornPrice, coleslawPrice, hashPrice, gravyPrice, vinegarPrice,
            soySaucePrice, mochiPrice, japFruitSandPrice, waterPrice, coffeeJellyPrice, cucumberLemonPrice;
    private int count = 1; // Initialize count to 1

    private LinearLayout preMadeMealTopSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crafted_meal);

        //SHARE PREFERENCES USER SESSION
        SharedPreferences userSession = getSharedPreferences("userSession", MODE_PRIVATE);
        String userRole = userSession.getString("role", "guest");

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

        //ADDON LAYOUT
        preMadeMealTopSec = findViewById(R.id.preMadeMealTopSec);
        preMadeMealTopSec.setVisibility(View.GONE);


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
                    Intent intent = new Intent(craftedMeal.this, cart.class);
                    startActivity(intent);
                }
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
        backBtn = findViewById(R.id.fabBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
}