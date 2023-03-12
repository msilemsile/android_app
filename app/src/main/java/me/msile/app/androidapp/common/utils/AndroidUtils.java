package me.msile.app.androidapp.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;

import androidx.core.app.ActivityCompat;

import java.io.File;

import me.msile.app.androidapp.common.core.ApplicationHolder;
import me.msile.app.androidapp.common.provider.FileProviderHelper;

public class AndroidUtils {

    /**
     * 获取当前版本
     */
    public static String getAppVersionName() {
        PackageManager packageManager = ApplicationHolder.getAppContext().getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(ApplicationHolder.getAppContext().getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("AndroidUtils", "getAppVersionName error");
        }
        return "";
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @return 当前应用的版本名称
     */
    public static int getVersionCode() {
        try {
            PackageManager packageManager = ApplicationHolder.getAppContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(ApplicationHolder.getAppContext().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用包名
     */
    public static String getAppPackageName() {
        return ApplicationHolder.getAppContext().getPackageName();
    }

    /**
     * 获取MetaData
     */
    public static String getMetaDataValue(String metaKey) {
        Bundle metaData = null;
        String apiKey = "";
        if (TextUtils.isEmpty(metaKey)) {
            return "";
        }
        try {
            Context appContext = ApplicationHolder.getAppContext();
            ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (Exception e) {
            Log.d("AndroidUtils", "getMetaDataValue error");
        }
        return apiKey;
    }

    /**
     * 重启App
     */
    public static void restartApp() {
        Application context = ApplicationHolder.getAppContext();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launchIntent);
    }

    /**
     * 杀掉App界面和进程
     */
    public static void killApp(Activity activity) {
        ActivityCompat.finishAffinity(activity);
        Process.killProcess(Process.myPid());
    }

    /**
     * 安装.apk文件
     */
    public static void installApk(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkFileUri = FileProviderHelper.fromFile(file);
            intent.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
            FileProviderHelper.addFileReadPermission(intent);
            ApplicationHolder.getAppContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取webView userAgent
     */
    public static String getWebUserAgent() {
        return WebSettings.getDefaultUserAgent(ApplicationHolder.getAppContext());
    }
}
