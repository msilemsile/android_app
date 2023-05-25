package me.msile.app.androidapp.common.ui.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Field;

import me.msile.app.androidapp.common.R;

/**
 * 基类dialog
 */
public abstract class BaseDialog extends DialogFragment {

    protected FragmentActivity mActivity;
    protected Resources mResources;

    public BaseDialog() {
        setStyle(STYLE_NORMAL, R.style.AppDialogTheme);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mResources = getResources();
    }

    /**
     * 复写父类show方法
     */
    @Override
    public void showNow(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            realShow(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseDialog", "show error super.showNow(manager, tag)");
        }
    }

    /**
     * 复写父类show方法
     */
    @Override
    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        try {
            return realShow(transaction, tag);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseDialog", "show error super.show(transaction, tag) return int");
        }
        return -1;
    }

    /**
     * 复写父类show方法
     */
    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            realShow(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseDialog", "show error super.show(manager, tag)");
        }
    }

    public void show(Context context) {
        if (context instanceof FragmentActivity) {
            show((FragmentActivity) context);
        }
    }

    public void show(FragmentActivity activity) {
        if (activity == null) {
            return;
        }
        try {
            show(activity.getSupportFragmentManager(), null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseDialog", "show error");
        }
    }

    /**
     * 最终show方法实现  使用commitAllowingStateLoss()
     */
    protected int realShow(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        try {
            Class<?> c = Class.forName("androidx.fragment.app.DialogFragment");
            Field dismissed = c.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
            Field shownByMe = c.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.set(this, true);
            transaction.add(this, tag);
            return transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseDialog", "realShow error");
        }
        return -1;
    }

    /**
     * 最终show方法实现  使用commitAllowingStateLoss()
     */
    protected void realShow(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            Class<?> c = Class.forName("androidx.fragment.app.DialogFragment");
            Field dismissed = c.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
            Field shownByMe = c.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.set(this, true);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(this, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseDialog", "realShow error");
        }
    }

    @Override
    public void dismiss() {
        try {
            dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseDialog", "dismiss error");
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BaseDialog", "dismissAllowingStateLoss error");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialogLifecycleListener != null) {
            mDialogLifecycleListener.onDialogDestroy();
        }
        Log.d("BaseDialog", "BaseDialog 周期 onDestroy -> this = " + this);
    }

    private DialogLifecycleListener mDialogLifecycleListener;

    public void setDialogLifecycleListener(DialogLifecycleListener dialogLifecycleListener) {
        this.mDialogLifecycleListener = dialogLifecycleListener;
    }

    public interface DialogLifecycleListener {
        default void onDialogDestroy() {

        }
    }
}
