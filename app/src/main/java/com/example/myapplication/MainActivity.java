package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public TextView displayDate, displayTime, dialogName, dialogDescription, dialogCal;
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public todayTaskAdapter mAdapter;
    public ArrayList<workoutItem> todayTaskList = new ArrayList<>();
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public Button gotitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDateAndTime();
        fillRecyclerView();
        buildRecyclerView();

        // to be added - change checkbox status by database info.
        // handle screen rotation

    }

    public void displayDateAndTime(){
        String currentDateString = new SimpleDateFormat("MMMM d yyyy").format(new Date());
        String currentDayInWeekString = new SimpleDateFormat("EEEE").format(new Date());
        displayDate = findViewById(R.id.displayDate);
        displayTime = findViewById(R.id.displayWeekday);
        displayDate.setText(currentDayInWeekString);
        displayTime.setText(currentDateString);
    }

    // subject to change
    public void fillRecyclerView(){
        todayTaskList.add(0, new workoutItem("Swim", Boolean.FALSE, null, 100,
                "famous activity swim"));
        todayTaskList.add(0, new workoutItem("Push ups", Boolean.FALSE, null, 100, "too difficult" +
                " to me "));
        todayTaskList.add(0, new workoutItem("Dance", Boolean.FALSE, null, 100, "i love dance!"));
        todayTaskList.add(0, new workoutItem("Jump", Boolean.FALSE, null, 100, "jump 1000 times " +
                "a day"));
        todayTaskList.add(0, new workoutItem("Basketball", Boolean.FALSE, null, 100, "wish i " +
                "could be taller"));
        todayTaskList.add(0, new workoutItem("Football", Boolean.FALSE, null, 100, "run run run"));
        todayTaskList.add(0, new workoutItem("Tennis", Boolean.FALSE, null, 100, "Tennis is a racket sport that can be played individually against a single opponent (singles) or between two teams of two players each (doubles). Each player uses a tennis racket that is strung with cord to strike a hollow rubber ball covered with felt over or around a net and into the opponent's court. "));
    }

    public void buildRecyclerView(){
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.todayListRecycler);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new todayTaskAdapter(todayTaskList);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                workoutItem currentItem = todayTaskList.get(position);
                showDescriptionPage(currentItem);
            }
        };
        mAdapter.setOnItemClickListener(itemClickListener);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    // click tennis -> show info.
    public void showDescriptionPage(workoutItem item){
        dialogBuilder = new AlertDialog.Builder (this);
        final View descriptionPage =
                getLayoutInflater().inflate(R.layout.workout_discription_popup,
                null);

        dialogName = descriptionPage.findViewById(R.id.popupWorkoutName);
        dialogDescription = descriptionPage.findViewById(R.id.popupWorkoutDescription);
        dialogCal = descriptionPage.findViewById(R.id.popupCal);
        gotitBtn = descriptionPage.findViewById(R.id.popupGotBtn);

        dialogName.setText(item.workoutName);
        dialogDescription.setText(item.description);
        dialogCal.setText(String.valueOf(item.cal) + " Cal.");

        dialogBuilder.setView(descriptionPage);
        dialog = dialogBuilder.create();
        dialog.show();

        gotitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    public void openRunningTracker(View view) {
        Intent intent = new Intent(this, activity_runningTracker.class);
        startActivity(intent);
    }

    public void openNewPlan(View view) {
        Intent intent = new Intent(this, CreateNewPlanActivity.class);
        startActivity(intent);
    }

}