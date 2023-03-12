package me.msile.app.androidapp.common.ui.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

/**
 * 基类popup window
 */
public class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(Context context) {
        super(context);
        init(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public BasePopupWindow() {
        init(null);
    }

    public BasePopupWindow(View contentView) {
        super(contentView);
        init(null);
    }

    public BasePopupWindow(int width, int height) {
        super(width, height);
        init(null);
    }

    public BasePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        init(null);
    }

    public BasePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        init(null);
    }

    private void init(Context context) {
        // 设置背景
        setBackgroundDrawable(new ColorDrawable());
        // 外部点击事件
        setOutsideTouchable(true);
        // 设置焦点
        setFocusable(true);
        //设置宽高（5.0不展示问题)）
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //Context 可能为空 如果有contentView 则取View的Context
        if (context == null) {
            View contentView = getContentView();
            if (contentView != null) {
                context = contentView.getContext();
            }
        }
        //初始化popup window
        initPopupWindow(context);
    }

    protected void initPopupWindow(@Nullable Context context) {
    }
}
