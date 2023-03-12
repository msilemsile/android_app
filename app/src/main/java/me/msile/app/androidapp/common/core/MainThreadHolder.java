package me.msile.app.androidapp.common.core;

import android.os.Handler;
import android.os.Looper;

/**
 * 主线程holder
 */

public class MainThreadHolder {

    private static Handler sHandler;

    /**
     * 主线程执行task
     */
    public static void post(Runnable threadTask) {
        if (isOnUIThread()) {
            threadTask.run();
        } else {
            getMainHandler().post(threadTask);
        }
    }

    /**
     * 主线程延时执行task
     */
    public static void postDelayed(Runnable threadTask, long delayed) {
        getMainHandler().postDelayed(threadTask, delayed);
    }

    /**
     * 主线程移除task
     */
    public static void removeCallbacks(Runnable threadTask) {
        getMainHandler().removeCallbacks(threadTask);
    }

    private static Handler getMainHandler() {
        synchronized (MainThreadHolder.class) {
            if (sHandler == null) {
                sHandler = new Handler(Looper.getMainLooper());
            }
            return sHandler;
        }
    }

    /**
     * 获取主线程
     */
    public static Thread getUIThread() {
        return Looper.getMainLooper().getThread();
    }

    /**
     * 当前是否在主线程
     */
    public static boolean isOnUIThread() {
        return Thread.currentThread() == getUIThread();
    }

}
