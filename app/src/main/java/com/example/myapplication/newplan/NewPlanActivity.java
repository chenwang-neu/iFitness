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
import java.util.List;

public class NewPlanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ArrayList<DayItem> dayItemArrayList;
    private DayItem mon, tue, wed, thurs, fri, sat, sun;
    private ArrayList<WorkoutItem> workoutItemArrayList;
    private WorkoutItem workoutItem1, workoutItem2, workoutItem3, workoutItem4;
    private Integer currentCalories;
    private TextView calTextView;
    private ArrayList<WorkoutCategoryItem> workoutCategoryArrayList;
    private CategoryAdapter catAdapter;
    WorkoutCategoryItem selectedCategory;
    private ArrayList<String> workoutCategoryName ;

    //test firebase
    DatabaseReference databaseReference;

    //days list and exercise list for days: if day not selected or exercises not selected, will throw someting
    List<String> dayList = new ArrayList<>();
    List<String> exerciseList = new ArrayList<>();


    ArrayList<Integer> pendingDaysList = new ArrayList<>();
    ArrayList<Integer> pendingIndexList = new ArrayList<>();
    ArrayList<String> pendingCategoryList = new ArrayList<>();

    //total calories from use selected
    private List<String> spinnerList = new ArrayList<>();


    // For savedInstance
    private static final String PENDING_DAYS = "PENDING_DAYS";
    private static final String PENDING_DAYS_SIZE = "PENDING_DAYS_SIZE";
    private static final String PENDING_WORKOUTS = "PENDING_WORKOUTS";
    private static final String PENDING_WORKOUTS_INDEX = "PENDING_WORKOUTS_INDEX";
    private static final String PENDING_WORKOUTS_SIZE = "PENDING_WORKOUTS_SIZE";
    private static final String PENDING_CALORIES = "PENDING_CALORIES";
    private static final String PENDING_DAYLIST = "PENDING_DAYLIST";
    private static final String PENDING_DAYLIST_SIZE = "PENDING_DAYLIST_SIZE";
    private static final String PENDING_EXERCISTLIST = "PENDING_EXERCISTLIST";
    private static final String PENDING_EXERCISTLIST_SIZE = "PENDING_EXERCISTLIST_SIZE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_plan);
        initializeDefault();

        calTextView = findViewById(R.id.calNumTextview);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        clearSelectedWorkout(view);


        selectedCategory = (WorkoutCategoryItem) parent.getItemAtPosition(position);
        restoreWorkoutItemColor(pendingCategoryList, pendingIndexList);

        String category = selectedCategory.getCategoryName().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("exercises");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Exercise> categoryExercise = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    if (exercise.getCategory().equals(category)) {
                        categoryExercise.add(exercise);
                    }
                }
                workoutItem1.getBtn().setText(categoryExercise.get(0).getEname());
                workoutItem2.getBtn().setText(categoryExercise.get(1).getEname());
                workoutItem3.getBtn().setText(categoryExercise.get(2).getEname());
                workoutItem4.getBtn().setText(categoryExercise.get(3).getEname());

                workoutItem1.setCal((categoryExercise.get(0).getCalories()) * 60);
                workoutItem2.setCal((categoryExercise.get(0).getCalories()) * 60);
                workoutItem3.setCal((categoryExercise.get(0).getCalories()) * 60);
                workoutItem4.setCal((categoryExercise.get(0).getCalories()) * 60);

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


    // handles screen rotation
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d("kk out",pendingCategoryList.toString());
        Log.d("kk out",pendingIndexList.toString());
//        Log.d("kk out", dayList.toString());

        // Handle days
        int pendingDaySize = pendingDaysList == null? 0: pendingDaysList.size();
        outState.putInt(PENDING_DAYS_SIZE, pendingDaySize);
        for (int i = 0; i < pendingDaySize; i++){
            outState.putInt(PENDING_DAYS+i, pendingDaysList.get(i));
        }

        // Handle workout btns
        int pendingWorkoutSize = pendingIndexList == null ? 0 : pendingIndexList.size();
        outState.putInt(PENDING_WORKOUTS_SIZE, pendingWorkoutSize);
        if (pendingWorkoutSize != 0){
            for (int i = 0; i < pendingIndexList.size(); i++){
                outState.putInt(PENDING_WORKOUTS_INDEX + i, pendingIndexList.get(i));
            }
            for (int i = 0; i < pendingCategoryList.size(); i++){
                outState.putString(PENDING_WORKOUTS + i, pendingCategoryList.get(i));
            }
        }

        // Handle calories num
        outState.putInt(PENDING_CALORIES, currentCalories);
        super.onSaveInstanceState(outState);

