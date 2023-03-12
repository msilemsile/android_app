package me.msile.app.androidapp.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.WindowManager;

public class ScreenUtils {

    public static boolean isPortraitScreen(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static void setActivityFullScreen(Activity activity, boolean fullScreen) {
        if (fullScreen) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void setScreenOrientation(Activity activity, int orientation) {
        activity.setRequestedOrientation(orientation);
    }

}
