package me.msile.app.androidapp.common.ui.widget.statuslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import me.msile.app.androidapp.common.utils.StatusBarUtils;

/**
 * 去除状态栏高度
 */
public class StatusBarFrameLayout extends FrameLayout {

    public StatusBarFrameLayout(Context context) {
        super(context);
        init();
    }

    public StatusBarFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusBarFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int statusBarHeight = StatusBarUtils.getStatusBarHeight();
        setPadding(0, statusBarHeight, 0, 0);
    }
}