//         Handel exerciseList and dayList for db
        Log.d("kk get", dayList.toString());
        Log.d("kk get", exerciseList.toString());
        int dayListSize = dayList == null? 0: dayList.size();
        outState.putInt(PENDING_DAYLIST_SIZE, dayListSize);
        if (dayListSize != 0) {
            for (int i = 0; i < dayListSize; i++) {
                outState.putString(PENDING_DAYLIST + i, dayList.get(i));
            }
        }
        int exerciseListSize = exerciseList == null? 0: exerciseList.size();
        outState.putInt(PENDING_EXERCISTLIST_SIZE, exerciseListSize);
        for (int i = 0; i < exerciseListSize; i++){
            outState.putString(PENDING_EXERCISTLIST + i, exerciseList.get(i));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null ){
            if (savedInstanceState.containsKey(PENDING_DAYS_SIZE)){
                int pendingDaysSize = savedInstanceState.getInt(PENDING_DAYS_SIZE);
                for (int i = 0; i < pendingDaysSize; i++){
                    pendingDaysList.add(savedInstanceState.getInt(PENDING_DAYS+i));
                    dayItemArrayList.get(savedInstanceState.getInt(PENDING_DAYS+i)).getBtn().setBackgroundColor(Color.parseColor("#002952"));
                    dayItemArrayList.get(savedInstanceState.getInt(PENDING_DAYS+i)).changeSelectionStatus(Boolean.TRUE);
                }
            }

            if (savedInstanceState.containsKey(PENDING_WORKOUTS_SIZE)){
                int pendingWorkoutSize = savedInstanceState.getInt(PENDING_WORKOUTS_SIZE);
                for (int i = 0; i < pendingWorkoutSize; i++){
                    pendingIndexList.add(savedInstanceState.getInt(PENDING_WORKOUTS_INDEX + i));
                    pendingCategoryList.add(savedInstanceState.getString(PENDING_WORKOUTS + i));
                }
            }

            currentCalories = savedInstanceState.getInt(PENDING_CALORIES);

            if (savedInstanceState.containsKey(PENDING_DAYLIST_SIZE)){
                int dayListSize = savedInstanceState.getInt(PENDING_DAYLIST_SIZE);
                for (int i = 0; i < dayListSize; i++){
                    dayList.add(savedInstanceState.getString(PENDING_DAYLIST + i));
                }
            }


            if (savedInstanceState.containsKey(PENDING_EXERCISTLIST_SIZE)){
                int exerciseListSize = savedInstanceState.getInt(PENDING_EXERCISTLIST_SIZE);
                for (int i = 0; i < exerciseListSize; i++){
                    exerciseList.add(savedInstanceState.getString(PENDING_EXERCISTLIST + i));
                }
            }
            Log.d("kk restore", exerciseList.toString());
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

        selectedCategory = new WorkoutCategoryItem("", R.drawable.abs);

        workoutCategoryArrayList = new ArrayList<>();
        workoutCategoryArrayList.add(new WorkoutCategoryItem("Chest", R.drawable.abs));
        workoutCategoryArrayList.add(new WorkoutCategoryItem("Back", R.drawable.back));
        workoutCategoryArrayList.add(new WorkoutCategoryItem("Arm", R.drawable.arm));
        workoutCategoryArrayList.add(new WorkoutCategoryItem("Leg", R.drawable.leg));

        workoutCategoryName = new ArrayList<>();
        workoutCategoryName.add("Chest");
        workoutCategoryName.add("Back");
        workoutCategoryName.add("Arm");
        workoutCategoryName.add("Leg");


    }

    public void restoreWorkoutItemColor(ArrayList<String> pendingCategoryList,
                                        ArrayList<Integer> tempSelectedIdx){
        for (int i = 0; i < workoutItemArrayList.size(); i++){
            workoutItemArrayList.get(i).setSelected(false);
        }
        for (int i =0; i < pendingCategoryList.size(); i++){
            if (pendingCategoryList.get(i) == selectedCategory.getCategoryName()){
                workoutItemArrayList.get(tempSelectedIdx.get(i)).getBtn().setBackgroundColor(Color.parseColor("#002952"));
                workoutItemArrayList.get(tempSelectedIdx.get(i)).setSelected(true);
            }
        }
    }

    // deal with week day selection
    public void onSelectedWeekdayAction(DayItem day, int idx){
        String dayName = day.getWeekday();
        //if this means client choose this day, add it to the daylist
        if (day.getStatus() == Boolean.FALSE){
            day.getBtn().setBackgroundColor(Color.parseColor("#002952"));
            day.changeSelectionStatus(Boolean.TRUE);
            dayList.add(dayName);
            pendingDaysList.add(idx);
        } else {
            // unclick this day, remove this day from daylist
            day.getBtn().setBackgroundColor(Color.parseColor("#637daf"));
            day.changeSelectionStatus(Boolean.FALSE);

            int pos = dayList.indexOf(dayName);
            if (pos >= 0) {
                dayList.remove(pos);
                pendingDaysList.remove(pendingDaysList.indexOf(idx));
            }

        }
    }

    // deal with work out selection
    public void onSelectedWorkoutAction(WorkoutItem workout, int idx){

        String exerciseName = workout.getWorkoutName();
        //if this means client choose this exercise, add it to exerciseList
        if (workout.getStatus() == Boolean.FALSE){
            workout.getBtn().setBackgroundColor(Color.parseColor("#002952"));
            currentCalories += workout.getCal();
            calTextView.setText(String.valueOf(currentCalories));
            workout.changeSelectionStatus(Boolean.TRUE);

            exerciseList.add(exerciseName);

            pendingIndexList.add(idx);
            pendingCategoryList.add(selectedCategory.getCategoryName());

        } else {
            //if this means unclick the exercise button, remove this exercise from the exerciseLst
            int pos = exerciseList.indexOf(exerciseName);
            if (pos >= 0) {
                exerciseList.remove(pos);
            }

            workout.getBtn().setBackgroundColor(Color.parseColor("#637daf"));
            currentCalories = currentCalories - workout.getCal();
            calTextView.setText(String.valueOf(currentCalories));
            workout.changeSelectionStatus(Boolean.FALSE);

            pendingIndexList.remove(pendingIndexList.indexOf(idx));
            pendingCategoryList.remove(selectedCategory.getCategoryName());

        }
    }

    public void onButtonClick(View view){
        switch (view.getId()){
            case R.id.mondayBtn:
                onSelectedWeekdayAction(mon, 0);
                break;
            case R.id.tuesdayBtn:
                onSelectedWeekdayAction(tue, 1);
                break;
            case R.id.wednesdayBtn:
                onSelectedWeekdayAction(wed, 2);
                break;
            case R.id.thursdayBtn:
                onSelectedWeekdayAction(thurs, 3);
                break;
            case R.id.fridayBtn:
                onSelectedWeekdayAction(fri, 4);
                break;
            case R.id.saturdayBtn:
                onSelectedWeekdayAction(sat, 5);
                break;
            case R.id.sundayBtn:
                onSelectedWeekdayAction(sun, 6);
                break;
            case R.id.defaultWorkout1:
                onSelectedWorkoutAction(workoutItem1, 0);
                break;
            case R.id.defaultWorkout2:
                onSelectedWorkoutAction(workoutItem2, 1);
                break;
            case R.id.defaultWorkout3:
                onSelectedWorkoutAction(workoutItem3, 2);
                break;
            case R.id.defaultWorkout4:
                onSelectedWorkoutAction(workoutItem4, 3);
                break;
        }
    }



    public void clearBtnAction(View view){
        //backend clear dayLst and exerciseLst
        dayList.clear();
        exerciseList.clear();
        pendingCategoryList.clear();
        pendingIndexList.clear();
        pendingDaysList.clear();

        //front end
        for (int i = 0; i < dayItemArrayList.size(); i++){
            dayItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);
            dayItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#637daf"));
        }
        for (int i = 0; i < workoutItemArrayList.size(); i++){
            workoutItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);
            workoutItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#637daf"));
        }
        currentCalories = 0;
        calTextView.setText(String.valueOf(currentCalories));
    }

    public void confirmBtnAction(View view){
        //if click confirm button

        //1. add to calendar database

        if (dayList.size() == 0 || exerciseList.size() == 0) {
            Toast.makeText(NewPlanActivity.this, "You have not select day or exercise yet. Please select first", Toast.LENGTH_SHORT).show();
            Log.d("can not be 0", "You have not select day or exercise yet. Please select first");
            return;
        }
        DataBaseHelper dataBaseHelper = new DataBaseHelper(NewPlanActivity.this);
        for (String day : dayList) {
            for (String exer : exerciseList) {
                //1. create calendar class
                Calendar calendar = new Calendar(-1, day, exer);
                //2. add calendar to calendar database

                dataBaseHelper.addOneCalendar(calendar);
                Log.d("kk confirm", day);
                Log.d("kk confirm", exer);
            }

        }
        //test calendar database
        Log.d("The calendar database!!!!!!!!!!!!", dataBaseHelper.getAllCalendar().toString());

        //2. clear frontend

        clearBtnAction(view);

    }

    public void clearSelectedWorkout(View view){
        for (int i = 0; i < workoutItemArrayList.size(); i++){
            workoutItemArrayList.get(i).changeSelectionStatus(Boolean.FALSE);
            workoutItemArrayList.get(i).getBtn().setBackgroundColor(Color.parseColor("#637daf"));
        }
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