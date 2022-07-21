package com.example.nutritional;

public class foodData {

    private String foodName;
    private String foodCalorieNumber;

    public foodData() {

    }

    // created getter and setter methods
    // for all our variables.
    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCalorieNumber() {
        return foodCalorieNumber;
    }
    public void setFoodCalorieNumber(String foodCalorieNumber) {
        this.foodCalorieNumber = foodCalorieNumber;
    }
}