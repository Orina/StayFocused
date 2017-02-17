package me.elmira.stayfocused.adapter;

/**
 * Created by elmira on 2/16/17.
 */

public interface TaskTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
