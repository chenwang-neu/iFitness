package com.example.myapplication;

import android.widget.Button;

public class dayItem {
    public String weekday;
    public Boolean selected;
    public Button btn;



    public dayItem(String input1, Boolean input2, Button input3){
        weekday = input1;
        selected = input2;
        btn = input3;

    }


    public String getWeekday(){
        return weekday;
    }
    public Boolean getStatus() {
        return selected;
    }
    public Button getBtn() {
        return btn;
    }



    public void changeSelectionStatus(Boolean newstatus){
        this.selected = newstatus;
    }

}
