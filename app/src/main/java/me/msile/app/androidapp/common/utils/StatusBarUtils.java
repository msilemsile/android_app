package me.msile.app.androidapp.common.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import me.msile.app.androidapp.common.core.ApplicationHolder;

/**
 * 状态栏工具类(api>=21)
 */
public class StatusBarUtils {

    private static final int INVALID_VAL = -1;

    /**
     * 设置状态栏颜色(调用此方法之前请调用setTransStatusBar()或者setTransStatusBarFullScreen())
     */
    public static void setStatusBarColor(Activity activity, int statusColor) {
        if (statusColor != INVALID_VAL) {
            activity.getWindow().setStatusBarColor(statusColor);
        }
    }

    /**
     * 设置透明状态栏
     */
    public static void setTransStatusBar(Window window) {
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * 设置透明状态栏
     */
    public static void setTransStatusBarFullScreen(Window window) {
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * 设置黑色状态栏图标
     */
    public static void setSystemBarMode(Activity activity, boolean isLightMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            if (isLightMode) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * 设置黑色状态栏图标
     */
    public static void setSystemBarModeFullScreen(Activity activity, boolean isLightMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            if (isLightMode) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    /**
     * 获取状态栏高度(1.resource获取 2.反射获取 3.粗略计算)
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = ApplicationHolder.getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ApplicationHolder.getAppContext().getResources().getDimensionPixelSize(resourceId);
        }
        if (result <= 0) {
            result = (int) (25 * ApplicationHolder.getAppContext().getResources().getDisplayMetrics().density);
        }
        return result;
    }

    /**
     * 设置状态栏与内容自适应
     */
    public static void setFitsSystemWindows(Activity activity) {
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        if (contentView == null) {
            return;
        }
        if (contentView.getChildCount() > 0) {
            View pageView = contentView.getChildAt(0);
            if (pageView != null) {
                pageView.setFitsSystemWindows(true);
            }
        }
    }
}
