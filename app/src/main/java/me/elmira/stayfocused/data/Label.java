package me.elmira.stayfocused.data;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by elmira on 1/20/17.
 */

public class Label {
    @NonNull
    private final String mId;

    @NonNull
    private final String mName;

    public Label(@NonNull String mName) {
        mId = UUID.randomUUID().toString();
        this.mName = mName;
    }

    public Label(@NonNull String id, @NonNull String name) {
        this.mId = id;
        this.mName = name;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }
}
