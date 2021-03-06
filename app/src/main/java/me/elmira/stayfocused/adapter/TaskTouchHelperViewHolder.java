package me.elmira.stayfocused.adapter;

/**
 * Created by elmira on 2/16/17.
 */

public interface TaskTouchHelperViewHolder {
    /**
     * Called when the { ItemTouchHelper} first registers an
     * item as being moved or swiped.
     * Implementations should update the item view to indicate
     * it's active state.
     */
    void onItemSelected();


    /**
     * Called when the { ItemTouchHelper} has completed the
     * move or swipe, and the active item state should be cleared.
     */
    void onItemClear();
}
