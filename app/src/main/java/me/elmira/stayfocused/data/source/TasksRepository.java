package me.elmira.stayfocused.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.elmira.stayfocused.data.Task;
import me.elmira.stayfocused.data.TaskListFilter;

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
    public void loadTasks(@NonNull final LoadTasksCallback callback, TaskListFilter filter) {
        checkNotNull(callback);

        if (mCachedTasks != null && !mCacheIsDirty) {
            //todo check this code
            //callback.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
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
        }, filter);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallback callback) {
        checkNotNull(callback);
        checkNotNull(taskId);

        Task cachedTask = getCachedTask(taskId);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }

        mTasksLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

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

    @Override
    public void deleteTask(String taskId) {
        mTasksLocalDataSource.deleteTask(checkNotNull(taskId));
        mCachedTasks.remove(taskId);
    }

    public void completeTask(String taskId) {
        checkNotNull(taskId);
        completeTask(getCachedTask(taskId));
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.completeTask(task);

        Task completedTask = new Task.Builder(task.getId()).completed(true).build();
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(completedTask.getId(), completedTask);
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.activateTask(task);

        Task completedTask = new Task.Builder(task).completed(false).build();
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(completedTask.getId(), completedTask);
    }

    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getCachedTask(taskId));
    }

    @Override
    public void insertTask(@NonNull Task task) {
        checkNotNull(task);

        mTasksLocalDataSource.insertTask(task);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(@NonNull Task task) {
        checkNotNull(task);

        mTasksLocalDataSource.updateTask(task);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Nullable
    public Task getCachedTask(@NonNull String id) {
        checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        }
        else {
            return mCachedTasks.get(id);
        }
    }

    public void updateCachedTask(@NonNull Task task) {
        checkNotNull(task);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    public void removeCachedTask(@NonNull String taskId) {
        checkNotNull(taskId);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            return;
        }
        mCachedTasks.remove(taskId);
    }
}
