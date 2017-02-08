package me.elmira.stayfocused;

import android.content.Context;
import android.support.annotation.NonNull;

import me.elmira.stayfocused.data.source.TasksRepository;
import me.elmira.stayfocused.data.source.local.TasksLocalDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by elmira on 1/31/17.
 */

public class Injection {
    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return TasksRepository.getInstance(TasksLocalDataSource.getInstance(context));
    }
}
