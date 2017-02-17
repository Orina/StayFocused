package me.elmira.stayfocused.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by elmira on 2/14/17.
 */

public class TasksDbHelper extends SQLiteOpenHelper {

    public static TasksDbHelper sInstance;

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "stayfocused.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String SEPARATOR = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TasksPersistenceContract.TaskEntry.TABLE_NAME + " (" +
                    TasksPersistenceContract.TaskEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_TASK_ID + TEXT_TYPE + SEPARATOR +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + SEPARATOR +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + SEPARATOR +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED + INTEGER_TYPE + SEPARATOR +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_PRIORITY + INTEGER_TYPE + SEPARATOR +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_DUE_DATE + INTEGER_TYPE +
                    " )";

    public static TasksDbHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TasksDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private TasksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
