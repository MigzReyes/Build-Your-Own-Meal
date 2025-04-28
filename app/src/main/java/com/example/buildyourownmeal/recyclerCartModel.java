package com.example.buildyourownmeal;

import java.util.ArrayList;

public class recyclerCartModel {
    private int cartItemImg, cartItemPrice;
    private ArrayList<String> cartItemName;


    public recyclerCartModel(int cartItemImg, int cartItemPrice, ArrayList<String> cartItemName) {
        this.cartItemImg = cartItemImg;
        this.cartItemPrice = cartItemPrice;
        this.cartItemName = cartItemName;
    }

    public int getCartItemImg() {
        return cartItemImg;
    }

    public int getCartItemPrice() {
        return cartItemPrice;
    }

    public ArrayList<String> getCartItemName() {
        return cartItemName;
    }
}
