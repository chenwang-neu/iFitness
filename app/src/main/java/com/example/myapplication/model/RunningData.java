package com.example.myapplication.model;

public class RunningData {
        private double Longitude;
        private double Latitude;
        private double Speed;
        private double pace;
        private String date;
        private double burntCalories;
        private Double distanceKm;
        private Double bodyMass;

        public RunningData() {}

        public double getLongitude() { return Longitude; }

        public void setLongitude(double longitude) {
            Longitude = longitude;
        }

        public double getLatitude() { return Latitude; }

        public void setLatitude(double latitude) {
            Latitude = latitude;
        }

        public double getSpeed() { return Speed; }

        public void setSpeed(double speed) {
            Speed = speed;
        }

        public double getPace() { return pace; }

        public void setPace(double pace) {
            this.pace = pace;
        }

        public String getDate() { return date; }

        public void setDate(String date) { this.date = date; }

        public double getBurntCalories() { return burntCalories; }

        public void setBurntCalories(double burntCalories) { this.burntCalories = burntCalories; }

        public Double getDistanceKm() { return distanceKm; }

        public void setDistanceKm(Double distanceKm) {
            this.distanceKm = distanceKm;
        }

        public Double getBodyMass() { return bodyMass; }

        public void setBodyMass(Double bodyMass) { this.bodyMass = bodyMass; }
    }

