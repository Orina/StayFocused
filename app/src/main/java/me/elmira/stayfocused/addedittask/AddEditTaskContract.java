package me.elmira.stayfocused.addedittask;

import me.elmira.stayfocused.data.Task;

/**
 * Created by elmira on 2/6/17.
 */

public interface AddEditTaskContract {

    interface View {

        void setTitle(String title);

        void setDescription(String description);

        void setImage(String filePath);

        void setDueDate(String date, String time);

        void setPriority();

        void onLoadPriority();

        void onLoadDueDate();

        void onCompleteTask();

        void onDeleteTask();

        boolean isActive();

        void onResultBack();
    }

    interface Presenter {

        Task loadTask();

        void saveTaskTitle(String title);

        void saveDescription(String description);

        void setDueTime(String dueDate, String dueTime);

        void setPriority(String priority);

        boolean reloadTask();
    }
}
