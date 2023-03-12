package me.msile.app.androidapp.common.web.callback;

import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

import me.msile.app.androidapp.common.web.model.PermissionRequestType;

public interface WebViewHolder {

    default void showWebLoading(String tips) {

    }

    default void hideWebLoading() {

    }

    default void loadInitUrl(String url) {
    }

    default void loadInitUrl() {
    }

    default void loadUrl(String url) {
    }

    default void loadUrl(String url, Map<String, String> header) {
    }

    default void reload() {
    }

    default boolean goBack() {
        return false;
    }

    default boolean goForward() {
        return false;
    }

    default boolean canGoBack() {
        return false;
    }

    default boolean canGoForward() {
        return false;
    }

    default boolean canGoBackForward(int step) {
        return false;
    }

    default boolean goBackForward(int step) {
        return false;
    }

    default int getFirstPageStep() {
        return 0;
    }

    default int getLastPageStep() {
        return 0;
    }

    default void stopLoading() {

    }

    default void clearHistory() {

    }

    default void addJavascriptInterface(Object object, String name) {
    }

    default void evaluateJavascript(String jsCode) {
    }

    void permissionCallback(@NonNull PermissionRequestType requestType, boolean isGrant);

    default String getWebViewInfo() {
        return "";
    }

    default String getCurrentPageLink() {
        return "";
    }

    default View getWebView() {
        return null;
    }

    default void setFileChooserResult(@Nullable Uri[] uri) {

    }

    default int getWebHistoryCount() {
        return 0;
    }

    default void setEnableZoomPage(boolean enableZoomPage) {

    }
}