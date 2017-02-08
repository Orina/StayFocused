package me.elmira.stayfocused.today;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import me.elmira.stayfocused.data.Task;
import me.elmira.stayfocused.data.source.TasksDataSource;
import me.elmira.stayfocused.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;
import static me.elmira.stayfocused.today.TodayTasksFilterType.ACTIVE_TASKS;
import static me.elmira.stayfocused.today.TodayTasksFilterType.COMPLETED_TASKS;

/**
 * Created by elmira on 1/31/17.
 */

public class TodayPresenter implements TodayContract.Presenter {

    private final TasksRepository mTasksRepository;

    private final TodayContract.View mTodayView;

    private boolean mFirstLoad = true;

    private TodayTasksFilterType mCurrentTasksFilter = TodayTasksFilterType.ALL_TASKS;

    public TodayPresenter(@NonNull TasksRepository mTasksRepository, @NonNull TodayContract.View mTodayView) {
        this.mTasksRepository = checkNotNull(mTasksRepository, "tasks repositore can't be null");
        this.mTodayView = checkNotNull(mTodayView, "task view can not be null");

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
            mTodayView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }
        mTasksRepository.loadTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                // The view may not be able to handle UI updates anymore
                if (!mTodayView.isActive()) return;

                List<Task> tasksToShow = new ArrayList<Task>();

                if (mCurrentTasksFilter == TodayTasksFilterType.ALL_TASKS) {
                    tasksToShow.addAll(tasks);
                } else if (mCurrentTasksFilter == ACTIVE_TASKS) {
                    for (Task task : tasks) {
                        if (task.isActive()) tasksToShow.add(task);
                    }
                } else if (mCurrentTasksFilter == COMPLETED_TASKS) {
                    for (Task task : tasks) {
                        if (task.isCompleted()) tasksToShow.add(task);
                    }
                } else if (mCurrentTasksFilter == TodayTasksFilterType.LABEL_TASKS) {
                    for (Task task : tasks) {
                        if (task.hasLabel(mCurrentTasksFilter.getLabelId())) tasksToShow.add(task);
                    }
                }
                if (showLoadingUI) {
                    mTodayView.setLoadingIndicator(false);
                }
                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (mTodayView.isActive()) return;
                processEmptyTasks();
            }
        });
    }


    @Override
    public void addNewTask() {

    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {

    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            mTodayView.showTasks(tasks);
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrentTasksFilter) {
            case ACTIVE_TASKS:
                mTodayView.showActiveFilter();
                break;
            case COMPLETED_TASKS:
                mTodayView.showCompletedFilter();
                break;
            case LABEL_TASKS:
                mTodayView.showLabelFilter();
                break;
            default:
                mTodayView.showAllFilter();
                break;
        }
    }

    private void processEmptyTasks() {
        switch (mCurrentTasksFilter) {
            case ACTIVE_TASKS:
                mTodayView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTodayView.showNoCompletedTasks();
                break;
            case LABEL_TASKS:
                mTodayView.showNoLabelTasks();
                break;
            default:
                mTodayView.showNoTasks();
                break;
        }
    }
}
