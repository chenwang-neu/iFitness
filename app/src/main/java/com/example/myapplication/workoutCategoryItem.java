package com.example.myapplication;

public class workoutCategoryItem {
    public String categoryName;
    public int categoryImg;

    public workoutCategoryItem(String input1, int input2){
        categoryName = input1;
        categoryImg = input2;
    }

    public String getCategoryName(){ return categoryName;}

    public int getCategoryImg(){ return categoryImg;}


}
