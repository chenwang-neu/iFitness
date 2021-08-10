package com.example.myapplication.current_schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.RunningTrackerActivity;
import com.example.myapplication.model.Calendar;
import com.example.myapplication.model.Exercise;
import com.example.myapplication.newplan.DayItem;
import com.example.myapplication.newplan.NewPlanActivity;
import com.example.myapplication.newplan.WorkoutItem;
import com.example.myapplication.service.DataBaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduledPlanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DayItem mon, tue, wed, thurs, fri, sat, sun;
    public TextView displayDate, displayTime, dialogName, dialogDescription, dialogCal;
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public TaskRecycleViewAdapter mAdapter;
    public ArrayList<WorkoutItem> todayTaskList;
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public Button gotItBtn;
    public ImageButton infoButton;
    private ArrayList<DayItem> weekdayItemList;
    private DayItemAdapter dayAdapter;



    //chou test here for database
    List<Exercise> exerciseList = new ArrayList<>();
    //List<Calendar> selectedDayExerciseList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_plan);
        displayDateAndTime();
        initSpinner();
        // to be added - change checkbox status by database info.
        // handle screen rotation
        //chou test here for database
        //Log.d("The exerciselist at 0 is:", String.valueOf(exerciseList.size()));

    }

    public void fillDayItem(){
        mon = new DayItem("MON", Boolean.FALSE, findViewById(R.id.mondayBtn));
        tue = new DayItem("TUES", Boolean.FALSE, findViewById(R.id.tuesdayBtn));
        wed = new DayItem("WED", Boolean.FALSE, findViewById(R.id.wednesdayBtn));
        thurs = new DayItem("THUR", Boolean.FALSE, findViewById(R.id.thursdayBtn));
        fri = new DayItem("FRI", Boolean.FALSE, findViewById(R.id.fridayBtn));
        sat = new DayItem("SAT", Boolean.FALSE, findViewById(R.id.saturdayBtn));
        sun = new DayItem("SUN", Boolean.FALSE, findViewById(R.id.sundayBtn));

        weekdayItemList = new ArrayList<DayItem>();
        weekdayItemList.add(mon);
        weekdayItemList.add(tue);
        weekdayItemList.add(wed);
        weekdayItemList.add(thurs);
        weekdayItemList.add(fri);
        weekdayItemList.add(sat);
        weekdayItemList.add(sun);
    }

    private void initSpinner(){
        fillDayItem();
        Spinner weekdaySpinner = findViewById(R.id.weekdaySpinner);
        dayAdapter = new DayItemAdapter(this, weekdayItemList);
        weekdaySpinner.setAdapter(dayAdapter);
        weekdaySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        todayTaskList = new ArrayList<>();
        List<String> selectedExerciseList = new ArrayList<>();
        List<Calendar> selectedDayExerciseList = new ArrayList<>();

        DayItem selectedDay = (DayItem) parent.getItemAtPosition(position);

        // get calendar list for selectedDay:
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ScheduledPlanActivity.this);

        selectedDayExerciseList = dataBaseHelper.getCalendarByDay(selectedDay.getWeekday());
        Log.d("getCalendarByDay is !!!!!!!!!!!!!!!", String.valueOf(selectedDayExerciseList.toString()));

        for (Calendar s : selectedDayExerciseList) {
            if (s.getCname().equals(selectedDay.getWeekday())) {
                selectedExerciseList.add(s.getEname());
            }
        }
        Log.d("Exercises list include !!!!!!!!!!!!!!!", String.valueOf(selectedExerciseList.toString()));


        //exercises for the day are in selectedExercisesList, then find them from firebase and show them in the frontpage
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("exercises");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class);

                    if (selectedExerciseList.contains(exercise.getEname())) {
                      //  Log.d("exercise content is !!!!!!!!!!!!!1:", exercise.toString());
//                        //add exercise to todayTaskList
//                        //todayTaskList.add(0, new WorkoutItem("Swim", Boolean.FALSE, null, 100,
////                "famous activity swim"));
                        String ename = exercise.getEname();
                        int calories = exercise.getCalories();
                        String description = exercise.getDescription();
                        WorkoutItem workoutItem = new WorkoutItem(ename, Boolean.FALSE, null, calories, description);
                        todayTaskList.add(workoutItem);
//                    }
                    }
                }
                buildRecyclerView();
                Log.d("The exerciseList is !!!!!!!!!!!!!!!!!!!!!!", String.valueOf(exerciseList.size()));
