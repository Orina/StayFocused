package me.elmira.stayfocused.addedittask;

import me.elmira.stayfocused.BasePresenter;
import me.elmira.stayfocused.BaseView;
import me.elmira.stayfocused.data.Priority;
import me.elmira.stayfocused.data.Task;

/**
 * Created by elmira on 2/6/17.
 */

public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {

        void setTitle(String title);

        void setDescription(String description);

        void setDueDate(long time);

        void setPriority(Priority priority);

        void setCompleteStatus(boolean isCompleted);

        void onCompleteTask(String taskId);

        void onActivateTask(String taskId);

        void onDeleteTask(String taskId);

        boolean isActive();

        void onResultBack(Task task);

        void prepareNewTaskView();
    }

    interface Presenter extends BasePresenter {

        void loadTask();

        Task updateTask(String title, String description, int priority, long dueDate);

        void deleteTask();

        boolean reloadTask();

        void completeTask(String title, String description, int priority, long dueDate);

        void activateTask(String title, String description, int priority, long dueDate);

        boolean isNewTask();

        void onBackButtonPressed(String title, String description, int priority, long dueDate);
    }
}
