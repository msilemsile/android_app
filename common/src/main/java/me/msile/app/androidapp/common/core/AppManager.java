package me.msile.app.androidapp.common.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 * 应用activity管理
 */
public enum AppManager {
    INSTANCE;
    //主界面(弱引用，防止内存泄露)
    private WeakReference<Activity> mMainACReference;
    //当前界面(弱引用，防止内存泄露)
    private WeakReference<Activity> mCurrentACReference;

    //判断应用是否在前台
    private int isAppForeground;
    //应用当前活动个数
    private int appActivityCount;
    private Application.ActivityLifecycleCallbacks appLifecycleCallback;

    public void init(Application application) {
        if (appLifecycleCallback == null) {
            appLifecycleCallback = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    appActivityCount++;
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    isAppForeground++;
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    mCurrentACReference = new WeakReference<>(activity);
                }

                @Override
                public void onActivityPaused(Activity activity) {
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    isAppForeground--;
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    appActivityCount--;
                }
            };
            application.registerActivityLifecycleCallbacks(appLifecycleCallback);
        }
    }

    public Activity getCurrentActivity() {
        if (mCurrentACReference == null) {
            return null;
        }
        return mCurrentACReference.get();
    }

    public void clearCurrentActivity() {
        if (mCurrentACReference != null) {
            mCurrentACReference.clear();
            mCurrentACReference = null;
        }
    }

    public void setMainActivity(Activity homeActivity) {
        mMainACReference = new WeakReference<>(homeActivity);
    }

    public void clearMainActivity() {
        if (mMainACReference != null) {
            mMainACReference.clear();
            mMainACReference = null;
        }
    }

    public Activity getMainActivity() {
        if (mMainACReference == null) {
            return null;
        }
        return mMainACReference.get();
    }

    public boolean isAppForeground() {
        return isAppForeground > 0;
    }

    public int getAppActivityCount() {
        return appActivityCount;
    }
}