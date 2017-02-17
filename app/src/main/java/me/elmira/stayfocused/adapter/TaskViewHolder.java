package me.elmira.stayfocused.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.elmira.stayfocused.R;
import me.elmira.stayfocused.data.Priority;
import me.elmira.stayfocused.data.Task;

/**
 * Created by elmira on 2/16/17.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder {
    final TextView titleView;
    final TextView descView;
    final ImageView priorityIcon;
    //final ListView labelsListView;
    //private TaskLabelAdapter mTaskLabelAdapter;
    private View cardView;
    private TextView dueDateTextView;
    private View dueDateLayout;

    private Calendar mCalendar = Calendar.getInstance();
    private int thisYear = mCalendar.get(Calendar.YEAR);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d");
    private SimpleDateFormat simpleDateFormatWithYear = new SimpleDateFormat("EEE, MMM d, yyyy");

    public TaskViewHolder(View container) {
        super(container);

        cardView = container.findViewById(R.id.card_view);
        titleView = (TextView) cardView.findViewById(R.id.task_title);
        descView = (TextView) cardView.findViewById(R.id.task_description);
        priorityIcon = (ImageView) container.findViewById(R.id.priorityIcon);
        dueDateLayout = container.findViewById(R.id.due_date_layout);
        dueDateTextView = (TextView) dueDateLayout.findViewById(R.id.task_due_date);
        //labelsListView = (ListView) cardView.findViewById(R.id.labelsList);
        //mTaskLabelAdapter = new TaskLabelAdapter();
        //labelsListView.setAdapter(mTaskLabelAdapter);
    }

    public void bind(Context context, final Task task, final TaskAdapter.OnTaskClickListener listener) {
        if (Strings.isNullOrEmpty(task.getTitle())) {
            titleView.setVisibility(View.GONE);
        }
        else {
            titleView.setText(task.getTitle());
            titleView.setVisibility(View.VISIBLE);
        }
        if (Strings.isNullOrEmpty(task.getDescription())) {
            descView.setVisibility(View.GONE);
        }
        else {
            descView.setVisibility(View.VISIBLE);
            descView.setText(task.getDescription());
        }
        if (task.getPriority() == Priority.HIGH) {
            priorityIcon.setVisibility(View.VISIBLE);
            priorityIcon.getDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.colorAccentLight), PorterDuff.Mode.SRC_IN);
        }
        else {
            priorityIcon.setVisibility(View.GONE);
        }
        if (task.getDueDate() > 0) {
            mCalendar.setTime(new Date(task.getDueDate()));
            String formattedDate = "";
            if (mCalendar.get(Calendar.YEAR) != thisYear) {
                formattedDate = simpleDateFormatWithYear.format(mCalendar.getTime());
            }
            else {
                formattedDate = simpleDateFormat.format(mCalendar.getTime());
            }
            dueDateTextView.setText(formattedDate);
            dueDateLayout.setVisibility(View.VISIBLE);
        }
        else {
            dueDateLayout.setVisibility(View.GONE);
        }
        if (listener != null) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTaskClick(task, cardView);
                }
            });
        }
    }
}
