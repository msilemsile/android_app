package me.msile.app.androidapp.common.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.msile.app.androidapp.common.ui.activity.BaseActivity;

public class ActivityHolderFactory {

    public static @Nullable
    <T extends ActivityWeakRefHolder> T get(@NonNull Class<T> tClass, @NonNull Context context) {
        if (context instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) context;
            return baseActivity.getWeakRefHolder(tClass);
        }
        return null;
    }

}
