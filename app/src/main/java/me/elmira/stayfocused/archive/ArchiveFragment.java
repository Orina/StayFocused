package me.elmira.stayfocused.archive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.elmira.stayfocused.R;
import me.elmira.stayfocused.adapter.TaskAdapter;
import me.elmira.stayfocused.adapter.TaskTouchHelperCallback;
import me.elmira.stayfocused.addedittask.AddEditTaskActivity;
import me.elmira.stayfocused.addedittask.AddEditTaskFragment;
import me.elmira.stayfocused.data.Task;
import me.elmira.stayfocused.today.TodayFragment;
import me.elmira.stayfocused.util.SnackbarUtil;
import me.elmira.stayfocused.viewutil.ItemOffsetDecoration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 2/16/17.
 */

public class ArchiveFragment extends Fragment implements ArchiveContract.View {

    private static final String LOG_TAG = "ArchiveFragment";

    private TaskAdapter mTaskAdapter;
    private RecyclerView mTasksRecyclerView;

    private View mNoTaskView;
    private TextView mNoTasksTextView;

    private int taskLayoutManager = 0;

    public static int TASK_LAYOUT_MANAGER_GRID = 0;
    public static int TASK_LAYOUT_MANAGER_LIST = 1;

    private StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    private StaggeredGridLayoutManager listLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

    private ArchiveContract.Presenter mPresenter;
    private View mRootView;

    public ArchiveFragment() {

    }

    public static ArchiveFragment newInstance() {
        return new ArchiveFragment();
    }

    @Override
    public void setPresenter(@NonNull ArchiveContract.Presenter presenter) {
        this.mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTaskAdapter = new TaskAdapter(getActivity());
        mTaskAdapter.setClickListener(mPresenter);
        mTaskAdapter.setTouchListener(mPresenter);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()");
        mRootView = inflater.inflate(R.layout.fragment_archive, container, false);
        mTasksRecyclerView = (RecyclerView) mRootView.findViewById(R.id.tasks_recycler_view);

        mTasksRecyclerView.setAdapter(mTaskAdapter);
        mTasksRecyclerView.setLayoutManager(gridLayoutManager);
        mTasksRecyclerView.addItemDecoration(new ItemOffsetDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.margin_tiny)));

        new ItemTouchHelper(new TaskTouchHelperCallback(mTaskAdapter)).attachToRecyclerView(mTasksRecyclerView);

        mNoTaskView = mRootView.findViewById(R.id.noTasks);
        mNoTasksTextView = (TextView) mNoTaskView.findViewById(R.id.noTasksText);

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadTasks(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_layout_id:
                int type = changeLayout();
                if (type == TodayFragment.TASK_LAYOUT_MANAGER_GRID) {
                    item.setIcon(R.drawable.ic_view_stream_white_24dp);
                }
                else if (type == TodayFragment.TASK_LAYOUT_MANAGER_LIST) {
                    item.setIcon(R.drawable.ic_dashboard_white_24dp);
                }
                return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode, data);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mTaskAdapter.updateData(tasks);
        mTasksRecyclerView.setVisibility(View.VISIBLE);
        mNoTaskView.setVisibility(View.GONE);
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(getContext().getResources().getString(R.string.no_tasks_active));
    }

    @Override
    public void showTaskDetailsUI(@NonNull String taskId, @NonNull View view) {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_TASK_ID, checkNotNull(taskId));
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_EDIT_TASK,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this.getActivity(),
                        new Pair<View, String>(view, getString(R.string.transition_task))).toBundle());
    }

    @Override
    public void onTaskUpdated(@NonNull Task task) {
        checkNotNull(task);
        SnackbarUtil.showSnackbar(mRootView, R.string.success_task_saved, Snackbar.LENGTH_LONG);
        mTaskAdapter.updateTask(task);
    }

    @Override
    public void onTaskDeleted(@NonNull String taskId) {
        checkNotNull(taskId);
        SnackbarUtil.showSnackbar(mRootView, R.string.success_task_deleted, Snackbar.LENGTH_LONG);
        mTaskAdapter.removeTask(taskId);
    }

    @Override
    public void onTaskActivated(@NonNull String taskId) {
        checkNotNull(taskId);
        SnackbarUtil.showSnackbar(mRootView, R.string.success_task_activated, Snackbar.LENGTH_LONG);
        mTaskAdapter.removeTask(taskId);
    }

    private void showNoTasksViews(String mainText) {
        mTasksRecyclerView.setVisibility(View.GONE);
        mNoTaskView.setVisibility(View.VISIBLE);
        mNoTasksTextView.setText(mainText);
    }

    private int changeLayout() {
        taskLayoutManager = (taskLayoutManager == TASK_LAYOUT_MANAGER_GRID) ? TASK_LAYOUT_MANAGER_LIST : TASK_LAYOUT_MANAGER_GRID;

        switch (taskLayoutManager) {
            case 0:
                mTasksRecyclerView.setLayoutManager(gridLayoutManager);
                break;
            case 1:
                mTasksRecyclerView.setLayoutManager(listLayoutManager);
                break;
        }
        return taskLayoutManager;
    }
}
