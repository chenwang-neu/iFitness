package com.example.myapplication.newplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.current_schedule.ScheduledPlanActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Exercise;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewPlanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ArrayList<DayItem> dayItemArrayList;
    private DayItem mon, tue, wed, thurs, fri, sat, sun;
    private ArrayList<WorkoutItem> workoutItemArrayList;
    private WorkoutItem workoutItem1, workoutItem2, workoutItem3, workoutItem4;
    private Integer currentCalories;
    private TextView calTextView;
    private HashMap<String, String> confirmedNewPlan;
    private ArrayList<WorkoutCategoryItem> workoutCategoryArrayList;
    private CategoryAdapter catAdapter;


    //test firebase
    DatabaseReference databaseReference;

    private List<String> spinnerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_plan);
        initializeDefault();
        calTextView = findViewById(R.id.calNumTextview);
        //initSpinner();

        //use firebase show on spinner

        Spinner workoutCategorySpinner = findViewById(R.id.workoutCategorySpinner);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("exercises");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                WorkoutCategoryItem workoutCategoryItem;

                spinnerList.clear();
                workoutCategoryArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    //add category to spinner list
                    String category = exercise.getCategory();

                    if (!spinnerList.contains(category)) {
                        spinnerList.add(category);
                        if (category.equals("Chests")) {
                            workoutCategoryItem = new WorkoutCategoryItem(category, R.drawable.abs);
                            workoutCategoryArrayList.add(workoutCategoryItem);
                        } else if (category.equals("Backs")) {
                            workoutCategoryItem = new WorkoutCategoryItem(category, R.drawable.back);
                            workoutCategoryArrayList.add(workoutCategoryItem);
                        } else if (category.equals("Shoulders")) {
                            workoutCategoryItem = new WorkoutCategoryItem(category, R.drawable.arm);
                            workoutCategoryArrayList.add(workoutCategoryItem);
                        } else if(category.equals("Legs")) {
                            workoutCategoryItem = new WorkoutCategoryItem(category, R.drawable.leg);
                            workoutCategoryArrayList.add(workoutCategoryItem);
                        }
                    }

                    //add same category's exercise
                    if (category.equals("Legs")) {
                        Log.d("Legs exerise !!!!!!!!!!!!!!!!", exercise.getEname());
                        count++;

                    }

                }
                Log.d("count is !!!!!!!!!!!!!!!!", String.valueOf(count));

                catAdapter = new CategoryAdapter(NewPlanActivity.this, workoutCategoryArrayList);
                workoutCategorySpinner.setAdapter(catAdapter);
                workoutCategorySpinner.setOnItemSelectedListener(NewPlanActivity.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    private void initSpinner(){
//        Spinner workoutCategorySpinner = findViewById(R.id.workoutCategorySpinner);
//        catAdapter = new CategoryAdapter(this, workoutCategoryArrayList);
//        workoutCategorySpinner.setAdapter(catAdapter);
//        workoutCategorySpinner.setOnItemSelectedListener(this);
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        WorkoutCategoryItem selectedCategory = (WorkoutCategoryItem) parent.getItemAtPosition(position);
        String category = selectedCategory.getCategoryName().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("exercises");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Exercise> categoryExercise = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    if (exercise.getCategory().equals(category)) {
//                        String ename = exercise.getEname();
//                        workoutItem1.getBtn().setText(ename);
                        categoryExercise.add(exercise);
                    }
                }
                workoutItem1.getBtn().setText(categoryExercise.get(0).getEname());
                workoutItem2.getBtn().setText(categoryExercise.get(1).getEname());
                workoutItem3.getBtn().setText(categoryExercise.get(2).getEname());
                workoutItem4.getBtn().setText(categoryExercise.get(3).getEname());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    private void init(Bundle savedInstanceState) { // called in onCreate()
        onRestoreInstanceState(savedInstanceState);
    }

    // handles screen rotation
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("calories", currentCalories);
        for (int i = 0; i < 7; i++){
            if (dayItemArrayList.get(i).getStatus() == Boolean.FALSE){
                outState.putInt("weekdayRecord"+i, 0);

            } else {
                outState.putInt("weekdayRecord"+i, 1);
            }
        }
        for (int i = 0; i < 3; i++){
            if (workoutItemArrayList.get(i).getStatus() == Boolean.FALSE){
                outState.putInt("workoutRecord"+i, 0);
            } else {
                outState.putInt("workoutRecord"+i, 1);
            }
        }
        super.onSaveInstanceState(outState);
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null){
            currentCalories = savedInstanceState.getInt("calories");
            for (int i = 0; i < 7; i++){
                if (savedInstanceState.getInt("weekdayRecord"+i) == 0){
                    dayItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);

                } else{
                    dayItemArrayList.get(i).changeSelectionStatus(Boolean.TRUE);
                    dayItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#002952"));
                }
            }
            for (int i = 0; i < 3; i++){
                if (savedInstanceState.getInt("workoutRecord"+i) == 0){
                    workoutItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);

                } else{
                    workoutItemArrayList.get(i).changeSelectionStatus(Boolean.TRUE);
                    workoutItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#002952"));
                }
            }
        }
    }


    public void initializeDefault(){
        currentCalories = 0;
        mon = new DayItem("Monday", Boolean.FALSE, findViewById(R.id.mondayBtn));
        tue = new DayItem("Tuesday", Boolean.FALSE, findViewById(R.id.tuesdayBtn));
        wed = new DayItem("Wednesday", Boolean.FALSE, findViewById(R.id.wednesdayBtn));
        thurs = new DayItem("Thursday", Boolean.FALSE, findViewById(R.id.thursdayBtn));
        fri = new DayItem("Friday", Boolean.FALSE, findViewById(R.id.fridayBtn));
        sat = new DayItem("Saturday", Boolean.FALSE, findViewById(R.id.saturdayBtn));
        sun = new DayItem("Sunday", Boolean.FALSE, findViewById(R.id.sundayBtn));

        dayItemArrayList = new ArrayList<DayItem>();
        dayItemArrayList.add(mon);
        dayItemArrayList.add(tue);
        dayItemArrayList.add(wed);
        dayItemArrayList.add(thurs);
        dayItemArrayList.add(fri);
        dayItemArrayList.add(sat);
        dayItemArrayList.add(sun);


        // subject to change
        workoutItem1 = new WorkoutItem("Workout1", Boolean.FALSE,
                findViewById(R.id.defaultWorkout1), 800, "famous activity");
        workoutItem2 = new WorkoutItem("Workout3", Boolean.FALSE,
                findViewById(R.id.defaultWorkout2),
                1200, "famous activity");
        workoutItem3 = new WorkoutItem("Workout2", Boolean.FALSE,
                findViewById(R.id.defaultWorkout3), 500, "famous activity");
        workoutItem4 = new WorkoutItem("Workout4", Boolean.FALSE,
                findViewById(R.id.defaultWorkout4),
                1200, "famous activity");

        workoutItemArrayList = new ArrayList<WorkoutItem>();
        workoutItemArrayList.add(workoutItem1);
        workoutItemArrayList.add(workoutItem2);
        workoutItemArrayList.add(workoutItem3);
        workoutItemArrayList.add(workoutItem4);


        workoutCategoryArrayList = new ArrayList<>();
        workoutCategoryArrayList.add(new WorkoutCategoryItem("Chest", R.drawable.abs));
        workoutCategoryArrayList.add(new WorkoutCategoryItem("Back", R.drawable.back));
        workoutCategoryArrayList.add(new WorkoutCategoryItem("Arm", R.drawable.arm));
        workoutCategoryArrayList.add(new WorkoutCategoryItem("Leg", R.drawable.leg));
    }

    // deal with week day selection
    public void onSelectedWeekdayAction(DayItem day){
        if (day.getStatus() == Boolean.FALSE){
            day.getBtn().setBackgroundColor(Color.parseColor("#002952"));
            day.changeSelectionStatus(Boolean.TRUE);
        } else {
            day.getBtn().setBackgroundColor(Color.parseColor("#637daf"));
            day.changeSelectionStatus(Boolean.FALSE);
        }
    }

    // deal with work out selection
    public void onSelectedWorkoutAction(WorkoutItem workout){
        if (workout.getStatus() == Boolean.FALSE){
            workout.getBtn().setBackgroundColor(Color.parseColor("#002952"));
            currentCalories = currentCalories + workout.getCal();
            calTextView.setText(String.valueOf(currentCalories));
            workout.changeSelectionStatus(Boolean.TRUE);
        } else {
            workout.getBtn().setBackgroundColor(Color.parseColor("#637daf"));
            currentCalories = currentCalories - workout.getCal();
            calTextView.setText(String.valueOf(currentCalories));
            workout.changeSelectionStatus(Boolean.FALSE);
        }
    }

    public void onButtonClick(View view){
        switch (view.getId()){
            case R.id.mondayBtn:
                onSelectedWeekdayAction(mon);
                break;
            case R.id.tuesdayBtn:
                onSelectedWeekdayAction(tue);
                break;
            case R.id.wednesdayBtn:
                onSelectedWeekdayAction(wed);
                break;
            case R.id.thursdayBtn:
                onSelectedWeekdayAction(thurs);
                break;
            case R.id.fridayBtn:
                onSelectedWeekdayAction(fri);
                break;
            case R.id.saturdayBtn:
                onSelectedWeekdayAction(sat);
                break;
            case R.id.sundayBtn:
                onSelectedWeekdayAction(sun);
                break;
            case R.id.defaultWorkout1:
                onSelectedWorkoutAction(workoutItem1);
                break;
            case R.id.defaultWorkout2:
                onSelectedWorkoutAction(workoutItem2);
                break;
            case R.id.defaultWorkout3:
                onSelectedWorkoutAction(workoutItem3);
                break;
        }
    }



    public void clearBtnAction(View view){
        for (int i = 0; i < 7; i++){
            dayItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);
            dayItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#637daf"));
        }
        for (int i = 0; i < 3; i++){
            workoutItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);
            workoutItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#637daf"));
        }
        currentCalories = 0;
        calTextView.setText(String.valueOf(currentCalories));
    }

    public void confirmBtnAction(View view){
        confirmedNewPlan = new HashMap<String, String>(); // hashmap
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 3; j++){
                if (dayItemArrayList.get(i).getStatus() && workoutItemArrayList.get(j).getStatus()){
                    confirmedNewPlan.put(dayItemArrayList.get(i).getWeekday(),
                            workoutItemArrayList.get(j).getWorkoutName());
                }
            }
        }
        clearBtnAction(view);
    }



    //
    public void openToday(View view) {
        Intent intent = new Intent(this, ScheduledPlanActivity.class);
        startActivity(intent);
    }




    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}