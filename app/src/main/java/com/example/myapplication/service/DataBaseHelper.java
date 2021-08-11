package com.example.myapplication.service;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.model.Calendar;
import com.example.myapplication.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String CALENDAR_TABLE = "CALENDAR_TABLE";
    public static final String CNAME = "cname";
    public static final String ENAME = "ename";



    //create schema first

    public DataBaseHelper(@Nullable Context context) {
        super(context, "practice.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement1 =
                "CREATE TABLE CALENDAR_TABLE " +
                        "(" +
                        "cid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "cname VARCHAR(10) NOT NULL CHECK(cname IN('MON', 'TUES', 'WED', 'THUR', 'FRI', 'SAT', 'SUN')), " +
                        "ename VARCHAR(10)" +
                        ")";

        db.execSQL(createTableStatement1);
    }


    //if table already exists, drop first

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CALENDAR_TABLE);
    }

    //Start CRUD REQUEST HERE:
    public boolean addOneCalendar(Calendar calendar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CNAME, calendar.getCname());
        cv.put(ENAME, calendar.getEname());

        long insert = db.insert(CALENDAR_TABLE, null, cv);
        if (insert < 0) {
            return false;
        }
        return true;
    }



    public List<Calendar> getAllCalendar() {
        List<Calendar> calendarList = new ArrayList<>();
        String queryString = "SELECT * FROM " + CALENDAR_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int cid1 = cursor.getInt(0);
                String cname1 = cursor.getString(1);
                String ename1 = cursor.getString(2);

                Calendar calendar = new Calendar(cid1, cname1, ename1);
//                calendar.getCname().toString();
                calendarList.add(calendar);

            } while (cursor.moveToNext());
        } else {
            //do nothing if empty;
        }
        cursor.close();
        db.close();

        return calendarList;

    }

    public List<Calendar> getCalendarByDay(String day) {
        List<Calendar> calendarList = new ArrayList<>();

        String queryString = "SELECT * FROM CALENDAR_TABLE WHERE cname = '" + day + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {

                int cid1 = cursor.getInt(0);
                String cname1 = cursor.getString(1);
                String ename1 = cursor.getString(2);

                Calendar calendar = new Calendar(cid1, cname1, ename1);
//                calendar.getCname().toString();

                if (cname1.equals(day)) {
                    calendarList.add(calendar);
                }

            } while (cursor.moveToNext());
        } else {
            //do nothing if empty;
        }
        cursor.close();
        db.close();



        return calendarList;
    }

    public boolean deleteOneCalendar(int deleteCid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CALENDAR_TABLE + " WHERE cid = " + deleteCid;
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean deleteAllCalendar() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CALENDAR_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
        //db.execSQL("delete from "+ CALENDAR_TABLE);
    }

    public boolean deleteCalendarByDay(String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM CALENDAR_TABLE WHERE cname = '" + day + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
        //db.execSQL("delete from "+ CALENDAR_TABLE);
    }




//    public int getCalendarsRows() {
//        SQLiteDatabase db  = this.getWritableDatabase();
//
//        int count = 0;
//        String query = "select count(*) from " + CALENDAR_TABLE;
//        Cursor cursor = db.rawQuery(query, null);
//        while (cursor.moveToFirst()) {
//            count = cursor.getCount();
//        }
//        return count;
//    }


}
