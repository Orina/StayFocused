package me.elmira.stayfocused.addedittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import me.elmira.stayfocused.data.Task;
import me.elmira.stayfocused.data.source.TasksDataSource;
import me.elmira.stayfocused.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 2/8/17.
 */

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter, TasksDataSource.GetTaskCallback {

    @Nullable
    private String mTaskId;

    @NonNull
    private TasksRepository mTasksRepository;

    @NonNull
    private AddEditTaskContract.View mView;

    private boolean mReloadTask;

    public AddEditTaskPresenter(@Nullable String mTaskId, @NonNull TasksRepository mTasksRepository, @NonNull AddEditTaskContract.View mView, boolean mReloadTask) {
        this.mTaskId = mTaskId;
        this.mView = checkNotNull(mView, "add-edit task view can't be null");
        this.mTasksRepository = checkNotNull(mTasksRepository, "tasks repository can't be null");
        this.mReloadTask = mReloadTask;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (isNewTask()) {
            mView.prepareNewTaskView();
            return;
        }
        if (!isNewTask() && mReloadTask) {
            loadTask();
        }
    }

    @Override
    public void loadTask() {
        if (isNewTask()) return;
        mTasksRepository.getTask(mTaskId, this);
    }

    @Override
    public Task updateTask(String title, String description, int priority, long dueDate) {
        if (isNewTask() && Strings.isNullOrEmpty(title) && Strings.isNullOrEmpty(description)) {
            return null;
        }
        final Task task;

        if (isNewTask()) {
            task = new Task.Builder().title(title).description(description).priority(priority).dueDate(dueDate).build();
            mTasksRepository.insertTask(task);
        }
        else {
            task = new Task.Builder(mTaskId).title(title).description(description).priority(priority).dueDate(dueDate).build();
            Task oldTask = mTasksRepository.getCachedTask(mTaskId);
            if (oldTask != null && task.fullEquals(oldTask)) {
                return null;
            }
            mTasksRepository.updateTask(task);
        }
        return task;
    }

    @Override
    public void deleteTask() {
        if (isNewTask()) {
            return;
        }
        mTasksRepository.deleteTask(mTaskId);
        mView.onDeleteTask(mTaskId);
    }

    @Override
    public void completeTask(String title, String description, int priority, long dueDate) {
        Task task = updateTask(title, description, priority, dueDate);
        if (isNewTask() && task != null) {
            mView.onCompleteTask(task.getId());
            return;
        }
        mTasksRepository.completeTask(mTaskId);
        mView.onCompleteTask(mTaskId);
    }

    @Override
    public boolean reloadTask() {
        return mReloadTask;
    }

    @Override
    public void onTaskLoaded(Task task) {
        if (!mView.isActive()) return;

        mView.setTitle(task.getTitle());
        mView.setDescription(task.getDescription());
        mView.setDueDate(task.getDueDate());
        mView.setPriority(task.getPriority());
        mView.setCompleteStatus(task.isCompleted());

        mReloadTask = false;
    }

    @Override
    public void activateTask(String title, String description, int priority, long dueDate) {
        if (isNewTask()) return;
        Task task = updateTask(title, description, priority, dueDate);
        mTasksRepository.activateTask(mTaskId);
        mView.onActivateTask(mTaskId);
    }

    @Override
    public void onDataNotAvailable() {
        mReloadTask = true;
    }

    @Override
    public boolean isNewTask() {
        return Strings.isNullOrEmpty(mTaskId);
    }

    @Override
    public void onBackButtonPressed(String title, String description, int priority, long dueDate) {
        Task task = updateTask(title, description, priority, dueDate);
        mView.onResultBack(task);
    }
}