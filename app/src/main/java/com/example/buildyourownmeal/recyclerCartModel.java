package com.example.buildyourownmeal;

public class recyclerCartModel {
    private int cartItemImg;
    private String cartItemName, cartItemPrice;


    public recyclerCartModel(int cartItemImg, String cartItemPrice, String cartItemName) {
        this.cartItemImg = cartItemImg;
        this.cartItemPrice = cartItemPrice;
        this.cartItemName = cartItemName;
    }

    public int getCartItemImg() {
        return cartItemImg;
    }

    public String getCartItemPrice() {
        return cartItemPrice;
    }

    public String getCartItemName() {
        return cartItemName;
    }
}
