package me.msile.app.androidapp.common.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

import me.msile.app.androidapp.common.core.ActivityHolderHelper;
import me.msile.app.androidapp.common.core.ActivityMethodProxy;
import me.msile.app.androidapp.common.core.ActivityWeakRefHolder;
import me.msile.app.androidapp.common.core.AppLoadingDialogHelper;
import me.msile.app.androidapp.common.utils.ActivityUtils;
import me.msile.app.androidapp.common.utils.StatusBarUtils;

/**
 * app 基类Activity
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private Set<ActivityMethodProxy> methodProxySet = new HashSet<>();
    private ActivityHolderHelper activityHolderHelper = new ActivityHolderHelper();
    private AppLoadingDialogHelper loadingDialogHelper = new AppLoadingDialogHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "BaseActivity 周期 onCreate -> this = " + this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "BaseActivity 周期 onStart -> this = " + this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "BaseActivity 周期 onStop -> this = " + this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "BaseActivity 周期 onResume -> this = " + this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "BaseActivity 周期 onPause -> this = " + this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        methodProxySet.clear();
        activityHolderHelper.clear();
        loadingDialogHelper.clear();
        Log.d(TAG, "BaseActivity 周期 onDestroy -> this = " + this);
    }

    public void showLoadingDialog() {
        runUIThreadWithCheck(new Runnable() {
            @Override
            public void run() {
                loadingDialogHelper.showLoadingDialog(BaseActivity.this, 0, "");
            }
        });
    }

    public void showLoadingDialog(String loadingText) {
        runUIThreadWithCheck(new Runnable() {
            @Override
            public void run() {
                loadingDialogHelper.showLoadingDialog(BaseActivity.this, 0, loadingText);
            }
        });
    }

    public void showLoadingDialog(int tag) {
        runUIThreadWithCheck(new Runnable() {
            @Override
            public void run() {
                loadingDialogHelper.showLoadingDialog(BaseActivity.this, tag, "");
            }
        });
    }

    public void showLoadingDialog(int tag, String loadingText) {
        runUIThreadWithCheck(new Runnable() {
            @Override
            public void run() {
                loadingDialogHelper.showLoadingDialog(BaseActivity.this, tag, loadingText);
            }
        });
    }

    public void hideLoadingDialog() {
        runUIThreadWithCheck(new Runnable() {
            @Override
            public void run() {
                loadingDialogHelper.hideLoadingDialog(0);
            }
        });
    }

    public void hideLoadingDialog(int tag) {
        runUIThreadWithCheck(new Runnable() {
            @Override
            public void run() {
                loadingDialogHelper.hideLoadingDialog(tag);
            }
        });
    }

    public boolean isActivityFinished() {
        return ActivityUtils.isActivityFinished(this);
    }

    public void runUIThreadWithCheck(Runnable task) {
        if (!isActivityFinished()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isActivityFinished()) {
                        task.run();
                    }
                }
            });
        }
    }

    public <T extends ActivityWeakRefHolder> T getWeakRefHolder(@NonNull Class<T> tClass) {
        return activityHolderHelper.get(tClass, this);
    }

    public <T extends ActivityWeakRefHolder> void clearWeakRefHolder(@NonNull Class<T> tClass) {
        activityHolderHelper.clear(tClass);
    }

    public String getActivityClassName() {
        return this.getClass().getCanonicalName();
    }

    public void setSystemBarMode(boolean isLightMode) {
        if (this instanceof ImmerseFullScreenActivity) {
            StatusBarUtils.setSystemBarModeFullScreen(this, isLightMode);
        } else {
            StatusBarUtils.setSystemBarMode(this, isLightMode);
        }
    }

    public void addActivityMethodCallback(ActivityMethodProxy methodCallback) {
        if (methodCallback != null) {
            methodProxySet.add(methodCallback);
        }
    }

    public void removeActivityMethodCallback(ActivityMethodProxy methodCallback) {
        if (methodCallback != null) {
            methodProxySet.remove(methodCallback);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (ActivityMethodProxy methodCallback : methodProxySet) {
            methodCallback.onActivityResult(requestCode, resultCode, data);
        }
    }

    //设置字体为默认大小，不随系统字体大小改而改变
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            //非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            //非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

}