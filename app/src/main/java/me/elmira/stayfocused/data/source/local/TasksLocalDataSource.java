package me.elmira.stayfocused.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import me.elmira.stayfocused.data.source.TasksDataSource;

/**
 * Created by elmira on 1/31/17.
 */

public class TasksLocalDataSource implements TasksDataSource {
    private static TasksLocalDataSource INSTANCE = null;

    public static TasksLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void loadTasks(@NonNull LoadTasksCallback callback) {

    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }

    @Override
    public void refreshTasks() {

    }
}
