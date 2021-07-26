package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView displayDate, displayTime;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private todayTaskAdapter mAdapter;
    private ArrayList<workoutItem> todayTaskList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayDateAndTime();
        fillRecyclerView();
        buildRecyclerView();
    }


    public void displayDateAndTime(){
        String currentDateString = new SimpleDateFormat("MMM d, yyyy").format(new Date());
        String currentTimeString = new SimpleDateFormat("h:mm a").format(new Date());
        displayDate = findViewById(R.id.displayDate);
        displayTime = findViewById(R.id.displayTime);
        displayDate.setText(currentDateString);
        displayTime.setText(currentTimeString);
    }

    // subject to change
    public void fillRecyclerView(){
        todayTaskList.add(0, new workoutItem("Swim", Boolean.FALSE, null, 100));
        todayTaskList.add(0, new workoutItem("Push ups", Boolean.FALSE, null, 100));
        todayTaskList.add(0, new workoutItem("Dance", Boolean.FALSE, null, 100));
        todayTaskList.add(0, new workoutItem("Jump", Boolean.FALSE, null, 100));
        todayTaskList.add(0, new workoutItem("Basketball", Boolean.FALSE, null, 100));
        todayTaskList.add(0, new workoutItem("Football", Boolean.FALSE, null, 100));
        todayTaskList.add(0, new workoutItem("Tennis", Boolean.FALSE, null, 100));

    }

    public void buildRecyclerView(){
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.todayListRecycler);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new todayTaskAdapter(todayTaskList);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        };
        mAdapter.setOnItemClickListener(itemClickListener);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }




    public void openRunningTracker(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openNewPlan(View view) {
        Intent intent = new Intent(this, CreateNewPlanActivity.class);
        startActivity(intent);
    }

}