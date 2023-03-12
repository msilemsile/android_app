package me.msile.app.androidapp.common.ui.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * 基类popup window
 */
public abstract class CommonPopupWindow extends BasePopupWindow {

    public CommonPopupWindow(Context context) {
        super(context);
    }

    public CommonPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommonPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initPopupWindow(@Nullable Context context) {
        //设置contentView如果子类复写了getLayoutResId或者getContentLayoutView并且Context不为空
        if (context != null) {
            int layoutResId = getLayoutResId();
            if (layoutResId != 0) {
                View rootView = LayoutInflater.from(context).inflate(layoutResId, null);
                setContentView(rootView);
                ViewGroup.LayoutParams layoutParam = getLayoutParams();
                if (layoutParam != null) {
                    setWidth(layoutParam.width);
                    setHeight(layoutParam.height);
                }
                initViews(rootView);
            } else {
                View contentLayoutView = getLayoutContentView(context);
                if (contentLayoutView != null) {
                    setContentView(contentLayoutView);
                    ViewGroup.LayoutParams layoutParam = getLayoutParams();
                    if (layoutParam != null) {
                        setWidth(layoutParam.width);
                        setHeight(layoutParam.height);
                    }
                    initViews(contentLayoutView);
                }
            }
        }
    }

    /**
     * 初始化内容view 复写getLayoutResId或者getLayoutContentView
     */
    protected abstract int getLayoutResId();

    protected View getLayoutContentView(Context context) {
        return null;
    }

    protected abstract ViewGroup.LayoutParams getLayoutParams();

    protected void initViews(View rootView) {

    }

}
