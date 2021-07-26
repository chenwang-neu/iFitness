package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class CreateNewPlanActivity extends AppCompatActivity {
    private Button monBtn, tueBtn, wedBtn, thursBtn, friBtn, satBtn, sunBtn, swimBtn, runBtn,
            pushupBtn;
    private ArrayList<Integer> selectedWeekdayBtn, selectedWorkoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_plan);
        // array list store selected weekday
        selectedWeekdayBtn = new ArrayList<Integer>();
        for (int i = 0; i < 7 ; i++) {
            selectedWeekdayBtn.add(0);
        }

        selectedWorkoutBtn = new ArrayList<Integer>();
        for (int i = 0; i < 3 ; i++) {
            selectedWorkoutBtn.add(0);
        }

    }
    // deal with week day selection
    public void onSelectedWeekdayAction(Button Btn, int idx){
        if (selectedWeekdayBtn.get(idx)== 0){
            Btn.setBackgroundColor(Color.parseColor("#002952"));
            selectedWeekdayBtn.set(idx, 1);
        } else {
            Btn.setBackgroundColor(Color.parseColor("#637daf"));
            selectedWeekdayBtn.set(idx, 0);
        }
    }

    public void onSelectedWorkoutAction(Button Btn, int idx){
        if (selectedWorkoutBtn.get(idx)== 0){
            Btn.setBackgroundColor(Color.parseColor("#002952"));
            selectedWorkoutBtn.set(idx, 1);
        } else {
            Btn.setBackgroundColor(Color.parseColor("#637daf"));
            selectedWorkoutBtn.set(idx, 0);
        }
    }

    public void onButtonClick(View view){
        switch (view.getId()){
            case R.id.mondayBtn:
                monBtn = findViewById(R.id.mondayBtn);
                onSelectedWeekdayAction(monBtn, 0);
                break;
            case R.id.tuesdayBtn:
                tueBtn = findViewById(R.id.tuesdayBtn);
                onSelectedWeekdayAction(tueBtn, 1);
                break;
            case R.id.wednesdayBtn:
                wedBtn = findViewById(R.id.wednesdayBtn);
                onSelectedWeekdayAction(wedBtn, 2);
                break;
            case R.id.thursdayBtn:
                thursBtn = findViewById(R.id.thursdayBtn);
                onSelectedWeekdayAction(thursBtn, 3);
                break;
            case R.id.fridayBtn:
                friBtn = findViewById(R.id.fridayBtn);
                onSelectedWeekdayAction(friBtn, 4);
                break;
            case R.id.saturdayBtn:
                satBtn = findViewById(R.id.saturdayBtn);
                onSelectedWeekdayAction(satBtn, 5);
                break;
            case R.id.sundayBtn:
                sunBtn = findViewById(R.id.sundayBtn);
                onSelectedWeekdayAction(sunBtn, 6);
                break;
            case R.id.defaultSwimBtn:
                swimBtn = findViewById(R.id.defaultSwimBtn);
                onSelectedWorkoutAction(swimBtn, 0);
                break;
            case R.id.defaultPushupBtn:
                pushupBtn = findViewById(R.id.defaultPushupBtn);
                onSelectedWorkoutAction(pushupBtn, 1);
                break;
            case R.id.defaultRunningBtn:
                runBtn = findViewById(R.id.defaultRunningBtn);
                onSelectedWorkoutAction(runBtn, 2);
                break;
        }
    }

    public void clearBtnAction(View view){

    }

    //
    public void openToday(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openRunningTracker(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}