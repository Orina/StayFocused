package me.elmira.stayfocused.data;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.List;
import java.util.UUID;

/**
 * Created by elmira on 1/19/17.
 */

public class Task {

    @NonNull
    private final String mId;

    @NonNull
    private final String mTitle;

    @NonNull
    private final String mDescription;

    private final boolean mCompleted;

    private final int mProgress;

    public List<Label> mLabels;

    private final Priority mPriority;

    public String mImageRes = null;

    public Task(@NonNull String mTitle, @NonNull String mDescription) {
        this(UUID.randomUUID().toString(), mTitle, mDescription, false, 0, null, null, null);
    }

    public Task(@NonNull String title, @NonNull String description, boolean completed, int progress) {
        this(UUID.randomUUID().toString(), title, description, completed, progress, null, null, null);
    }

    public Task(@NonNull String id, @NonNull String title, @NonNull String description, boolean completed, int progress, List<Label> labels, Priority priority, String imageRes) {
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mCompleted = completed;
        this.mProgress = progress;
        this.mLabels = labels;
        this.mPriority = priority;
        this.mImageRes = imageRes;
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

    public int getProgress() {
        return mProgress;
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

    public boolean hasImage() {
        return mImageRes != null;
    }

    public Priority getPriority() {
        return mPriority;
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
        return Objects.equal(mId, task.mId) &&
                Objects.equal(mTitle, task.mTitle) &&
                Objects.equal(mDescription, task.mDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription);
    }

    @Override
    public String toString() {
        return "Task with id: " + mId + "and title " + mTitle;
    }
}