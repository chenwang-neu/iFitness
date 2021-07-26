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
    private Button button;
    private TextView displayDate, displayTime;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private todayTaskAdapter mAdapter;
    private ArrayList<todayTaskItem> todayTaskList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentDateString = new SimpleDateFormat("MMM d, yyyy").format(new Date());
        String currentTimeString = new SimpleDateFormat("h:mm a").format(new Date());
        displayDate = findViewById(R.id.displayDate);
        displayTime = findViewById(R.id.displayTime);
        displayDate.setText(currentDateString);
        displayTime.setText(currentTimeString);


        todayTaskList.add(0, new todayTaskItem("Swim", Boolean.FALSE));
        todayTaskList.add(0, new todayTaskItem("Push ups", Boolean.FALSE));
        todayTaskList.add(0, new todayTaskItem("Dance", Boolean.FALSE));
        todayTaskList.add(0, new todayTaskItem("Jump", Boolean.FALSE));
        todayTaskList.add(0, new todayTaskItem("Basketball", Boolean.FALSE));
        todayTaskList.add(0, new todayTaskItem("Football", Boolean.FALSE));
        todayTaskList.add(0, new todayTaskItem("Tennis", Boolean.FALSE));

        buildRecyclerView();
    }

    public void buildRecyclerView(){
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.todayListRecycler);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new todayTaskAdapter(todayTaskList);
//        ItemClickListener itemClickListener = new ItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//
//            }
//        };
//        mAdapter.setOnItemClickListener(itemClickListener);
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