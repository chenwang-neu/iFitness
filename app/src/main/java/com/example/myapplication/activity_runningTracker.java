package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;


import org.jetbrains.annotations.NotNull;

public class activity_runningTracker extends AppCompatActivity {
//    private LocationManager locationManager;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_tracker);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {

            }
        });

//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        locationManager.getCurrentLocation();
//
//        TextView speedText = findViewById(R.id.speedNum);
    }

    public void openNewPlan(View view) {
        Intent intent = new Intent(this, CreateNewPlanActivity.class);
        startActivity(intent);
    }

    public void openToday(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}