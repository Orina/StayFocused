package me.elmira.stayfocused.today;

import android.support.annotation.NonNull;

import java.util.List;

import me.elmira.stayfocused.BasePresenter;
import me.elmira.stayfocused.BaseView;
import me.elmira.stayfocused.data.Task;

/**
 * Created by elmira on 1/31/17.
 */

public interface TodayContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        boolean isActive();

        void showTasks(List<Task> tasks);

        void showActiveFilter();

        void showCompletedFilter();

        void showAllFilter();

        void showLabelFilter();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showNoTasks();

        void showNoLabelTasks();

        void showAddTask();

        void showTaskDetailsUi(String taskId);
    }

    interface Presenter extends BasePresenter {

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openTaskDetails(@NonNull Task requestedTask);

    }
}
