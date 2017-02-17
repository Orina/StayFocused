package me.elmira.stayfocused.util;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import me.elmira.stayfocused.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 2/13/17.
 */

public class SnackbarUtil {

    public static void showSnackbar(@NonNull View parentView, int stringId, int duration) {
        checkNotNull(parentView);
        Snackbar snackbar = Snackbar.make(parentView, stringId, duration);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(parentView.getResources().getColor(R.color.colorPrimary));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(parentView.getResources().getColor(android.R.color.white));
        snackbar.show();
    }
}
