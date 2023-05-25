package me.msile.app.androidapp.common.web.callback;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import me.msile.app.androidapp.common.web.model.PermissionRequestType;

public interface WebViewListener {

    default void onReceiveTitle(String title) {
    }

    default void onCreateWebView() {
    }

    default void onRequestPermission(@NonNull PermissionRequestType requestType) {
    }

    default boolean onHandleOverrideUrl(String requestUrl) {
        return false;
    }

    default boolean onShowFileChooser(String fileType, boolean isCaptureEnabled) {
        return false;
    }

    default boolean onLoadUrl(String url, boolean isX5WebView) {
        return false;
    }

    default void onReload(boolean isX5WebView) {

    }

    default void doUpdateVisitedHistory(String url, boolean isReload) {

    }

    default void onPageStarted(String url, Bitmap favicon) {

    }

    default void onPageFinished(String url) {

    }

    default void onReceivedError(boolean isForMainFrame, int errorCode, String description, String failingUrl) {

    }

    default void onHitTestResult(int type, String extra) {

    }

}
