package com.example.myapplication;

public class todayTaskItem {
    private String todayTaskName;
    private Boolean todayTaskCheck;

    public todayTaskItem(String input1, Boolean input2){
        todayTaskName = input1;
        todayTaskCheck = input2;
    }

    public String getTodayTaskName() {
        return todayTaskName;
    }
    public Boolean getTodayTaskCheck() {
        return todayTaskCheck;
    }




}
