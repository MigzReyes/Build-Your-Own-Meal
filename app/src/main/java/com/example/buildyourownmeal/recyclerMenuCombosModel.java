package com.example.buildyourownmeal;

import androidx.recyclerview.widget.RecyclerView;

public class recyclerMenuCombosModel {

    private String comboMealName, comboMealDescription;
    private int comboMealImg;

    public recyclerMenuCombosModel (String comboMealName, String comboMealDescription, int comboMealImg) {
        this.comboMealName = comboMealName;
        this.comboMealDescription = comboMealDescription;
        this.comboMealImg = comboMealImg;
    }

    public String getComboMealName() {
        return comboMealName;
    }

    public String getComboMealDescription() {
        return comboMealDescription;
    }

    public int getComboMealImg() {
        return comboMealImg;
    }
}
