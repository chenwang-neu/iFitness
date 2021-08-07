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
import android.widget.Toast;

import com.example.myapplication.current_schedule.ScheduledPlanActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Calendar;
import com.example.myapplication.model.Exercise;
import com.example.myapplication.service.DataBaseHelper;
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


    //days list and exercise list for days: if day not selected or exercises not selected, will throw someting
    List<String> dayLst = new ArrayList<>();
    List<String> exerciseLst = new ArrayList<>();
    List<Exercise> exeLst = new ArrayList<>();


    //total calories from use selected


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
                }
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
        clearSelectedWorkout(view);

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

                workoutItem1.setCal(categoryExercise.get(0).getCalories());
                workoutItem2.setCal(categoryExercise.get(1).getCalories());
                workoutItem3.setCal(categoryExercise.get(2).getCalories());
                workoutItem4.setCal(categoryExercise.get(3).getCalories());

                workoutItem1.setWorkoutName(categoryExercise.get(0).getEname());
                workoutItem2.setWorkoutName(categoryExercise.get(1).getEname());
                workoutItem3.setWorkoutName(categoryExercise.get(2).getEname());
                workoutItem4.setWorkoutName(categoryExercise.get(3).getEname());

                workoutItem1.setDescription(categoryExercise.get(0).getDescription());
                workoutItem2.setDescription(categoryExercise.get(1).getDescription());
                workoutItem3.setDescription(categoryExercise.get(2).getDescription());
                workoutItem4.setDescription(categoryExercise.get(3).getDescription());


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
        for (int i = 0; i < 4; i++){
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
            for (int i = 0; i < 4; i++){
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
        mon = new DayItem("MON", Boolean.FALSE, findViewById(R.id.mondayBtn));
        tue = new DayItem("TUES", Boolean.FALSE, findViewById(R.id.tuesdayBtn));
        wed = new DayItem("WED", Boolean.FALSE, findViewById(R.id.wednesdayBtn));
        thurs = new DayItem("THUR", Boolean.FALSE, findViewById(R.id.thursdayBtn));
        fri = new DayItem("FRI", Boolean.FALSE, findViewById(R.id.fridayBtn));
        sat = new DayItem("SAT", Boolean.FALSE, findViewById(R.id.saturdayBtn));
        sun = new DayItem("SUN", Boolean.FALSE, findViewById(R.id.sundayBtn));

        dayItemArrayList = new ArrayList<DayItem>();
        dayItemArrayList.add(mon);
        dayItemArrayList.add(tue);
        dayItemArrayList.add(wed);
        dayItemArrayList.add(thurs);
        dayItemArrayList.add(fri);
        dayItemArrayList.add(sat);
        dayItemArrayList.add(sun);


        // Initialize workoutitem
        workoutItem1 = new WorkoutItem("", Boolean.FALSE,
                findViewById(R.id.defaultWorkout1), -1, "");
        workoutItem2 = new WorkoutItem("", Boolean.FALSE,
                findViewById(R.id.defaultWorkout2),
                -1, "");
        workoutItem3 = new WorkoutItem("", Boolean.FALSE,
                findViewById(R.id.defaultWorkout3), -1, "");
        workoutItem4 = new WorkoutItem("", Boolean.FALSE,
                findViewById(R.id.defaultWorkout4),
                -1, "");

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
        String dayName = day.getWeekday();

        //if this means client choose this day, add it to the daylist
        if (day.getStatus() == Boolean.FALSE){
            day.getBtn().setBackgroundColor(Color.parseColor("#002952"));
            day.changeSelectionStatus(Boolean.TRUE);
            
            dayLst.add(dayName);

        } else {
            // unclick this day, remove this day from daylist
            day.getBtn().setBackgroundColor(Color.parseColor("#637daf"));
            day.changeSelectionStatus(Boolean.FALSE);

            int pos = dayLst.indexOf(dayName);
            if (pos >= 0) {
                dayLst.remove(pos);
            }

        }
    }

    // deal with work out selection
    public void onSelectedWorkoutAction(WorkoutItem workout){
        String exerciseName = workout.getWorkoutName();
        //if this means client choose this exercise, add it to exerciseList
        if (workout.getStatus() == Boolean.FALSE){
            exerciseLst.add(exerciseName);
            workout.getBtn().setBackgroundColor(Color.parseColor("#002952"));
                //
                //calories calcutor!!!!!!!!!!!!!!!!!!!
                //currentCalories = currentCalories + workout.getCal();

            currentCalories += workout.getCal();

            calTextView.setText(String.valueOf(currentCalories));
            workout.changeSelectionStatus(Boolean.TRUE);
        } else {
            //if this means unclick the exercise button, remove this exercise from the exerciseLst
            int pos = exerciseLst.indexOf(exerciseName);
            if (pos >= 0) {
                exerciseLst.remove(pos);
            }

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
            case R.id.defaultWorkout4:
                onSelectedWorkoutAction(workoutItem4);
                break;
        }
    }



    public void clearBtnAction(View view){
        //backend clear dayLst and exerciseLst
        dayLst.clear();
        exerciseLst.clear();


        //front end
        for (int i = 0; i < 7; i++){
            dayItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);
            dayItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#637daf"));
        }
        for (int i = 0; i < 4; i++){
            workoutItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);
            workoutItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#637daf"));
        }
        currentCalories = 0;
        calTextView.setText(String.valueOf(currentCalories));
    }

    public void confirmBtnAction(View view){
        //if click confirm button

        //1. add to calendar database

        if (dayLst.size() == 0 || exerciseLst.size() == 0) {
            //Toast.makeText(NewPlanActivity.this, "You have not select day or exercise yet. Please select first", Toast.LENGTH_SHORT);
            Toast.makeText(NewPlanActivity.this, "You have not select day or exercise yet. Please select first", Toast.LENGTH_SHORT).show();
            Log.d("can not be 0", "You have not select day or exercise yet. Please select first");
            return;
        }
        DataBaseHelper dataBaseHelper = new DataBaseHelper(NewPlanActivity.this);
        for (String day : dayLst) {
            for (String exer : exerciseLst) {
                //1. create calendar class
                Calendar calendar = new Calendar(-1, day, exer);

                //2. add calendar to calendar database

                dataBaseHelper.addOneCalendar(calendar);
            }
        }


        //test calendar database
        Log.d("The calendar database!!!!!!!!!!!!", dataBaseHelper.getAllCalendar().toString());

        //dataBaseHelper.deleteAllCalendar();
        //Log.d("The calendar database after delete all!!!!!!!!!!!!", dataBaseHelper.getAllCalendar().toString());

        //2. clear frontend

        clearBtnAction(view);

//        confirmedNewPlan = new HashMap<String, String>(); // hashmap
//        for (int i = 0; i < 7; i++){
//            for (int j = 0; j < 4; j++){
//                if (dayItemArrayList.get(i).getStatus() && workoutItemArrayList.get(j).getStatus()){
//                    confirmedNewPlan.put(dayItemArrayList.get(i).getWeekday(),
//                            workoutItemArrayList.get(j).getWorkoutName());
//                }
//            }
//        }

    }

    public void clearSelectedWorkout(View view){
        for (int i = 0; i < 4; i++){
            workoutItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);
            workoutItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#637daf"));
        }
//        currentCalories = 0;
//        calTextView.setText(String.valueOf(currentCalories));
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