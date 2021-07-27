package com.example.myapplication;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class todayTaskViewHolder extends RecyclerView.ViewHolder {
    public TextView todayTask;
    public CheckBox todayTaskCheckbox;
    public TextView todayItemCal;

    public todayTaskViewHolder(View todayTaskView, final ItemClickListener listener) {
        super(todayTaskView);
        todayTask = itemView.findViewById(R.id.todayTaskName);
        todayTaskCheckbox = itemView.findViewById(R.id.todayTaskCheckbox);
        todayItemCal = itemView.findViewById(R.id.todayItemCal);

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
    }

}
