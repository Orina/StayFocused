package me.elmira.stayfocused.data;

import java.util.ArrayList;

import me.elmira.stayfocused.data.source.local.TasksPersistenceContract;

/**
 * Created by elmira on 2/14/17.
 */

public class TaskListFilter {
    private final int mCompleted;
    private final String mLabelId;
    private final long mEqualDueDate;
    private final long mAfterDueDate;
    private final String selection;
    private final String[] selectionArgs;

    public static class Builder {
        private int mCompleted = -1;
        private String mLabelId;
        private long mEqualDueDate = 0;
        private long mAfterDueDate = 0;

        public Builder completed(boolean completed) {
            this.mCompleted = completed ? 1 : 0;
            return this;
        }

        public Builder equalDueDate(long dueDate) {
            this.mEqualDueDate = dueDate;
            return this;
        }

        public Builder afterDueDate(long dueDate) {
            this.mAfterDueDate = dueDate;
            return this;
        }

        public Builder label(String labelId) {
            this.mLabelId = labelId;
            return this;
        }

        public TaskListFilter build() {
            return new TaskListFilter(mCompleted, mLabelId, mEqualDueDate, mAfterDueDate);
        }
    }

    private TaskListFilter(int mCompleted, String mLabelId, long mEqualDueDate, long mAfterDueDate) {
        this.mCompleted = mCompleted;
        this.mLabelId = mLabelId;
        this.mEqualDueDate = mEqualDueDate;
        this.mAfterDueDate = mAfterDueDate;

        StringBuilder selectionBuilder = new StringBuilder();
        ArrayList<String> selectionArgsList = new ArrayList<>();
        boolean first = true;

        if (mCompleted != -1) {
            selectionBuilder.append(TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ? ");
            selectionArgsList.add(mCompleted == 1 ? "1" : "0");
            first = false;
        }

        selectionArgs = new String[selectionArgsList.size()];
        for (int i = 0; i < selectionArgsList.size(); i++) {
            selectionArgs[i] = selectionArgsList.get(i);
        }

        selection = selectionBuilder.toString();
    }

    public boolean isCompleted() {
        return mCompleted == 1;
    }

    public String getLabelId() {
        return mLabelId;
    }

    public long getEqualDueDate() {
        return mEqualDueDate;
    }

    public long getAfterDueDate() {
        return mAfterDueDate;
    }

    public String getSelection() {
        return selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }
}
