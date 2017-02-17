package me.elmira.stayfocused.today;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import me.elmira.stayfocused.addedittask.AddEditTaskActivity;
import me.elmira.stayfocused.data.Task;
import me.elmira.stayfocused.data.TaskListFilter;
import me.elmira.stayfocused.data.source.TasksDataSource;
import me.elmira.stayfocused.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 1/31/17.
 */

public class TodayPresenter implements TodayContract.Presenter {

    private static final String LOG_TAG = "TodayPresenter";

    private final TasksRepository mTasksRepository;

    private final TodayContract.View mView;

    private boolean mFirstLoad = true;

    public TodayPresenter(@NonNull TasksRepository mTasksRepository, @NonNull TodayContract.View mTodayView) {
        this.mTasksRepository = checkNotNull(mTasksRepository, "tasks repository can't be null");
        this.mView = checkNotNull(mTodayView, "task view can not be null");
        mTodayView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }

        TaskListFilter filter = new TaskListFilter.Builder().completed(false).build();
        mTasksRepository.loadTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                // The view may not be able to handle UI updates anymore
                if (!mView.isActive()) return;

                if (showLoadingUI) {
                    mView.setLoadingIndicator(false);
                }
                processTasks(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) return;
                processEmptyTasks();
            }
        }, filter);
    }

    @Override
    public void addNewTask() {
        mView.showAddTaskUI();
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode) {
            Task task = (Task) data.getSerializableExtra(AddEditTaskActivity.EXTRA_TASK);
            if (task != null) {
                //mTasksRepository.updateCachedTask(task);
                mView.onTaskCreated(task);
            }
        }
        else if (AddEditTaskActivity.REQUEST_EDIT_TASK == requestCode) {
            int extraAction = data.getIntExtra(AddEditTaskActivity.EXTRA_ACTION, 0);

            if (extraAction == AddEditTaskActivity.EXTRA_DELETED_TASK) {
                String taskId = data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_ID);
                if (taskId != null) {
                    //mTasksRepository.removeCachedTask(taskId);
                    mView.onTaskDeleted(taskId);
                }
            }
            else if (extraAction == AddEditTaskActivity.EXTRA_COMPLETED_TASK) {
                String taskId = data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_ID);
                if (taskId != null) {
                    //mTasksRepository.updateCachedTask(task);
                    mView.onTaskCompleted(taskId);
                }
            }
            else if (extraAction == AddEditTaskActivity.EXTRA_UPDATED_TASK) {
                Task task = (Task) data.getSerializableExtra(AddEditTaskActivity.EXTRA_TASK);
                if (task != null) {
                    //mTasksRepository.updateCachedTask(task);
                    mView.onTaskUpdated(task);
                }
            }
        }
    }

    @Override
    public void onTaskClick(Task task, View cardView) {
        mView.showTaskDetailsUI(task.getId(), cardView);
    }

    @Override
    public void onTaskDismiss(Task task) {
        mTasksRepository.completeTask(task.getId());
        mView.onTaskCompleted(task.getId());
    }

    private void processTasks(List<Task> tasks) {
        if (!mView.isActive()) return;
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        }
        else {
            // Show the list of tasks
            mView.showTasks(tasks);
        }
    }

    private void processEmptyTasks() {
        mView.showNoTasks();
    }
}