package com.example.myapplication.running_tracker;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class timeCalculator {

    private timeCalculator(){}

    public static void countAndShowTrainingTime(String startTime, String stopTime, TextView textView) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("pl-PL"));
        Date d1, d2;
        try {
            d1 = format.parse(startTime);
            d2 = format.parse(stopTime);
            long diff = Math.abs (d2.getTime() - d1.getTime());

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;

            long hours = diff / hoursInMilli;
            diff = diff % hoursInMilli;

            long minutes = diff / minutesInMilli;
            diff = diff % minutesInMilli;

            long seconds = diff / secondsInMilli;

            String hoursString=""+hours, minutesString=""+minutes, secondsString=""+seconds;

            if (hours <10) hoursString="0"+hours;
            if (minutes <10) minutesString="0"+minutes;
            if (seconds <10) secondsString="0"+seconds;

            String difference = hoursString+":"+minutesString+":"+secondsString;
            textView.setText(difference);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static long countTrainingTimeInSec(String startTime, String stopTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("pl-PL"));
        Date d1, d2;
        try {
            d1 = format.parse(startTime);
            d2 = format.parse(stopTime);
            long diff = Math.abs(d2.getTime() - d1.getTime());
            long secondsInMilli = 1000;
            long seconds = diff / secondsInMilli;
            return seconds;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}