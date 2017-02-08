package me.elmira.stayfocused.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.base.Strings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.elmira.stayfocused.R;
import me.elmira.stayfocused.data.Priority;
import me.elmira.stayfocused.data.Task;

/**
 * Created by elmira on 1/19/17.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<Task> mTasks;

    private WeakReference<Context> mContext;


    public TaskAdapter(Activity activity) {
        mLayoutInflater = LayoutInflater.from(activity.getApplicationContext());
        mContext = new WeakReference<Context>(activity);
        mTasks = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_task, parent, false));
    }

    public void updateData(List<Task> tasks) {
        mTasks.clear();
        if (tasks != null) mTasks.addAll(tasks);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mTasks.get(position);

        if (task.hasImage()) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageDrawable(mContext.get().getDrawable(R.drawable.icon_category_food_raster));
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        if (Strings.isNullOrEmpty(task.getTitle())) {
            holder.titleView.setText(task.getDescription().substring(0, Math.min(30, task.getDescription().length())));
        } else {
            holder.titleView.setText(task.getTitle());
        }
        if (Strings.isNullOrEmpty(task.getDescription())) {
            holder.descView.setVisibility(View.GONE);
        } else {
            holder.descView.setVisibility(View.VISIBLE);
            holder.descView.setText(task.getDescription());
        }
        if (task.getPriority() == Priority.HIGH) {
            holder.priorityIcon.setVisibility(View.VISIBLE);
            holder.priorityIcon.getDrawable().mutate().setColorFilter(mContext.get().getResources().getColor(R.color.colorAccentLight), PorterDuff.Mode.SRC_IN);
        }
        if (task.getLabels() != null && task.getLabels().size() > 0) {
            holder.mTaskLabelAdapter.setLabels(task.getLabels());
            holder.labelsListView.setVisibility(View.VISIBLE);
        } else holder.labelsListView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView;
        final TextView descView;
        final ImageView imageView;
        final ProgressBar progressView;
        final ImageView priorityIcon;
        final ListView labelsListView;
        private TaskLabelAdapter mTaskLabelAdapter;

        public ViewHolder(View container) {
            super(container);

            View cardView = container.findViewById(R.id.card_view);
            titleView = (TextView) cardView.findViewById(R.id.task_title);
            descView = (TextView) cardView.findViewById(R.id.task_description);
            progressView = (ProgressBar) cardView.findViewById(R.id.task_progress_bar);
            imageView = (ImageView) cardView.findViewById(R.id.imageView);
            priorityIcon = (ImageView) container.findViewById(R.id.priorityIcon);

            labelsListView = (ListView) cardView.findViewById(R.id.labelsList);
            mTaskLabelAdapter = new TaskLabelAdapter();
            labelsListView.setAdapter(mTaskLabelAdapter);
        }
    }
}
