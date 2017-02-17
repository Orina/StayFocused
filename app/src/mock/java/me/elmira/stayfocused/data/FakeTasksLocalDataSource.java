package me.elmira.stayfocused.data;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import me.elmira.stayfocused.data.source.TasksDataSource;

/**
 * Created by elmira on 1/31/17.
 */

public class FakeTasksLocalDataSource implements TasksDataSource {
    private static FakeTasksLocalDataSource INSTANCE = null;

    public static FakeTasksLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeTasksLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void loadTasks(@NonNull LoadTasksCallback callback, TaskListFilter filter) {
        callback.onTasksLoaded(generateMockTasks());
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        callback.onTaskLoaded(generateMockTask());
    }

    @Override
    public void refreshTasks() {

    }

    private List<Task> generateMockTasks() {
        List<Task> list = new ArrayList<>();
        String str = "The  app lets a user create, read, update and delete to-do tasks. A task has a title and description and can be completed or active. Completed tasks can be deleted at once. All screens have a drawer to switch between Tasks and Statistics and a today_toolbar. Shows number of active tasks and completed tasks. The project has a flavor dimension to let the developer test against fake data or the real remote data source.";
        int N = str.length();
        Random r = new Random(System.currentTimeMillis());
        List<Label> labels = new ArrayList<>();
        //labels.add(new Label("algorithms"));
        labels.add(new Label("read"));
        labels.add(new Label("project bbb"));
        labels.add(new Label("shop"));
        Calendar calendar = GregorianCalendar.getInstance();
        Random rn = new Random();
        int offset = 30;
        for (int i = 0; i < 30; i++) {
            int start = r.nextInt(N - offset);
            int end = r.nextInt(N - offset);
            start = Math.min(start, end);
            end = Math.max(start, end);

            Priority priority = i % 5 == 0 ? Priority.HIGH : Priority.NORMAL;

            Task task = new Task.Builder().title((i % 5 == 0) ? "" : str.substring(start, start + offset).trim()).description(str.substring(start, end).trim()).completed(false).priority(priority.getId()).dueDate(calendar.getTimeInMillis()).build();

            if (Strings.isNullOrEmpty(task.getTitle()) && Strings.isNullOrEmpty(task.getDescription()))
                continue;

            if (i % 3 == 0) {
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + rn.nextInt(2));
            }

            list.add(task);
        }
        return list;
    }


    private Task generateMockTask() {
        String str = "Completed tasks can be deleted at once. All screens have a drawer to switch between Tasks and Statistics and a today_toolbar. Shows number of active tasks and completed tasks. The project has a flavor dimension to let the developer test against fake data or the real remote data source.";
        int N = str.length();
        Random r = new Random(System.currentTimeMillis());
        List<Label> labels = new ArrayList<>();
        labels.add(new Label("read"));
        labels.add(new Label("project bbb"));

        int offset = 30;
        int start = r.nextInt(N - offset);
        int end = r.nextInt(N - offset);
        start = Math.min(start, end);
        end = Math.max(start, end);

        Priority priority = Priority.HIGH;
        Task task = new Task.Builder().title(str.substring(start, start + offset)).description(str.substring(start, end)).completed(false).priority(priority.getId()).dueDate(new Date().getTime()).build();
        return task;
    }

    @Override
    public void deleteTask(String taskId) {

    }

    @Override
    public void completeTask(String taskId) {

    }

    @Override
    public void insertTask(@NonNull Task task) {

    }

    @Override
    public void updateTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull Task task) {

    }

    @Override
    public void activateTask(@NonNull Task task) {

    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public Task getCachedTask(@NonNull String taskId) {
        return null;
    }
}
