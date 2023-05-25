package me.msile.app.androidapp.common.web.helper;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;

public class WebViewCompat {
    /**
     * 修复android9 10 WebView多进程问题
     */
    public static void fixWebViewDataDirectory(Application application, String currentProcessName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String appPackageName = application.getPackageName();
            if (!TextUtils.isEmpty(currentProcessName) && !TextUtils.equals(appPackageName, currentProcessName)) {
                com.tencent.smtt.sdk.WebView.setDataDirectorySuffix(currentProcessName);
            }
        }
    }
}