//                selectedExerciseList.clear();
//                exerciseList.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void displayDateAndTime(){
//        String currentDateString = new SimpleDateFormat("MMMM d yyyy").format(new Date());
        String currentDayInWeekString = new SimpleDateFormat("MMMM d, yyyy").format(new Date());
        displayDate = findViewById(R.id.displayDate);
//        displayTime = findViewById(R.id.displayWeekday);
        displayDate.setText(currentDayInWeekString);
//        displayTime.setText(currentDateString);
    }

    // subject to change
//    public void fillRecyclerView(){
//        todayTaskList.add(0, new WorkoutItem("Swim", Boolean.FALSE, null, 100,
//                "famous activity swim"));
//        todayTaskList.add(0, new WorkoutItem("Push ups", Boolean.FALSE, null, 100, "too difficult" +
//                " to me "));
//        todayTaskList.add(0, new WorkoutItem("Dance", Boolean.FALSE, null, 100, "i love dance!"));
//        todayTaskList.add(0, new WorkoutItem("Jump", Boolean.FALSE, null, 100, "jump 1000 times " +
//                "a day"));
//        todayTaskList.add(0, new WorkoutItem("Basketball", Boolean.FALSE, null, 100, "wish i " +
//                "could be taller"));
//        todayTaskList.add(0, new WorkoutItem("Football", Boolean.FALSE, null, 100, "run run run"));
//        todayTaskList.add(0, new WorkoutItem("Tennis", Boolean.FALSE, null, 100, "Tennis is a racket sport that can be played individually against a single opponent (singles) or between two teams of two players each (doubles). Each player uses a tennis racket that is strung with cord to strike a hollow rubber ball covered with felt over or around a net and into the opponent's court. "));
//    }

    public void buildRecyclerView(){
        //fillRecyclerView();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.todayListRecycler);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new TaskRecycleViewAdapter(todayTaskList);

        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                WorkoutItem currentItem = todayTaskList.get(position);
//                showDescriptionPage(currentItem);
            }

            @Override
            public void onInfoBtnClick(int position) {
                WorkoutItem currentItem = todayTaskList.get(position);
                showDescriptionPage(currentItem);
            }
        };

        mAdapter.setOnItemClickListener(itemClickListener);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    // click tennis -> show info.
    public void showDescriptionPage(WorkoutItem item){
        dialogBuilder = new AlertDialog.Builder (this);
        final View descriptionPage =
                getLayoutInflater().inflate(R.layout.workout_discription_popup,
                null);

        dialogName = descriptionPage.findViewById(R.id.popupWorkoutName);
        dialogDescription = descriptionPage.findViewById(R.id.popupWorkoutDescription);
        dialogCal = descriptionPage.findViewById(R.id.popupCal);
        gotItBtn = descriptionPage.findViewById(R.id.popupGotBtn);

        dialogName.setText(item.workoutName);
        dialogDescription.setText(item.description);
        dialogCal.setText(String.valueOf(item.cal) + " Cal.");

        dialogBuilder.setView(descriptionPage);
        dialog = dialogBuilder.create();
        dialog.show();

        gotItBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    public void openRunningTracker(View view) {
        Intent intent = new Intent(this, RunningTrackerActivity.class);
        startActivity(intent);
    }

    public void openNewPlan(View view) {
        Intent intent = new Intent(this, NewPlanActivity.class);
        startActivity(intent);
    }


}