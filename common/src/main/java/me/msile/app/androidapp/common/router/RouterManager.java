package me.msile.app.androidapp.common.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import me.msile.app.androidapp.common.core.AppManager;
import me.msile.app.androidapp.common.core.ApplicationHolder;
import me.msile.app.androidapp.common.utils.ActivityUtils;
import me.msile.app.androidapp.common.utils.IntentUtils;

/**
 * 应用内路由协议
 */
public class RouterManager {

    public static final String SCHEMA_APP = "msileapp";

    RouterManager() {
    }

    /**
     * app外部路由唤醒和跳转（三方app唤醒）
     */
    public static boolean routeWithAwakeApp(Context context, String url) {
        Activity homeActivity = AppManager.INSTANCE.getMainActivity();
        if (!ActivityUtils.isActivityFinished(homeActivity)) {
            return route(homeActivity, url);
        } else {
            return route(context, url);
        }
    }

    /**
     * app内部路由跳转
     */
    public static boolean route(Context context, String url) {

        return true;
    }

    /**
     * 第三方协议跳转
     *
     * @param url 协议地址
     */
    public static void startThirdPartySchemaIntent(String url) {
        try {
            Intent intent = IntentUtils.getThirdPartySchemaIntent(url, true);
            ApplicationHolder.getAppContext().startActivity(intent);
        } catch (Exception e) {
            Log.d("RouterManager", "startThirdPartySchemaIntent error");
        }
    }

    /**
     * 仅仅唤起app 不做路由处理
     */
    public static void awakeApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(SCHEMA_APP + "://"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationHolder.getAppContext().startActivity(intent);
        } catch (Exception e) {
            Log.d("RouterManager", "startThirdPartySchemaIntent error");
        }
    }

    /**
     * 打开页面并检查上下文
     */
    public static void startActivityWithCheck(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
