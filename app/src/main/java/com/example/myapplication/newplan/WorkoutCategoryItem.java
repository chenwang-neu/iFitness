package com.example.myapplication.newplan;

public class WorkoutCategoryItem {
    public String categoryName;
    public int categoryImg;

    public WorkoutCategoryItem(String input1, int input2){
        categoryName = input1;
        categoryImg = input2;
    }

    public String getCategoryName(){ return categoryName;}

    public int getCategoryImg(){ return categoryImg;}


}
