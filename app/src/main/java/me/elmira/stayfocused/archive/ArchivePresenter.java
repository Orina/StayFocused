package me.elmira.stayfocused.archive;

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
 * Created by elmira on 2/16/17.
 */

public class ArchivePresenter implements ArchiveContract.Presenter {

    private TasksRepository mTasksRepository;

    private ArchiveContract.View mView;

    private boolean mFirstLoad = true;

    public ArchivePresenter(@NonNull TasksRepository mTasksRepository, @NonNull ArchiveContract.View mView) {
        this.mTasksRepository = checkNotNull(mTasksRepository);
        this.mView = checkNotNull(mView);
        mView.setPresenter(this);
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

    @Override
    public void onTaskClick(Task task, View cardView) {
        mView.showTaskDetailsUI(task.getId(), cardView);
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (AddEditTaskActivity.REQUEST_EDIT_TASK == requestCode) {
            int extraAction = data.getIntExtra(AddEditTaskActivity.EXTRA_ACTION, 0);

            if (extraAction == AddEditTaskActivity.EXTRA_DELETED_TASK) {
                String taskId = data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_ID);
                if (taskId != null) {
                    mView.onTaskDeleted(taskId);
                }
            }
            else if (extraAction == AddEditTaskActivity.EXTRA_ACTIVATED_TASK) {
                String taskId = data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_ID);
                if (taskId != null) {
                    mView.onTaskActivated(taskId);
                }
            }
            else if (extraAction == AddEditTaskActivity.EXTRA_UPDATED_TASK) {
                Task task = (Task) data.getSerializableExtra(AddEditTaskActivity.EXTRA_TASK);
                if (task != null) {
                    mView.onTaskUpdated(task);
                }
            }
        }
    }

    @Override
    public void onTaskDismiss(Task task) {
        mTasksRepository.deleteTask(task.getId());
        mView.onTaskDeleted(task.getId());
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }
        TaskListFilter filter = new TaskListFilter.Builder().completed(true).build();

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