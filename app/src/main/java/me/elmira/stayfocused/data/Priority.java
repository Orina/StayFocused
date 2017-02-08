package me.elmira.stayfocused.data;

/**
 * Created by elmira on 1/26/17.
 */

public enum Priority {

    HIGH(1, 0),
    NORMAL(0, 0),
    LOW(2, 0);

    private int id;
    private int titleResId;

    Priority(int id, int titleResId) {
        this.id = id;
        this.titleResId = titleResId;
    }
}
