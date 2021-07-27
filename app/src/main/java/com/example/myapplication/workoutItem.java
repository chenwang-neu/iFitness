package com.example.myapplication;

import android.widget.Button;

public class workoutItem {
    public String workoutName;
    public Boolean selected;
    public Button btn;
    public int cal;
    public String description;

    public workoutItem(String input1, Boolean input2, Button input3, int input4, String input5){
        workoutName = input1;
        selected = input2;
        btn = input3;
        cal = input4;
        description = input5;
    }


    public String getWorkoutName(){return workoutName;}
    public Boolean getStatus() {
        return selected;
    }
    public Button getBtn() {
        return btn;
    }
    public int getCal(){return cal;}
    public String getDescription(){return description;}

    public void changeSelectionStatus(Boolean newstatus){
        this.selected = newstatus;
    }
}
