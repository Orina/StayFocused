package me.elmira.stayfocused.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.elmira.stayfocused.data.Task;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 1/31/17.
 */

public class TasksRepository implements TasksDataSource {
    private static TasksRepository INSTANCE = null;

    private final TasksDataSource mTasksLocalDataSource;

    Map<String, Task> mCachedTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    private TasksRepository(@NonNull TasksDataSource localDataSource) {
        mTasksLocalDataSource = checkNotNull(localDataSource);
    }

    public static TasksRepository getInstance(TasksDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(localDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void loadTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            return;
        }

        mTasksLocalDataSource.loadTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                callback.onTasksLoaded(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallback callback) {

    }

    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) mCachedTasks = new LinkedHashMap<>();
        else mCachedTasks.clear();

        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }
}
