package com.example.myapplication.current_schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.newplan.DayItem;


import java.util.ArrayList;

public class DayItemAdapter extends ArrayAdapter<DayItem> {
    public DayItemAdapter(Context context, ArrayList<DayItem> dayList){
        super(context, 0, dayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.day_spinner, parent, false
            );
        }
        TextView dayTextView = convertView.findViewById(R.id.dayTextView);
        DayItem currentItem = getItem(position);

        if (currentItem != null){
            dayTextView.setText(currentItem.getWeekday());
        }
        return convertView;
    }
}
