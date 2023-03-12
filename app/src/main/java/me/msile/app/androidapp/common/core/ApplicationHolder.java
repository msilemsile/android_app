package me.msile.app.androidapp.common.core;

import android.app.Application;

import androidx.annotation.NonNull;

public class ApplicationHolder {

    private static Application appContext;

    public static void init(@NonNull Application application) {
        appContext = application;
    }

    public static Application getAppContext() {
        return appContext;
    }
}