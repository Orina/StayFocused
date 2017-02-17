package me.elmira.stayfocused.data;

import me.elmira.stayfocused.R;

/**
 * Created by elmira on 1/26/17.
 */

public enum Priority {
    HIGH(1, R.string.priority_high),
    NORMAL(0, R.string.priority_normal);

    private int mId;
    private int mTitleResId;

    Priority(int id, int resId) {
        this.mId = id;
        this.mTitleResId = resId;
    }

    public Priority getPriorityById(int id) {
        if (HIGH.mId == id) return HIGH;
        else if (NORMAL.mId == id) return NORMAL;
        return NORMAL;
    }

    public int getId() {
        return mId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public static Priority getById(int id){
        if (id==HIGH.getId()) return HIGH;
        else if (id==NORMAL.getId()) return NORMAL;
        return null;
    }
}
