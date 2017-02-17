package me.elmira.stayfocused.addedittask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.elmira.stayfocused.R;
import me.elmira.stayfocused.data.Priority;
import me.elmira.stayfocused.data.Task;

import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 2/6/17.
 */

public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View, DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = AddEditTaskFragment.class.getName();

    public static final String ARGUMENT_TASK_ID = "taskId";

    private AddEditTaskContract.Presenter mPresenter;

    private EditText mTitleText;
    private EditText mDescriptionText;
    private DatePickerDialog mDatePickerDialog;
    private Calendar mCalendar = Calendar.getInstance();
    private View mDueDateView;
    private TextView mDueDateDesc;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
    private ImageView mPriorityIconView;
    private Menu mMenu;

    private long mDueDate = 0;
    private int mPriorityId;

    private boolean mCompleteStatus = false;

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addedit_task, container, false);

        mTitleText = (EditText) root.findViewById(R.id.task_title);
        mDescriptionText = (EditText) root.findViewById(R.id.task_description);
        mDatePickerDialog = new DatePickerDialog(getContext(), R.style.StayFocusedTheme_DialogPicker, this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));
        mDueDateView = root.findViewById(R.id.dueDatePicker);
        mDueDateDesc = (TextView) mDueDateView.findViewById(R.id.dueDateDesc);
        mPriorityIconView = (ImageView) root.findViewById(R.id.priorityIcon);

        mDatePickerDialog.setButton(BUTTON_NEUTRAL, getText(R.string.button_clear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == BUTTON_POSITIVE) {
                    mDatePickerDialog.getDatePicker().clearFocus();
                    onDateSet(mDatePickerDialog.getDatePicker(), mDatePickerDialog.getDatePicker().getYear(),
                            mDatePickerDialog.getDatePicker().getMonth(), mDatePickerDialog.getDatePicker().getDayOfMonth());
                }
                else if (which == BUTTON_NEUTRAL) {
                    mDatePickerDialog.getDatePicker().clearFocus();
                    onDateSet(mDatePickerDialog.getDatePicker(), 0, 0, 0);
                }
            }
        });
        mDueDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePickerDialog.setTitle("");
                mDatePickerDialog.show();
            }
        });

        mPriorityIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePriority();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_TAG, "Menu is being setup");

        getActivity().getMenuInflater().inflate(R.menu.menu_addedit_task, menu);
        mMenu = menu;

        updateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.completeTaskMenu:
                String title = mTitleText.getText().toString();
                String description = mDescriptionText.getText().toString();
                mPresenter.completeTask(title, description, mPriorityId, mDueDate);
                return true;
            case R.id.activateTaskMenu:
                title = mTitleText.getText().toString();
                description = mDescriptionText.getText().toString();
                mPresenter.activateTask(title, description, mPriorityId, mDueDate);
                return true;
            case R.id.deleteTaskMenu:
                mPresenter.deleteTask();
                return true;
            case android.R.id.home:
                title = mTitleText.getText().toString();
                description = mDescriptionText.getText().toString();
                mPresenter.onBackButtonPressed(title, description, mPriorityId, mDueDate);
                return true;
        }
        return false;
    }


    @Override
    public void setPresenter(@NonNull AddEditTaskContract.Presenter mPresenter) {
        this.mPresenter = checkNotNull(mPresenter);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if (i == 0) {
            mDueDate = 0;
            mDueDateDesc.setText(getText(R.string.task_due_date));
        }
        else {
            mCalendar.set(Calendar.YEAR, i);
            mCalendar.set(Calendar.MONTH, i1);
            mCalendar.set(Calendar.DAY_OF_MONTH, i2);

            mDueDate = mCalendar.getTimeInMillis();
            mDueDateDesc.setText(simpleDateFormat.format(mCalendar.getTime()));
        }
    }

    @Override
    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    @Override
    public void setDescription(String description) {
        mDescriptionText.setText(description);
    }

    @Override
    public void setDueDate(long time) {
        mDueDate = time;

        if (time == 0) mCalendar.setTime(new Date());
        else mCalendar.setTimeInMillis(time);

        mDatePickerDialog.updateDate(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.setTitle("");

        mDueDateDesc.setText(time > 0 ? simpleDateFormat.format(mCalendar.getTime()) : getText(R.string.task_due_date));
    }

    @Override
    public void setPriority(Priority priority) {
        mPriorityId = priority.getId();
        updatePriorityIcon();
    }

    private void changePriority() {
        if (mPriorityId == Priority.HIGH.getId()) mPriorityId = Priority.NORMAL.getId();
        else mPriorityId = Priority.HIGH.getId();

        updatePriorityIcon();
    }

    private void updatePriorityIcon() {
        if (mPriorityId == Priority.HIGH.getId()) {
            mPriorityIconView.getDrawable().mutate().setColorFilter(getContext().getResources().getColor(R.color.colorAccentLight), PorterDuff.Mode.SRC_IN);
        }
        else {
            mPriorityIconView.getDrawable().mutate().setColorFilter(getContext().getResources().getColor(R.color.colorPrimaryLight), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void setCompleteStatus(boolean isCompleted) {
        this.mCompleteStatus = isCompleted;
        updateOptionsMenu();
    }

    private void updateOptionsMenu() {
        if (mMenu == null) {
            Log.d(LOG_TAG, "menu is not defined");
            return;
        }
        if (mPresenter.isNewTask()) {
            mMenu.removeItem(R.id.deleteTaskMenu);
            mMenu.removeItem(R.id.activateTaskMenu);
        }
        else if (mCompleteStatus) {
            mMenu.removeItem(R.id.completeTaskMenu);
        }
        else {
            mMenu.removeItem(R.id.activateTaskMenu);
        }
    }

    @Override
    public void prepareNewTaskView() {
        if (mMenu != null) {
            mMenu.removeItem(R.id.deleteTaskMenu);
            mMenu.removeItem(R.id.activateTaskMenu);
        }
    }

    @Override
    public void onCompleteTask(String taskId) {
        finishWithExtraAction(AddEditTaskActivity.EXTRA_COMPLETED_TASK, AddEditTaskActivity.EXTRA_TASK_ID, taskId);
    }

    @Override
    public void onActivateTask(String taskId) {
        finishWithExtraAction(AddEditTaskActivity.EXTRA_ACTIVATED_TASK, AddEditTaskActivity.EXTRA_TASK_ID, taskId);
    }

    @Override
    public void onDeleteTask(String taskId) {
        finishWithExtraAction(AddEditTaskActivity.EXTRA_DELETED_TASK, AddEditTaskActivity.EXTRA_TASK_ID, taskId);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onResultBack(Task task) {
        Log.d(LOG_TAG, "onResultBack()");
        finishWithExtraAction(AddEditTaskActivity.EXTRA_UPDATED_TASK, AddEditTaskActivity.EXTRA_TASK, task);
    }

    private void finishWithExtraAction(int action, String extraName, Serializable extraObject) {
        if (extraObject == null) {
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finishAfterTransition();
            return;
        }

        Intent data = new Intent();
        data.putExtra(AddEditTaskActivity.EXTRA_ACTION, action);
        data.putExtra(extraName, extraObject);

        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finishAfterTransition();
    }
}