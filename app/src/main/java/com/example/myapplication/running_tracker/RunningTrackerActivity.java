package com.example.myapplication.running_tracker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.current_schedule.ScheduledPlanActivity;
import com.example.myapplication.model.RunningData;
import com.example.myapplication.newplan.NewPlanActivity;
import com.example.myapplication.service.GPSService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RunningTrackerActivity extends AppCompatActivity {

    private Button btnStart, btnStop, btnClear;
    private TextView showPace, showDistance, showCalories, showSpeed, showTime;
    private EditText editBodyMass;
    private BroadcastReceiver broadcastReceiver;
    LocationManager lm;
    private SensorManager sensorManager;
    DatabaseReference runningRef;
    boolean activityRunning;
    private float initialAmount = 0;
    private double latitude = 0.0;
    private double pace = 0;
    private double speedkmH = 0;
    private String startTime, stopTime;
    int maxId = 0;
    double bodyMass;
    double distance, totalDistance, caloriesTotal;
    int iteration=0;

    @Override
    public void onBackPressed() {
        showAlertDialog();

    }

    @Override
    protected void onResume() {

        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Double latitude = (Double) intent.getExtras().get("latitude");
                    Double longitude = (Double)  intent.getExtras().get("longitude");
                    Double speedkmH = (Double) intent.getExtras().get("speed");

                    String currentDateAndTime = String.valueOf(intent.getExtras().get("time"));

                    if (iteration==0) {
                        startTime= currentDateAndTime;
                        stopTime=startTime;
                    }
                    iteration+=1;
                    if (iteration!=0) {
                        stopTime= currentDateAndTime;
                    }

                    timeCalculator.countAndShowTrainingTime(startTime,stopTime,showTime);

                    distance=(double) intent.getExtras().get("distance");
                    totalDistance += distance;
                    totalDistance = roundingCalculator.roundingValue(totalDistance, 0);
                    showDistance.setText(String.valueOf(totalDistance));

                    caloriesTotal = bodyMass* totalDistance / 1000 * 1.036;
                    caloriesTotal = roundingCalculator.roundingValue(caloriesTotal,2);
                    showCalories.setText(String.valueOf(caloriesTotal));

                    showSpeed.setText(String.valueOf(speedkmH));

                    long timeInSec = timeCalculator.countTrainingTimeInSec(startTime, stopTime);
                    pace = roundingCalculator.roundingValue(timeInSec / 60 / totalDistance * 1000, 2);
                    int paceIntPart = (int)pace;
                    int paceDecimalPart = (int)(pace - (int)pace) * 100;
                    if (paceIntPart > 20) {
                        showPace.setText("0:00");
                    } else {
                        showPace.setText(String.format("%02d", paceIntPart) + ":" + String.format("%02d", paceDecimalPart));
                    }
                    runningRef = FirebaseDatabase.getInstance().getReference().child("running");
                    RunningData data = new RunningData();

                    data.setLatitude(latitude);
                    data.setLongitude(longitude);
                    data.setSpeed(speedkmH);
                    data.setPace(pace);
                    data.setDate(currentDateAndTime);
                    data.setBurntCalories(caloriesTotal);
                    data.setDistanceKm(totalDistance);
                    data.setBodyMass(bodyMass);

                    maxId+=1;
                    if (maxId % 30 == 0) runningRef.child(String.valueOf(maxId)).setValue(data);
                }

            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));

        activityRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runningtracker);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnClear = findViewById(R.id.btnClear);

        editBodyMass = findViewById(R.id.editBodyMass);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        showPace = findViewById(R.id.txtSteps);
        showDistance = findViewById(R.id.txtDistance);
        showCalories = findViewById(R.id.txtCalories);
        showSpeed = findViewById(R.id.txtSpeed);
        showTime = findViewById(R.id.txtTime);

        if (!runtime_permissions()) enable_buttons();

    }

    private void enable_buttons() {
        btnStart.setOnClickListener(v -> {
            runningRef = FirebaseDatabase.getInstance().getReference().child("running");
            runningRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long numOfRecord = dataSnapshot.getChildrenCount();
                    Log.d("number", String.valueOf(numOfRecord));
                    maxId = (int)numOfRecord * 30;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

            lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            String bodyMassString = editBodyMass.getText().toString().trim();
            if(bodyMassString.matches("")) {
                bodyMass=0.0;
                Toast.makeText(getApplicationContext(),"Please insert your body mass",Toast.LENGTH_SHORT).show();
            } else if (!isNumeric(bodyMassString)) {
                Toast.makeText(getApplicationContext(),"Body mass can only be integer.",Toast.LENGTH_SHORT).show();
            } else {
                bodyMass = Double.parseDouble(bodyMassString);
            }
            if (isGPSEnabled) {
                if (bodyMass!=0.0 && isNumeric(bodyMassString)) {
                    pace=0;
                    disableTrainingEditField();
                    Intent i = new Intent(getApplicationContext(), GPSService.class);
                    startService(i);
                }
            } else {
                final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        btnStop.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), GPSService.class);
            stopService(i);
            maxId=0;
            resetVariables();

            enableTrainingEditField();
        });
        btnClear.setOnClickListener(v -> {
            runningRef = FirebaseDatabase.getInstance().getReference().child("running");
            runningRef.removeValue();
        });
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private void resetVariables() {
        distance=0;
        totalDistance=0;
        caloriesTotal=0;
        speedkmH=0;
        iteration=0;
    }

    private void enableTrainingEditField() {

        editBodyMass.setText("");
        editBodyMass.setEnabled(true);
        editBodyMass.requestFocus();
    }

    private void disableTrainingEditField() {
        editBodyMass.setEnabled(false);

    }

    private boolean runtime_permissions() {
        if (/*Build.VERSION.SDK_INT >= 23 &&*/ ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else runtime_permissions();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RunningTrackerActivity.this);
        builder.setMessage("Are you sure you want to exit? Clicking Yes will end current training.");
        builder.setTitle("Warning!");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent i = new Intent(getApplicationContext(), GPSService.class);
            stopService(i);
            activityRunning = false;
            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> {

            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openToday(View view) {
        Intent intent = new Intent(this, ScheduledPlanActivity.class);
        startActivity(intent);
    }

    public void openNewPlan(View view) {
        Intent intent = new Intent(this, NewPlanActivity.class);
        startActivity(intent);
    }

}
