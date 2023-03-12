package me.msile.app.androidapp.common.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.msile.app.androidapp.common.utils.StatusBarUtils;

/**
 * 基类alert dialog
 */

public abstract class BaseRecyclerDialog extends BaseDialog {
    public BaseRecyclerDialog() {
        super();
    }

    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            View layoutContentView = getLayoutContentView();
            if (layoutContentView != null) {
                mRootView = layoutContentView;
            } else {
                mRootView = inflater.inflate(getLayoutResId(), container, false);
            }
            initViews(mRootView);
            initData(true, savedInstanceState);
        } else {
            initData(false, savedInstanceState);
        }
        ViewParent viewParent = mRootView.getParent();
        if (viewParent instanceof ViewGroup) {
            ((ViewGroup) viewParent).removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getShowsDialog()) {
            Dialog dialog = getDialog();
            if (dialog != null) {
                Window window = dialog.getWindow();
                if (needTransStatusBar()) {
                    //透明状态栏
                    StatusBarUtils.setTransStatusBar(window);
                }
                //全屏布局
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                initWindow(window);
            }
        }
    }

    /**
     * 是否需要沉浸式状态栏
     */
    protected boolean needTransStatusBar() {
        return true;
    }

    protected View findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    /**
     * 初始化内容view 复写getLayoutResId或者getLayoutContentView
     */
    protected abstract int getLayoutResId();

    protected View getLayoutContentView() {
        return null;
    }

    protected void initViews(View rootView) {
    }

    /**
     * @param isFirstInit        第一次初始化
     * @param savedInstanceState 异常回收
     */
    protected void initData(boolean isFirstInit, @Nullable Bundle savedInstanceState) {
    }

    protected void initWindow(Window window) {
    }

}
