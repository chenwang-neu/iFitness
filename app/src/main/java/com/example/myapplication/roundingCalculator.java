package com.example.myapplication;

public final class roundingCalculator {
    private roundingCalculator() {
    }

    public static double roundingValue(double value, double digits) {
        value *= Math.pow(10, digits);
        value = Math.round(value);
        value = value / Math.pow(10, digits);
        return value;
    }
}