package me.elmira.stayfocused.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.elmira.stayfocused.R;
import me.elmira.stayfocused.data.Task;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 1/19/17.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> implements TaskTouchHelperAdapter {

    private final LayoutInflater mLayoutInflater;
    private List<Task> mTasks;

    private WeakReference<Context> mContext;
    private OnTaskClickListener mTaskClickListener;
    private OnTaskTouchListener mTaskTouchListener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task, View cardView);
    }

    public interface OnTaskTouchListener {
        void onTaskDismiss(Task task);
    }

    public TaskAdapter(Activity activity) {
        mLayoutInflater = LayoutInflater.from(activity.getApplicationContext());
        mContext = new WeakReference<Context>(activity);
        mTasks = new ArrayList<>();
    }

    public void setClickListener(OnTaskClickListener listener) {
        this.mTaskClickListener = listener;
    }

    public void setTouchListener(OnTaskTouchListener mTaskTouchListener) {
        this.mTaskTouchListener = mTaskTouchListener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(mLayoutInflater.inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.bind(mContext.get(), task, mTaskClickListener);
    }

    @Override
    public int getItemCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    public void updateData(List<Task> tasks) {
        mTasks.clear();
        if (tasks != null) mTasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void removeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        for (int i = 0; i < mTasks.size(); i++) {
            Task task = mTasks.get(i);
            if (taskId.equals(task.getId())) {
                remove(i);
                break;
            }
        }
    }

    public void addTask(@NonNull Task task) {
        checkNotNull(task);
        int position = 0;
        mTasks.add(position, task);
        notifyItemInserted(position);
    }

    public void updateTask(@NonNull Task task) {
        checkNotNull(task);
        for (int i = 0; i < mTasks.size(); i++) {
            Task tmp = mTasks.get(i);
            if (tmp.getId().equals(task.getId())) {
                update(i, task);
                break;
            }
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mTasks, i, i + 1);
            }
        }
        else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mTasks, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        if (position < 0 || position >= mTasks.size()) return;
        if (mTaskTouchListener != null) {
            mTaskTouchListener.onTaskDismiss(mTasks.get(position));
        }
    }

    private void remove(int position) {
        mTasks.remove(position);
        notifyItemRemoved(position);
    }

    private void update(int position, Task task) {
        mTasks.set(position, task);
        notifyItemChanged(position);
    }
}
