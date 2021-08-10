package com.example.myapplication.model;

public class Exercise {
    private String ename;
    private String category;
    private String description;
    private int calories;

    public Exercise() {

    }

    public Exercise(String ename, String category, String description, int calories) {
        this.ename = ename;
        this.category = category;
        this.description = description;
        this.calories = calories;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "ename='" + ename + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
