package me.msile.app.androidapp.common.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * BaseRecyclerFragment
 */
public abstract class BaseRecyclerFragment extends BaseFragment {

    protected View mRootView;
    protected boolean isFirstOnResume = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutResId(), container, false);
            initViews(mRootView);
            initData(true, savedInstanceState);
            Log.d("BaseFragment", "BaseFragment lifeCycle initViews! this = " + this);
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
    public void onResume() {
        super.onResume();
        if (isFirstOnResume) {
            onFragmentResume(true);
            isFirstOnResume = false;
        } else {
            onFragmentResume(false);
        }
    }

    protected abstract int getLayoutResId();

    protected void initViews(View rootView) {
    }

    protected View findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    /**
     * @param isFirstInit        第一次初始化
     * @param savedInstanceState 异常回收
     */
    protected void initData(boolean isFirstInit, @Nullable Bundle savedInstanceState) {
    }

    /**
     * @param isFirstOnResume 第一次执行onResume 配合viewpager2使用
     */
    protected void onFragmentResume(boolean isFirstOnResume) {
    }
}
