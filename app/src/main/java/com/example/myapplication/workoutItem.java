package com.example.myapplication;

import android.widget.Button;

public class workoutItem {
    public String workoutName;
    public Boolean selected;
    public Button btn;
    public int cal;
    public workoutItem(String input1, Boolean input2, Button input3, int input4){
        workoutName = input1;
        selected = input2;
        btn = input3;
        cal = input4;
    }


    public String getWorkoutName(){return workoutName;}

    public Boolean getStatus() {
        return selected;
    }
    public Button getBtn() {
        return btn;
    }
    public int getCal(){return cal;}

    public void changeSelectionStatus(Boolean newstatus){
        this.selected = newstatus;
    }
}
