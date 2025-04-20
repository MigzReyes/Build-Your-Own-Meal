package com.example.buildyourownmeal;

public class recyclerHomeCombosModel {
    public String comboMealName;
    public String comboMealDescription;
    public int comboMealImg;

    public recyclerHomeCombosModel(String comboMealName, String comboMealDescription, int comboMealImg) {
        this.comboMealName = comboMealName;
        this.comboMealDescription = comboMealDescription;
        this.comboMealImg = comboMealImg;
    }

    public String getComboMealDescription() {
        return comboMealDescription;
    }

    public String getComboMealName() {
        return comboMealName;
    }

    public int getComboMealImg() {
        return comboMealImg;
    }
}
