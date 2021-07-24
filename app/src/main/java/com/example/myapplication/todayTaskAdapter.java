package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class todayTaskAdapter extends RecyclerView.Adapter<todayTaskViewHolder> {

    private ArrayList<todayTaskItem> todayTaskList;
    private ItemClickListener listener;

    public todayTaskAdapter(ArrayList<todayTaskItem> taskList) { todayTaskList = taskList;}
    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public todayTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cardview, parent, false);
        return new todayTaskViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(todayTaskViewHolder holder, int position) {
        todayTaskItem currentItem = todayTaskList.get(position);
        holder.todayTask.setText(currentItem.getTodayTaskName());
        holder.todayTaskCheckbox.setChecked(currentItem.getTodayTaskCheck());
    }

    @Override
    public int getItemCount() {
        return todayTaskList.size();
    }
}
