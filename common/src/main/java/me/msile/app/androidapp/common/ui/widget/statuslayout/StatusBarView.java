package me.msile.app.androidapp.common.ui.widget.statuslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import me.msile.app.androidapp.common.utils.StatusBarUtils;

/**
 * 与状态栏高度一样的view
 */
public class StatusBarView extends View {

    private int statusBarHeight;

    public StatusBarView(Context context) {
        super(context);
        init();
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        statusBarHeight = StatusBarUtils.getStatusBarHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(statusBarHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }
}
