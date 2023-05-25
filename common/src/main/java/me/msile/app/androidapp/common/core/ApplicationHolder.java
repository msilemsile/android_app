package me.msile.app.androidapp.common.core;

import android.app.Application;

import androidx.annotation.NonNull;

import me.msile.app.androidapp.common.provider.FileProviderHelper;

public class ApplicationHolder {

    private static Application appContext;

    public static void init(@NonNull Application application) {
        appContext = application;
        FileProviderHelper.init(application);
    }

    public static Application getAppContext() {
        return appContext;
    }
}