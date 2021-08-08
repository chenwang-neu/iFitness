package com.example.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.RunningData;


public class RunningTrackerActivity extends AppCompatActivity implements SensorEventListener {

    private Button btnStart, btnStop;
    private TextView count, showDistance, showCalories, showSpeed, showTime;
    private EditText editBodyMass;
    private BroadcastReceiver broadcastReceiver;
    LocationManager lm;
    private SensorManager sensorManager;

    boolean activityRunning;
    private float initialAmount = 0;
    private boolean alreadyMeasured = false;
    private boolean showSteps = false;
    private double latitude = 0.0;
    private int stepsMeasured = 0;
    private double speedkmH = 0;
    private String startTime, stopTime;
    int maxId = 0;
    double bodyMass;
    double distance, distanceKm, totalDistanceKm, calories, caloriesTotal;
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

                    distanceKm=(double) intent.getExtras().get("distance")/1000;
                    totalDistanceKm +=distanceKm;
                    totalDistanceKm = roundingCalculator.roundingValue(totalDistanceKm,2);


                    calories = bodyMass*distanceKm;
                    caloriesTotal+= calories;
                    showDistance.setText(String.valueOf(totalDistanceKm));

                    caloriesTotal = roundingCalculator.roundingValue(calories,2);
                    showCalories.setText(String.valueOf(caloriesTotal));
                    showSpeed.setText(String.valueOf(speedkmH));

                    count.setText(String.valueOf(stepsMeasured));

                    RunningData data = new RunningData();

                    data.setLatitude(latitude);
                    data.setLongitude(longitude);
                    data.setSpeed(speedkmH);
                    data.setSteps(stepsMeasured);
                    data.setDate(currentDateAndTime);
                    data.setBurntCalories(calories);
                    data.setDistanceKm(distanceKm);
                    data.setBodyMass(bodyMass);
                }




            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));

        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
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

        editBodyMass = findViewById(R.id.editBodyMass);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        count = findViewById(R.id.txtSteps);
        showDistance = findViewById(R.id.txtDistance);
        showCalories = findViewById(R.id.txtCalories);
        showSpeed = findViewById(R.id.txtSpeed);
        showTime = findViewById(R.id.txtTime);

        if (!runtime_permissions()) enable_buttons();


    }

    private void enable_buttons() {
        btnStart.setOnClickListener(v -> {
            lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            String bodyMassString = editBodyMass.getText().toString().trim();
            if(bodyMassString.matches("")) bodyMass=0.0;
            else bodyMass = Double.parseDouble(bodyMassString);

            if (isGPSEnabled) {

                if (bodyMass!=0.0) {
                    stepsMeasured=0;
                    disableTrainingEditField();
                    Intent i = new Intent(getApplicationContext(), GPSService.class);
                    startService(i);

                    alreadyMeasured = false;
                    showSteps = true;
                }
                else Toast.makeText(getApplicationContext(),"Insert your body mass",Toast.LENGTH_SHORT).show();

            } else {
                final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        btnStop.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), GPSService.class);
            stopService(i);
            maxId=0;
            showSteps = false;


            resetVariables();

            enableTrainingEditField();
        });

    }

    private void resetVariables() {
        distanceKm=0;
        totalDistanceKm=0;
        calories =0;
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            if (!alreadyMeasured) {
                initialAmount = event.values[0];
                alreadyMeasured = true;
            }
            if (showSteps) {
                stepsMeasured = (int) (event.values[0] - initialAmount);
            } else stepsMeasured = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RunningTrackerActivity.this);
        builder.setMessage("Are you sure you want to exit? Clicking Yes will end current training session.");
        builder.setTitle("Warning!");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent i = new Intent(getApplicationContext(), GPSService.class);
            stopService(i);
            showSteps = false;
            activityRunning = false;
            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> {

            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
