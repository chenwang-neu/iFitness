package com.example.myapplication.newplan;

import android.widget.Button;

public class WorkoutItem {
    public String workoutName;
    public Boolean selected;
    public Button btn;
    public int cal;
    public String description;

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkoutItem(String input1, Boolean input2, Button input3, int input4, String input5){
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
