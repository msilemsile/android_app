package me.msile.app.androidapp.common.web;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebStorage;

import me.msile.app.androidapp.common.web.cookie.WebViewCookieManager;
import me.msile.app.androidapp.common.web.cookie.X5WebViewCookieManager;
import me.msile.app.androidapp.common.web.activity.CommonWebActivity;

public enum WebManager {
    INSTANCE;

    //------- 界面管理 ---------

    public Intent getCommonWebIntent(Context context, String webUrl) {
        return getCommonWebIntent(context, webUrl, true);
    }

    public Intent getCommonWebIntent(Context context, String webUrl, boolean isX5WebView) {
        Intent intent = new Intent(context, CommonWebActivity.class);
        intent.putExtra(CommonWebActivity.EXTRA_WEB_URL, webUrl);
        intent.putExtra(CommonWebActivity.EXTRA_WEB_X5, isX5WebView);
        return intent;
    }

    public void clearWebCookies() {
        WebViewCookieManager.clearAllCookies();
        X5WebViewCookieManager.clearAllCookies();
    }

    public void clearLocalStorage() {
        WebStorage.getInstance().deleteAllData();
    }

}