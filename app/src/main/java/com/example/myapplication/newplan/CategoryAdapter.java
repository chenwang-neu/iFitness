package com.example.myapplication.newplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter <WorkoutCategoryItem>{

    public CategoryAdapter(Context context, ArrayList<WorkoutCategoryItem> categoryList){
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

        WorkoutCategoryItem currentItem = getItem(position);

        if (currentItem != null){
            imageViewCategory.setImageResource(currentItem.getCategoryImg());
            textViewCategory.setText(currentItem.getCategoryName());
        }
        return convertView;
    }
}
