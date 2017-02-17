package me.elmira.stayfocused.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import me.elmira.stayfocused.data.Task;
import me.elmira.stayfocused.data.TaskListFilter;

/**
 * Created by elmira on 1/31/17.
 */

public interface TasksDataSource {

    interface LoadTasksCallback {

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    void loadTasks(@NonNull LoadTasksCallback callback, TaskListFilter filter);

    void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback);

    void deleteTask(String taskId);

    void insertTask(@NonNull Task task);

    void updateTask(@NonNull Task task);

    void completeTask(@NonNull Task task);

    void activateTask(@NonNull Task task);

}