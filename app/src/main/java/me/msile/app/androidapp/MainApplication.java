package me.msile.app.androidapp;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import me.msile.app.androidapp.common.core.AppManager;
import me.msile.app.androidapp.common.core.ApplicationHolder;
import me.msile.app.androidapp.common.extend.OpenFileProxyHelper;
import me.msile.app.androidapp.common.mmkv.MMKVInit;
import me.msile.app.androidapp.common.provider.FileProviderHelper;
import me.msile.app.androidapp.common.rx.RxHelper;
import me.msile.app.androidapp.common.utils.ProcessUtils;
import me.msile.app.androidapp.common.web.helper.WebViewCompat;
import me.msile.app.androidapp.common.web.helper.X5WebViewInit;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String currentProcessName = ProcessUtils.getCurrentProcessName();
        boolean isMainProcess = TextUtils.equals(currentProcessName, getPackageName());
        Log.d("MainApplication", "--onCreate-- currentProcess = " + currentProcessName + " | isMainProcess = " + isMainProcess);
        ApplicationHolder.init(this);
        FileProviderHelper.init(this);
        AppManager.INSTANCE.init(this);
        RxHelper.init();
        MMKVInit.init(this);
        //注意隐私!!!
        WebViewCompat.fixWebViewDataDirectory(this, currentProcessName);
        X5WebViewInit.init(this);
        OpenFileProxyHelper.INSTANCE.setMainActivityClass(MainActivity.class);
    }
}
