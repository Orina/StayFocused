package me.elmira.stayfocused.today;

/**
 * Created by elmira on 1/31/17.
 */

public enum TodayTasksFilterType {

    ALL_TASKS,

    ACTIVE_TASKS,

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS,

    LABEL_TASKS;

    private String labelId;

    TodayTasksFilterType(){

    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }
}
