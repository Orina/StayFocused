package me.elmira.stayfocused.today;

import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.List;

import me.elmira.stayfocused.BasePresenter;
import me.elmira.stayfocused.BaseView;
import me.elmira.stayfocused.adapter.TaskAdapter;
import me.elmira.stayfocused.data.Task;

/**
 * Created by elmira on 1/31/17.
 */

public interface TodayContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        boolean isActive();

        void showTasks(List<Task> tasks);

        void showNoTasks();

        void showAddTaskUI();

        void showTaskDetailsUI(@NonNull String taskId, @NonNull android.view.View view);

        void onTaskCreated(@NonNull Task task);

        void onTaskUpdated(@NonNull Task task);

        void onTaskDeleted(@NonNull String taskId);

        void onTaskCompleted(@NonNull String taskId);
    }

    interface Presenter extends BasePresenter, TaskAdapter.OnTaskClickListener, TaskAdapter.OnTaskTouchListener {

        void loadTasks(boolean forceUpdate);

        void result(int requestCode, int resultCode, Intent data);

        void addNewTask();
    }
}
