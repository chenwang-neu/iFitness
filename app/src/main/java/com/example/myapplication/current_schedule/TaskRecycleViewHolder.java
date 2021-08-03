package com.example.myapplication.current_schedule;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;


public class TaskRecycleViewHolder extends RecyclerView.ViewHolder {
    public TextView todayTask;
    public CheckBox todayTaskCheckbox;
    public TextView todayItemCal;
    public ImageButton infoButton;


    public TaskRecycleViewHolder(View todayTaskView, final ItemClickListener listener) {
        super(todayTaskView);
        todayTask = itemView.findViewById(R.id.todayTaskName);
        todayTaskCheckbox = itemView.findViewById(R.id.todayTaskCheckbox);
        todayItemCal = itemView.findViewById(R.id.todayItemCal);
        infoButton = itemView.findViewById(R.id.infoBtn);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onInfoBtnClick(position);
                    }
                }
            }
        });


    }

}
