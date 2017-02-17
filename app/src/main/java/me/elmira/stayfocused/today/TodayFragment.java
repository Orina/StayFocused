package me.elmira.stayfocused.today;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import me.elmira.stayfocused.util.SnackbarUtil;
import me.elmira.stayfocused.viewutil.ItemOffsetDecoration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 1/19/17.
 */

public class TodayFragment extends Fragment implements TodayContract.View {

    private static final String LOG_TAG = "TodayFragment";

    private TaskAdapter mTaskAdapter;
    private RecyclerView mTasksRecyclerView;

    private View mNoTaskView;
    private TextView mNoTasksTextView;

    private int taskLayoutManager = 0;

    public static int TASK_LAYOUT_MANAGER_GRID = 0;
    public static int TASK_LAYOUT_MANAGER_LIST = 1;

    private StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    private StaggeredGridLayoutManager listLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

    private TodayContract.Presenter mPresenter;
    private View mRootView;

    public TodayFragment() {

    }

    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG, "onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
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
        mRootView = inflater.inflate(R.layout.fragment_today, container, false);
        mTasksRecyclerView = (RecyclerView) mRootView.findViewById(R.id.tasks_recycler_view);

        mTasksRecyclerView.setAdapter(mTaskAdapter);
        mTasksRecyclerView.setLayoutManager(gridLayoutManager);
        mTasksRecyclerView.addItemDecoration(new ItemOffsetDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.margin_tiny)));

        new ItemTouchHelper(new TaskTouchHelperCallback(mTaskAdapter)).attachToRecyclerView(mTasksRecyclerView);

        mNoTaskView = mRootView.findViewById(R.id.noTasks);
        mNoTasksTextView = (TextView) mNoTaskView.findViewById(R.id.noTasksText);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addNewTask();
            }
        });

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()");
        super.onDestroy();
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
    public void setPresenter(TodayContract.Presenter presenter) {
        Log.d(LOG_TAG, "setPresenter()");
        this.mPresenter = checkNotNull(presenter);
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
    public void showAddTaskUI() {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult, requestCode: " + requestCode + ", resultCode: " + resultCode);
        mPresenter.result(requestCode, resultCode, data);
    }

    @Override
    public void showTaskDetailsUI(@NonNull String taskId, @NonNull View cardView) {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_TASK_ID, checkNotNull(taskId));
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_EDIT_TASK,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this.getActivity(),
                        new Pair<View, String>(cardView, getString(R.string.transition_task))).toBundle());
    }

    @Override
    public void onTaskCreated(@NonNull Task task) {
        checkNotNull(task);
        SnackbarUtil.showSnackbar(mRootView, R.string.success_task_created, Snackbar.LENGTH_LONG);
        mTaskAdapter.addTask(task);
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
    public void onTaskCompleted(@NonNull String taskId) {
        checkNotNull(taskId);
        SnackbarUtil.showSnackbar(mRootView, R.string.success_task_completed, Snackbar.LENGTH_LONG);
        mTaskAdapter.removeTask(taskId);
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(getContext().getResources().getString(R.string.no_tasks_active));
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

    private void showNoTasksViews(String mainText) {
        mTasksRecyclerView.setVisibility(View.GONE);
        mNoTaskView.setVisibility(View.VISIBLE);
        mNoTasksTextView.setText(mainText);
    }
}