package com.example.myapplication.current_schedule;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.newplan.WorkoutItem;

import java.util.ArrayList;

public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewHolder> {

    private ArrayList<WorkoutItem> todayTaskList;
    private ItemClickListener listener;
    private Cursor mCursor;
    private Context mContext;

    public TaskRecycleViewAdapter(ArrayList<WorkoutItem> taskList) { todayTaskList = taskList;}
    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public TaskRecycleViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }




    @Override
    public TaskRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheduled_item_cardview, parent, false);
        return new TaskRecycleViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(TaskRecycleViewHolder holder, int position) {
//        if (!mCursor.moveToPosition(position)) {
//            return;
//        }

//        int id = mCursor.getInt(mCursor.getColumnIndex("cid"));
//        holder.itemView.setTag(id);

        WorkoutItem currentItem = todayTaskList.get(position);
        holder.todayTask.setText(currentItem.getWorkoutName());
        holder.todayTaskCheckbox.setChecked(currentItem.getStatus());
        holder.todayItemCal.setText(String.valueOf(currentItem.getCal()) + " Cal.");
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
        //return todayTaskList.size();
    }

//    public void swapCursor(Cursor newCursor) {
//        if (mCursor != null) {
//            mCursor.close();
//        }
//
//        mCursor = newCursor;
//
//        if (newCursor != null) {
//            nofityData
//        }
//    }


}
