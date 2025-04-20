package com.example.buildyourownmeal;


public class recyclerCheckoutModel {
    private int checkoutMealImg;
    private String checkoutMealName, checkoutMealMainDish, checkoutMealSideDish, checkoutSauces, checkoutDesserts, checkoutDrinks;

    public recyclerCheckoutModel(int checkoutMealImg, String checkoutMealName, String checkoutMealMainDish, String checkoutMealSideDish, String checkoutSauces, String checkoutDesserts, String checkoutDrinks) {
        this.checkoutMealImg = checkoutMealImg;
        this.checkoutMealName = checkoutMealName;
        this.checkoutMealMainDish = checkoutMealMainDish;
        this.checkoutMealSideDish = checkoutMealSideDish;
        this.checkoutSauces = checkoutSauces;
        this.checkoutDesserts = checkoutDesserts;
        this.checkoutDrinks = checkoutDrinks;
    }

    public int getCheckoutMealImg() {
        return checkoutMealImg;
    }

    public String getCheckoutMealName() {
        return checkoutMealName;
    }

    public String getCheckoutMealMainDish() {
        return checkoutMealMainDish;
    }

    public String getCheckoutMealSideDish() {
        return checkoutMealSideDish;
    }

    public String getCheckoutSauces() {
        return checkoutSauces;
    }

    public String getCheckoutDesserts() {
        return checkoutDesserts;
    }

    public String getCheckoutDrinks() {
        return checkoutDrinks;
    }
}
