package me.elmira.stayfocused.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by elmira on 2/14/17.
 */

public class TasksPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TasksPersistenceContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TASK_ID = "task_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
    }

    public static String[] TASK_ENTRY_ALL_COLUMNS_PROJECTION = {
            TaskEntry.COLUMN_NAME_TASK_ID,
            TaskEntry.COLUMN_NAME_TITLE,
            TaskEntry.COLUMN_NAME_DESCRIPTION,
            TaskEntry.COLUMN_NAME_COMPLETED,
            TaskEntry.COLUMN_NAME_PRIORITY,
            TaskEntry.COLUMN_NAME_DUE_DATE
    };
}
