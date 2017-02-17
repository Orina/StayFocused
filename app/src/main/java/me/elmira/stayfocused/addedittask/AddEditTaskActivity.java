package me.elmira.stayfocused.addedittask;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.elmira.stayfocused.Injection;
import me.elmira.stayfocused.R;
import me.elmira.stayfocused.util.ActivityUtils;

/**
 * Created by elmira on 2/6/17.
 */

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String LOG_TAG = "AddEditTaskActivity";

    public static final String LOAD_DATA_KEY = "LOAD_DATA_KEY";
    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_TASK = "EXTRA_TASK";
    public static final String EXTRA_TASK_ID = "EXTRA_TASK_ID";

    public static final int REQUEST_ADD_TASK = 1;
    public static final int REQUEST_EDIT_TASK = 2;

    public static final int EXTRA_DELETED_TASK = 3;
    public static final int EXTRA_COMPLETED_TASK = 4;
    public static final int EXTRA_ACTIVATED_TASK = 5;
    public static final int EXTRA_UPDATED_TASK = 6;

    private AddEditTaskContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);

        String taskId = getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_TASK_ID);

        AddEditTaskFragment addEditTaskFragment = (AddEditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditTaskFragment.newInstance();
            if (getIntent().hasExtra(AddEditTaskFragment.ARGUMENT_TASK_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddEditTaskFragment.ARGUMENT_TASK_ID, taskId);
                addEditTaskFragment.setArguments(bundle);
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), addEditTaskFragment, R.id.content_frame);
        }

        boolean loadData = true;
        if (savedInstanceState != null) {
            loadData = savedInstanceState.getBoolean(LOAD_DATA_KEY);
        }

        mPresenter = new AddEditTaskPresenter(taskId, Injection.provideTasksRepository(getApplicationContext()), addEditTaskFragment, loadData);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mPresenter != null) {
            outState.putBoolean(LOAD_DATA_KEY, mPresenter.reloadTask());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
