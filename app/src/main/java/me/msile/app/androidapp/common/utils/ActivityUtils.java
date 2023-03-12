package me.msile.app.androidapp.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import me.msile.app.androidapp.common.core.MainThreadHolder;

public class ActivityUtils {

    public static boolean isActivityFinished(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return activity.isFinishing() || activity.isDestroyed();
        }
        return true;
    }

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
        setScreenOrientation(activity, orientation, false);
    }

    public static void setScreenOrientation(Activity activity, int orientation, boolean force) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        activity.setRequestedOrientation(orientation);
        if (force) {
            return;
        }
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        if (orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            MainThreadHolder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Activity activity = activityWeakReference.get();
                    if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                        return;
                    }
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }, 3000);
        }
    }

}
