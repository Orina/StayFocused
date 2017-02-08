package me.elmira.stayfocused.addedittask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by elmira on 2/6/17.
 */

public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void setImage(String filePath) {

    }

    @Override
    public void setDueDate(String date, String time) {

    }

    @Override
    public void setPriority() {

    }

    @Override
    public void onLoadPriority() {

    }

    @Override
    public void onLoadDueDate() {

    }

    @Override
    public void onCompleteTask() {

    }

    @Override
    public void onDeleteTask() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onResultBack() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
