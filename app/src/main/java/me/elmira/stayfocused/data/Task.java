package me.elmira.stayfocused.data;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by elmira on 1/19/17.
 */

public class Task implements Serializable {

    @NonNull
    private final String mId;

    private final String mTitle;

    private final String mDescription;

    private final boolean mCompleted;

    private List<Label> mLabels;

    private final Priority mPriority;
    private final int mPriorityId;

    private final long mDueDate;

    public static class Builder {
        private String mId;
        private String mTitle;
        private String mDescription;
        private boolean mCompleted;
        public List<Label> mLabels;
        private int mPriorityId;
        private long mDueDate = 0;

        public Builder() {
            this.mId = UUID.randomUUID().toString();
        }

        public Builder(String id) {
            this.mId = id;
        }

        public Builder(Task task) {
            this.mId = task.getId();
            this.mTitle = task.getTitle();
            this.mDescription = task.getDescription();
            this.mDueDate = task.getDueDate();
            this.mPriorityId = task.getPriorityId();
            this.mCompleted = task.isCompleted();
        }

        public Builder title(String title) {
            this.mTitle = title == null ? null : title.trim();
            return this;
        }

        public Builder description(String description) {
            this.mDescription = description == null ? null : description.trim();
            return this;
        }

        public Builder dueDate(long dueDate) {
            this.mDueDate = dueDate;
            return this;
        }

        public Builder priority(int priority) {
            this.mPriorityId = priority;
            return this;
        }

        public Builder completed(boolean completed) {
            this.mCompleted = completed;
            return this;
        }

        public Task build() {
            return new Task(mId, mTitle, mDescription, mCompleted, mPriorityId, mDueDate);
        }
    }

    private Task(@NonNull String id, String title, String description, boolean completed, int priorityId, long dueDate) {
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mPriorityId = priorityId;
        this.mPriority = Priority.getById(priorityId);
        this.mDueDate = dueDate;
        this.mCompleted = completed;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mTitle) &&
                Strings.isNullOrEmpty(mDescription);
    }

    public long getDueDate() {
        return mDueDate;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public int getPriorityId() {
        return mPriorityId;
    }

    public List<Label> getLabels() {
        return mLabels;
    }

    public boolean hasLabel(@NonNull String labelId) {
        if (Strings.isNullOrEmpty(labelId)) return false;

        if (mLabels == null || mLabels.size() == 0) return false;
        for (Label label : mLabels) {
            if (label.getId().equals(labelId)) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equal(mId, task.mId);
    }

    public boolean fullEquals(Task task) {
        if (mCompleted != task.mCompleted) return false;
        if (mPriorityId != task.mPriorityId) return false;
        if (mDueDate != task.mDueDate) return false;
        if (!mId.equals(task.mId)) return false;
        if (!mTitle.equals(task.mTitle)) return false;
        if (!mDescription.equals(task.mDescription)) return false;
        if (mLabels != null ? !mLabels.equals(task.mLabels) : task.mLabels != null) return false;
        return mPriority == task.mPriority;

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId);
    }

    @Override
    public String toString() {
        return "Task with id: " + mId + "and title " + mTitle;
    }
}