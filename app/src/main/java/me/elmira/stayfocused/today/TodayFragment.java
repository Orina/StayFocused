package me.elmira.stayfocused.today;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.elmira.stayfocused.R;
import me.elmira.stayfocused.adapter.TaskAdapter;
import me.elmira.stayfocused.data.Task;
import me.elmira.stayfocused.viewutil.ItemOffsetDecoration;

/**
 * Created by elmira on 1/19/17.
 */

public class TodayFragment extends Fragment implements TodayContract.View {

    private TaskAdapter mTaskAdapter;

    private RecyclerView mTasksRecyclerView;

    private View mNoTaskView;
    private TextView mNoTasksTextView;

    private int taskLayoutManager = 0;

    public static int TASK_LAYOUT_MANAGER_GRID = 0;
    public static int TASK_LAYOUT_MANAGER_LIST = 1;

    StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    StaggeredGridLayoutManager listLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

    private TodayContract.Presenter mPresenter;

    public TodayFragment() {

    }

    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskAdapter = new TaskAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        mTasksRecyclerView = (RecyclerView) root.findViewById(R.id.tasks_recycler_view);

        mTasksRecyclerView.setAdapter(mTaskAdapter);
        mTasksRecyclerView.setLayoutManager(gridLayoutManager);
        mTasksRecyclerView.addItemDecoration(new ItemOffsetDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.margin_tiny)));

        mNoTaskView = root.findViewById(R.id.noTasks);
        mNoTasksTextView = (TextView) root.findViewById(R.id.noTasksText);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(TodayContract.Presenter presenter) {
        this.mPresenter = presenter;
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
    public void showAddTask() {

    }

    @Override
    public void showTaskDetailsUi(String taskId) {

    }

    public int changeLayout() {
        if (taskLayoutManager == TASK_LAYOUT_MANAGER_GRID)
            taskLayoutManager = TASK_LAYOUT_MANAGER_LIST;
        else taskLayoutManager = TASK_LAYOUT_MANAGER_GRID;

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

    @Override
    public void showActiveFilter() {

    }

    @Override
    public void showCompletedFilter() {

    }

    @Override
    public void showAllFilter() {

    }

    @Override
    public void showLabelFilter() {

    }

    @Override
    public void showNoActiveTasks() {
        showNoTasksViews(getContext().getResources().getString(R.string.no_tasks_active));
    }

    @Override
    public void showNoCompletedTasks() {
        showNoTasksViews(getContext().getResources().getString(R.string.no_tasks_completed));
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(getContext().getResources().getString(R.string.no_tasks_all));
    }

    @Override
    public void showNoLabelTasks() {
        showNoTasksViews(getContext().getResources().getString(R.string.no_tasks_label));
    }

    private void showNoTasksViews(String mainText) {
        mTasksRecyclerView.setVisibility(View.GONE);
        mNoTaskView.setVisibility(View.VISIBLE);
        mNoTasksTextView.setText(mainText);
    }
}
