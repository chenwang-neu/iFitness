package com.example.myapplication.current_schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.myapplication.running_tracker.RunningTrackerActivity;
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
    public TextView displayDate, displayCal, dialogName, dialogDescription, dialogCal;
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public TaskRecycleViewAdapter mAdapter;
    public ArrayList<WorkoutItem> todayTaskList = new ArrayList<>();
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public Button gotItBtn;
    public ImageButton infoButton;
    private ArrayList<DayItem> weekdayItemList;
    private DayItemAdapter dayAdapter;

    //chou test here for database
    List<Exercise> exerciseList = new ArrayList<>();
    private SQLiteDatabase mDatabase;
    DayItem selectedDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_plan);
        init();

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                Toast.makeText(ScheduledPlanActivity.this, "Delete an item", Toast.LENGTH_SHORT).show();
//                int position = viewHolder.getLayoutPosition();
//                todayTaskList.remove(position);
//                mAdapter.notifyItemRemoved(position);
//                // remove data from database
//            }
//        });
//        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void init() {
        displayDateAndTime();
        initSpinner();
        buildRecyclerView();
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

    public void displayTotalCal(){
        displayCal = findViewById(R.id.displayCalories);
        int total_cal = 0;
        for (int i = 0; i < todayTaskList.size(); i++){
            total_cal += todayTaskList.get(i).getCal();
        }
        displayCal.setText(total_cal + " Cal.");
    }

    public void displayDateAndTime(){
        @SuppressLint("SimpleDateFormat") String currentDayInWeekString = new SimpleDateFormat("MMMM d, yyyy").format(new Date());
        displayDate = findViewById(R.id.displayDate);
        displayDate.setText(currentDayInWeekString);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        List<String> selectedExerciseList = new ArrayList<>();
        List<Calendar> selectedDayExerciseList = new ArrayList<>();
//        DayItem selectedDay = (DayItem) parent.getItemAtPosition(position);
        selectedDay = (DayItem) parent.getItemAtPosition(position);
        // get calendar list for selectedDay:
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ScheduledPlanActivity.this);
        selectedDayExerciseList = dataBaseHelper.getCalendarByDay(selectedDay.getWeekday());

        for (Calendar s : selectedDayExerciseList) {
            if (s.getCname().equals(selectedDay.getWeekday())) {
                selectedExerciseList.add(s.getEname());
            }
        }

        // exercises for the day are in selectedExercisesList, then find them from firebase and
        // show them in the frontpage
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("exercises");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todayTaskList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    if (selectedExerciseList.contains(exercise.getEname())) {
                        String ename = exercise.getEname();
                        int calories = exercise.getCalories() * 60;
                        String description = exercise.getDescription();
                        WorkoutItem workoutItem = new WorkoutItem(ename, Boolean.FALSE, null, calories, description);
                        todayTaskList.add(workoutItem);
                        mAdapter.notifyItemInserted(0);
                    }
                }
                buildRecyclerView();
                displayTotalCal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }


    public void buildRecyclerView(){
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.todayListRecycler);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new TaskRecycleViewAdapter(todayTaskList);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                return;
            }

            @Override
            public void onInfoBtnClick(int position) {
                WorkoutItem currentItem = todayTaskList.get(position);
                showDescriptionPage(currentItem);
            }
        };

        mAdapter.setOnItemClickListener(itemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
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
        dialogCal.setText(item.cal + " Cal.");

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

    public void deleteAll(View view){
        for (int i = 0; i < todayTaskList.size(); i++){
            todayTaskList.get(i).changeSelectionStatus(Boolean.TRUE);
        }
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ScheduledPlanActivity.this);
        dataBaseHelper.deleteCalendarByDay(selectedDay.getWeekday());
        todayTaskList.clear();
        displayTotalCal();
        buildRecyclerView();
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