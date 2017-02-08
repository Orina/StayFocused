package me.elmira.stayfocused;

import android.content.Context;
import android.support.annotation.NonNull;

import me.elmira.stayfocused.data.FakeTasksLocalDataSource;
import me.elmira.stayfocused.data.source.TasksRepository;

/**
 * Created by elmira on 1/31/17.
 */

public class Injection {

    public static TasksRepository provideTasksRepository(@NonNull Context context){
        return TasksRepository.getInstance(FakeTasksLocalDataSource.getInstance());
    }
}
