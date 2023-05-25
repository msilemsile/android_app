package me.msile.app.androidapp.common.web.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

/**
 * 应用通用WebView
 */

public class BaseWebView extends WebView {

    private BaseWebViewClient baseWebViewClient;
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private String userAgent;

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void init() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDatabaseEnabled(true);
//        settings.setAppCacheEnabled(true);
        settings.setTextZoom(100);
        settings.setLoadsImagesAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setGeolocationEnabled(true);

        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        setWebViewClient(new InnerWebViewClient());
        setWebChromeClient(new InnerChromeClient());
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (baseWebViewClient != null) {
                    boolean startDownload = baseWebViewClient.onStartDownload(url, userAgent, contentDisposition, mimetype, contentLength);
                    if (!startDownload) {
                        try {
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            getContext().startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        userAgent = settings.getUserAgentString();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setEnableZoom(boolean enableZoom) {
        WebSettings webSettings = getSettings();
        //设置可以支持缩放
        webSettings.setSupportZoom(enableZoom);
        //设置出现缩放工具
        webSettings.setBuiltInZoomControls(enableZoom);
        //设定缩放控件隐藏
        webSettings.setDisplayZoomControls(false);
    }

    public void setBaseWebViewClient(BaseWebViewClient BaseWebViewClient) {
        this.baseWebViewClient = BaseWebViewClient;
    }

    public interface BaseWebViewClient {
        default boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        default boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        default void onPageFinished(WebView view, String url) {
        }

        default void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        default void onProgressChanged(WebView view, int newProgress) {
        }

        default void onReceivedTitle(WebView view, String title) {
        }

        default boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            return false;
        }

        default boolean shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return false;
        }

        default boolean onStartDownload(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            return false;
        }

        default void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        }

        default void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {

        }

        default void onPermissionRequest(PermissionRequest request) {

        }

        default void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {

        }

        default void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

        }

        default void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }
    }

    private class InnerWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return baseWebViewClient != null && baseWebViewClient.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return baseWebViewClient != null && baseWebViewClient.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            WebResourceResponse response = super.shouldInterceptRequest(view, request);
            boolean interceptRequest = baseWebViewClient != null && baseWebViewClient.shouldInterceptRequest(view, request);
            WebResourceResponse emptyResponse = new WebResourceResponse("text/html", "UTF-8", null);
            return interceptRequest ? emptyResponse : response;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (baseWebViewClient != null) {
                baseWebViewClient.onPageFinished(view, url);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (baseWebViewClient != null) {
                baseWebViewClient.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (baseWebViewClient != null) {
                baseWebViewClient.onReceivedSslError(view, handler, error);
            } else {
                super.onReceivedSslError(view, handler, error);
            }
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            if (baseWebViewClient != null) {
                baseWebViewClient.doUpdateVisitedHistory(view, url, isReload);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (baseWebViewClient != null) {
                baseWebViewClient.onReceivedError(view, request, error);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (baseWebViewClient != null) {
                baseWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
            }
        }

    }

    private class InnerChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(final WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (baseWebViewClient != null) {
                baseWebViewClient.onProgressChanged(view, newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (baseWebViewClient != null) {
                baseWebViewClient.onReceivedTitle(view, title);
            }
        }

        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            return frameLayout;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            showCustomView(view, callback);
            Activity attachActivity = getAttachActivity();
            if (attachActivity == null) {
                return;
            }
            attachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
            Activity attachActivity = getAttachActivity();
            if (attachActivity == null) {
                return;
            }
            attachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (baseWebViewClient != null) {
                return baseWebViewClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            if (baseWebViewClient != null) {
                baseWebViewClient.onGeolocationPermissionsShowPrompt(origin, callback);
            } else {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        }

        @Override
        public void onPermissionRequest(PermissionRequest request) {
            if (baseWebViewClient != null) {
                baseWebViewClient.onPermissionRequest(request);
            } else {
                super.onPermissionRequest(request);
            }
        }
    }

    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        Context context = getContext();
        if (!(context instanceof Activity)) {
            return;
        }
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        Activity webActivity = (Activity) context;
        FrameLayout decor = (FrameLayout) webActivity.getWindow().getDecorView();
        fullscreenContainer = new FullScreenHolder(webActivity);
        fullscreenContainer.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        decor.addView(fullscreenContainer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        customView = view;
        customViewCallback = callback;
    }

    /**
     * 全屏容器界面
     */
    static class FullScreenHolder extends FrameLayout {

        public FullScreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean isFullScreen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        Activity attachActivity = getAttachActivity();
        if (attachActivity == null) {
            return;
        }
        if (isFullScreen) {
            attachActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            attachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        Activity attachActivity = getAttachActivity();
        if (attachActivity == null) {
            return;
        }
        FrameLayout decor = (FrameLayout) attachActivity.getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        if (customViewCallback != null) {
            customViewCallback.onCustomViewHidden();
        }
        customViewCallback = null;
        setVisibility(View.VISIBLE);
    }

    public boolean canWebBack() {
        Activity attachActivity = getAttachActivity();
        if (attachActivity == null) {
            return false;
        }
        boolean portraitScreen = attachActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (!portraitScreen) {
            hideCustomView();
            attachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        return false;
    }

    private Activity getAttachActivity() {
        Context context = getContext();
        if (!(context instanceof Activity)) {
            return null;
        }
        return (Activity) context;
    }

    /**
     * 修复WebView可能创建错误
     * android.webkit.WebViewFactory$MissingWebViewPackageException  Failed to load WebView provider: No WebView installed at
     */
    @Override
    public void setOverScrollMode(int mode) {
        try {
            super.setOverScrollMode(mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
