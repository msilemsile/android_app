package me.msile.app.androidapp.common.ui.activity;

import android.os.Build;
import android.os.Bundle;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.utils.StatusBarUtils;

/**
 * 默认沉浸式状态栏 Activity
 */
public class ImmerseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setTransStatusBar(getWindow());
        //设置状态栏颜色 (6.0以上设置成白色并且状态栏是暗色字体 6.0以下设置成灰色)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_above_m));
            StatusBarUtils.setSystemBarMode(this, true);
        } else {
            //5.0 5.1设置灰色状态栏
            StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.status_bar_below_m));
        }
    }
}