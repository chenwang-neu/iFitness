package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class categoryAdapter extends ArrayAdapter <workoutCategoryItem>{

    public categoryAdapter(Context context, ArrayList<workoutCategoryItem> categoryList){
        super(context, 0, categoryList);
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
                    R.layout.workout_spinner, parent, false
            );
        }
        ImageView imageViewCategory = convertView.findViewById(R.id.catImgView);
        TextView textViewCategory = convertView.findViewById(R.id.CatTextView);

        workoutCategoryItem currentItem = getItem(position);

        if (currentItem != null){
            imageViewCategory.setImageResource(currentItem.getCategoryImg());
            textViewCategory.setText(currentItem.getCategoryName());
        }
        return convertView;
    }
}
