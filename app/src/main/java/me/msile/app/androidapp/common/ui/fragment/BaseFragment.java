package me.msile.app.androidapp.common.ui.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import me.msile.app.androidapp.common.ui.activity.BaseActivity;


/**
 * Fragment基类
 */
public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    protected BaseActivity mActivity;
    protected Resources mResources;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mResources = getResources();
        Log.d(TAG, "BaseFragment lifeCycle onCreate! this = " + this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "BaseFragment lifeCycle onActivityCreated! this = " + this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "BaseFragment lifeCycle onStart! this = " + this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "BaseFragment lifeCycle onResume! this = " + this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "BaseFragment lifeCycle onPause! this = " + this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "BaseFragment lifeCycle onStop! this = " + this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "BaseFragment lifeCycle onDetach! this = " + this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "BaseFragment lifeCycle onDestroyView! this = " + this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BaseFragment lifeCycle onDestroy! this = " + this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "BaseFragment lifeCycle onHiddenChanged! hidden = " + hidden + ", this = " + this);
    }

    public void showLoadingDialog() {
        mActivity.showLoadingDialog();
    }

    public void showLoadingDialog(String loadingText) {
        mActivity.showLoadingDialog(loadingText);
    }

    public void showLoadingDialog(int tag) {
        mActivity.showLoadingDialog(tag);
    }

    public void showLoadingDialog(int tag, String loadingText) {
        mActivity.showLoadingDialog(tag, loadingText);
    }

    public void hideLoadingDialog() {
        mActivity.hideLoadingDialog();
    }

    public void hideLoadingDialog(int tag) {
        mActivity.hideLoadingDialog(tag);
    }

    public boolean isActivityFinished() {
        return mActivity.isActivityFinished();
    }

    public void runUIThreadWithCheck(Runnable task) {
        mActivity.runUIThreadWithCheck(task);
    }

    public String getFragmentClassName() {
        return this.getClass().getCanonicalName();
    }

    public void setSystemBarMode(boolean isLightMode) {
        mActivity.setSystemBarMode(isLightMode);
    }

}
