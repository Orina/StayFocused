package me.elmira.stayfocused.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.elmira.stayfocused.data.Task;
import me.elmira.stayfocused.data.TaskListFilter;
import me.elmira.stayfocused.data.source.TasksDataSource;
import me.elmira.stayfocused.data.source.local.TasksPersistenceContract.TaskEntry;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 1/31/17.
 */

public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE = null;
    private static final String LOG_TAG = TasksLocalDataSource.class.getName();

    private TasksDbHelper mDbHelper;

    private TasksLocalDataSource(Context context) {
        mDbHelper = TasksDbHelper.getsInstance(context);
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void loadTasks(@NonNull LoadTasksCallback callback, TaskListFilter filter) {
        checkNotNull(callback);

        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = null;
        Cursor c = null;

        String selection = null;
        String[] selectionArgs = null;
        if (filter != null) {
            selection = filter.getSelection();
            selectionArgs = filter.getSelectionArgs();
        }
        try {
            db = mDbHelper.getReadableDatabase();
            c = db.query(TaskEntry.TABLE_NAME, TasksPersistenceContract.TASK_ENTRY_ALL_COLUMNS_PROJECTION, selection, selectionArgs, null, null, null);

            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    Task task = parseTask(c);
                    tasks.add(task);
                }
            }
        } catch (Throwable ex) {
            Log.e(LOG_TAG, ex.toString());
        } finally {
            if (c != null) c.close();
            if (db != null) db.close();
        }
        if (tasks.isEmpty()) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onTasksLoaded(tasks);
        }
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        SQLiteDatabase db = null;
        Cursor c = null;

        String selection = TaskEntry.COLUMN_NAME_TASK_ID + " LIKE ? ";
        String[] selectionArgs = new String[]{taskId};
        Task task = null;

        try {
            db = mDbHelper.getReadableDatabase();
            c = db.query(TaskEntry.TABLE_NAME, TasksPersistenceContract.TASK_ENTRY_ALL_COLUMNS_PROJECTION, selection, selectionArgs, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                task = parseTask(c);
            }
        } catch (Throwable ex) {
            Log.e(LOG_TAG, ex.toString());
        } finally {
            if (c != null) c.close();
            if (db != null) db.close();
        }
        if (task == null) {
            callback.onDataNotAvailable();
        }
        else {
            callback.onTaskLoaded(task);
        }
    }

    @Override
    public void insertTask(@NonNull Task task) {
        checkNotNull(task);
        SQLiteDatabase db = null;
        try {
            db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_TASK_ID, task.getId());
            values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
            values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted() ? 1 : 0);
            values.put(TaskEntry.COLUMN_NAME_DUE_DATE, task.getDueDate() / 1000);
            values.put(TaskEntry.COLUMN_NAME_PRIORITY, task.getPriorityId());

            db.insert(TaskEntry.TABLE_NAME, null, values);
        } catch (Throwable ex) {
            Log.e(LOG_TAG, ex.toString());
        } finally {
            if (db != null) db.close();
        }
    }

    @Override
    public void updateTask(@NonNull Task task) {
        checkNotNull(task);
        SQLiteDatabase db = null;

        String selection = TaskEntry.COLUMN_NAME_TASK_ID + " LIKE ?";
        String[] selectionArgs = {task.getId()};

        try {
            db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
            values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted() ? 1 : 0);
            values.put(TaskEntry.COLUMN_NAME_DUE_DATE, task.getDueDate() / 1000);
            values.put(TaskEntry.COLUMN_NAME_PRIORITY, task.getPriorityId());

            db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        } catch (Throwable ex) {
            Log.e(LOG_TAG, ex.toString());
        } finally {
            if (db != null) db.close();
        }
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);

        SQLiteDatabase db = null;
        String selection = TaskEntry.COLUMN_NAME_TASK_ID + " LIKE ?";
        String[] selectionArgs = {task.getId()};
        try {
            db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

            db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        } catch (Throwable ex) {
            Log.e(LOG_TAG, ex.toString());
        } finally {
            if (db != null) db.close();
        }
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);

        SQLiteDatabase db = null;
        String selection = TaskEntry.COLUMN_NAME_TASK_ID + " LIKE ?";
        String[] selectionArgs = {task.getId()};
        try {
            db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

            db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        } catch (Throwable ex) {
            Log.e(LOG_TAG, ex.toString());
        } finally {
            if (db != null) db.close();
        }
    }

    @Override
    public void deleteTask(String taskId) {
        checkNotNull(taskId);
        SQLiteDatabase db = null;
        String selection = TaskEntry.COLUMN_NAME_TASK_ID + " LIKE ?";
        String[] selectionArgs = {taskId};

        try {
            db = mDbHelper.getWritableDatabase();
            db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
        } catch (Throwable ex) {
            Log.e(LOG_TAG, ex.toString());
        } finally {
            if (db != null) db.close();
        }
    }

    private Task parseTask(Cursor c) {
        String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TASK_ID));
        Task.Builder builder = new Task.Builder(itemId);

        int titleColumnIndex = c.getColumnIndex(TaskEntry.COLUMN_NAME_TITLE);
        if (titleColumnIndex != -1) {
            builder.title(c.getString(titleColumnIndex));
        }
        int descriptionColumnIndex = c.getColumnIndex(TaskEntry.COLUMN_NAME_DESCRIPTION);
        if (descriptionColumnIndex != -1) {
            builder.description(c.getString(descriptionColumnIndex));
        }
        int completedColumnIndex = c.getColumnIndex(TaskEntry.COLUMN_NAME_COMPLETED);
        if (completedColumnIndex != -1) {
            builder.completed(c.getInt(completedColumnIndex) == 1);
        }
        int dueDateColumnIndex = c.getColumnIndex(TaskEntry.COLUMN_NAME_DUE_DATE);
        if (dueDateColumnIndex != -1) {
            builder.dueDate(c.getInt(dueDateColumnIndex) * 1000l);
        }
        int priorityColumnIndex = c.getColumnIndex(TaskEntry.COLUMN_NAME_PRIORITY);
        if (priorityColumnIndex != -1) {
            builder.priority(c.getInt(priorityColumnIndex));
        }
        return builder.build();
    }
}
