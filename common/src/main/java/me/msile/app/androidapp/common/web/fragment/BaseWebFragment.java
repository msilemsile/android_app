package me.msile.app.androidapp.common.web.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Map;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.fragment.BaseRecyclerFragment;
import me.msile.app.androidapp.common.web.model.PermissionRequestType;
import me.msile.app.androidapp.common.web.callback.WebViewHolder;
import me.msile.app.androidapp.common.web.callback.WebViewListener;
import me.msile.app.androidapp.common.web.widget.BaseWebView;

public class BaseWebFragment extends BaseRecyclerFragment implements BaseWebView.BaseWebViewClient, WebViewHolder {

    private static final String TAG = "baseWebFragment";

    private static final int REQUEST_CODE_FILE_CHOOSER = 100;
    public static final String EXTRA_WEB_URL = "extra_web_url";
    public static final String EXTRA_WEB_SHOW_PROGRESS = "extra_web_show_progress";

    private ValueCallback<Uri[]> valueCallback;

    protected String mInitUrl;
    protected String mTitle;
    protected boolean mShowProgress;

    protected BaseWebView mWebView;
    protected ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mInitUrl = bundle.getString(EXTRA_WEB_URL);
            mShowProgress = bundle.getBoolean(EXTRA_WEB_SHOW_PROGRESS, false);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_base_web;
    }

    @Override
    protected void initViews(View rootView) {
        mWebView = rootView.findViewById(R.id.web_view);
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
                if (hitTestResult != null && webViewListener != null) {
                    webViewListener.onHitTestResult(hitTestResult.getType(), hitTestResult.getExtra());
                }
                return true;
            }
        });
        if (webViewListener != null) {
            webViewListener.onCreateWebView();
        }
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(mShowProgress ? View.VISIBLE : View.GONE);
        mWebView.setBaseWebViewClient(this);
        loadUrl(mInitUrl);
    }

    private WebViewListener webViewListener;

    public void setWebViewListener(WebViewListener webViewListener) {
        this.webViewListener = webViewListener;
    }

    private void openFileChooserActivity() {
        try {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(i, "选择文件(可多选)"), REQUEST_CODE_FILE_CHOOSER);
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FILE_CHOOSER) {
                if (valueCallback != null) {
                    handleFileChooserAboveL(data);
                }
            }
        } else {
            if (requestCode == REQUEST_CODE_FILE_CHOOSER) {
                if (valueCallback != null) {
                    valueCallback.onReceiveValue(null);
                    valueCallback = null;
                }
            }
        }
    }

    private void handleFileChooserAboveL(Intent intent) {
        Uri[] results = null;
        if (intent != null) {
            String dataString = intent.getDataString();
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                int clipDataCount = clipData.getItemCount();
                if (clipDataCount > 0) {
                    results = new Uri[clipDataCount];
                    for (int i = 0; i < clipDataCount; i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
            }
            if (dataString != null) {
                results = new Uri[]{intent.getData()};
            }
        }
        valueCallback.onReceiveValue(results);
        valueCallback = null;
    }

    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.setBaseWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            ViewParent parent = mWebView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    public static BaseWebFragment newInstance(String webUrl, boolean showProgress) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_WEB_URL, webUrl);
        bundle.putBoolean(EXTRA_WEB_SHOW_PROGRESS, showProgress);
        BaseWebFragment baseWebFragment = new BaseWebFragment();
        baseWebFragment.setArguments(bundle);
        return baseWebFragment;
    }

    //-------------------WebViewHolder start--------------------------------

    @Override
    public void showWebLoading(String tips) {
        showLoadingDialog(tips);
    }

    @Override
    public void hideWebLoading() {
        hideLoadingDialog();
    }

    @Override
    public void loadInitUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            mInitUrl = url;
            loadUrl(mInitUrl);
        }
    }

    @Override
    public void loadInitUrl() {
        loadUrl(mInitUrl);
    }

    @Override
    public void loadUrl(String url) {
        if (webViewListener != null) {
            boolean b = webViewListener.onLoadUrl(url, false);
            if (b) {
                return;
            }
        }
        mWebView.stopLoading();
        mWebView.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> header) {
        if (webViewListener != null) {
            boolean b = webViewListener.onLoadUrl(url, false);
            if (b) {
                return;
            }
        }
        mWebView.stopLoading();
        mWebView.loadUrl(url, header);
    }

    @Override
    public void reload() {
        if (webViewListener != null) {
            webViewListener.onReload(false);
        }
        mWebView.stopLoading();
        mWebView.reload();
        valueCallback = null;
    }

    @Override
    public boolean goBack() {
        if (mWebView.canWebBack()) {
            return true;
        }
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public boolean goForward() {
        if (mWebView.canGoForward()) {
            mWebView.goForward();
            return true;
        }
        return false;
    }

    @Override
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    @Override
    public boolean canGoForward() {
        return mWebView.canGoForward();
    }

    @Override
    public boolean canGoBackForward(int step) {
        return mWebView.canGoBackOrForward(step);
    }

    @Override
    public boolean goBackForward(int step) {
        if (canGoBackForward(step)) {
            mWebView.goBackOrForward(step);
            return true;
        }
        return false;
    }

    @Override
    public int getFirstPageStep() {
        WebBackForwardList webBackForwardList = mWebView.copyBackForwardList();
        if (webBackForwardList == null) {
            return 0;
        }
        return webBackForwardList.getCurrentIndex();
    }

    @Override
    public int getLastPageStep() {
        WebBackForwardList webBackForwardList = mWebView.copyBackForwardList();
        if (webBackForwardList == null) {
            return 0;
        }
        return webBackForwardList.getSize() - (webBackForwardList.getCurrentIndex() + 1);
    }

    @Override
    public void stopLoading() {
        mWebView.stopLoading();
    }

    @Override
    public void clearHistory() {
        mWebView.clearHistory();
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void addJavascriptInterface(Object object, String name) {
        mWebView.addJavascriptInterface(object, name);
    }

    @Override
    public void evaluateJavascript(String jsCode) {
        mWebView.evaluateJavascript(jsCode, null);
    }

    @Override
    public String getWebViewInfo() {
        return "系统WebView";
    }

    @Override
    public String getCurrentPageLink() {
        return mWebView.getUrl();
    }

    @Override
    public void permissionCallback(@NonNull PermissionRequestType requestType, boolean isGrant) {
        if (mPermissionRequest != null) {
            if (isGrant) {
                mPermissionRequest.grant(mPermissionRequest.getResources());
            }
            mPermissionRequest = null;
        }
    }

    @Override
    public WebView getWebView() {
        return mWebView;
    }

    @Override
    public void setFileChooserResult(@Nullable Uri[] uri) {
        if (valueCallback != null) {
            valueCallback.onReceiveValue(uri);
            valueCallback = null;
        }
    }

    @Override
    public int getWebHistoryCount() {
        WebBackForwardList webBackForwardList = mWebView.copyBackForwardList();
        if (webBackForwardList != null) {
            int size = webBackForwardList.getSize();
            return size;
        }
        return 0;
    }

    @Override
    public void setEnableZoomPage(boolean enableZoomPage) {
        mWebView.setEnableZoom(enableZoomPage);
    }

    //-------------------WebViewHolder end--------------------------------

    //-------------------BaseWebViewClient start--------------------------------

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String requestUrl = request.getUrl().toString();
        return handleOverrideUrl(view, requestUrl);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return handleOverrideUrl(view, url);
    }

    protected boolean handleOverrideUrl(WebView view, String requestUrl) {
        if (!TextUtils.isEmpty(requestUrl)) {
            if (webViewListener != null) {
                return webViewListener.onHandleOverrideUrl(requestUrl);
            }
            Log.d(TAG, "--shouldOverrideUrlLoading--" + requestUrl);
            return false;
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d(TAG, "--onPageFinished--" + url);
        if (webViewListener != null) {
            webViewListener.onPageFinished(url);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d(TAG, "--onPageStarted--" + url);
        if (webViewListener != null) {
            webViewListener.onPageStarted(url, favicon);
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mProgressBar.setProgress(newProgress);
        Log.d(TAG, "--onProgressChanged--" + newProgress + " url: " + view.getUrl());
        if (newProgress == 100) {
            mProgressBar.setVisibility(View.GONE);
            mTitle = view.getTitle();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        mTitle = title;
        if (webViewListener != null) {
            webViewListener.onReceiveTitle(title);
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        boolean hasHandleShowFileChooser = false;
        if (webViewListener != null) {
            //打开图库或者其他文件选择
            String fileType = null;
            boolean isCaptureEnabled = false;
            if (fileChooserParams != null) {
                String[] mimeTypes = fileChooserParams.getAcceptTypes();
                isCaptureEnabled = fileChooserParams.isCaptureEnabled();
                if (mimeTypes != null && mimeTypes.length == 1) {
                    String mime1 = mimeTypes[0];
                    if (!TextUtils.isEmpty(mime1)) {
                        fileType = mime1;
                    }
                }
            }
            hasHandleShowFileChooser = webViewListener.onShowFileChooser(fileType, isCaptureEnabled);
        }
        valueCallback = filePathCallback;
        if (hasHandleShowFileChooser) {
            return true;
        }
        openFileChooserActivity();
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        String errorMsg = "ssl未知错误";
        int primaryError = error.getPrimaryError();
        switch (primaryError) {
            case SslError.SSL_NOTYETVALID:
                errorMsg = "证书没有生效";
                break;
            case SslError.SSL_EXPIRED:
                errorMsg = "证书已过期";
                break;
            case SslError.SSL_IDMISMATCH:
                errorMsg = "主机名称不匹配";
                break;
            case SslError.SSL_UNTRUSTED:
                errorMsg = "证书不受信任";
                break;
            case SslError.SSL_DATE_INVALID:
                errorMsg = "证书日期无效";
                break;
            case SslError.SSL_INVALID:
                errorMsg = "证书无效";
                break;
        }
        handler.proceed();
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        if (webViewListener != null) {
            webViewListener.onRequestPermission(PermissionRequestType.LOCATION);
        }
    }

    private PermissionRequest mPermissionRequest;

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        if (webViewListener != null && request != null) {
            String[] resources = request.getResources();
            if (resources == null) {
                return;
            }
            mPermissionRequest = request;
            for (String webPermission : resources) {
                //录音权限
                if (TextUtils.equals(webPermission, PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                    webViewListener.onRequestPermission(PermissionRequestType.RECORD_AUDIO);
                    continue;
                }
                //相机权限
                if (TextUtils.equals(webPermission, PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                    webViewListener.onRequestPermission(PermissionRequestType.CAMERA);
                }
            }
        }
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        if (webViewListener != null) {
            webViewListener.doUpdateVisitedHistory(url, isReload);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (webViewListener != null) {
            webViewListener.onReceivedError(
                    request.isForMainFrame(),
                    error.getErrorCode(),
                    error.getDescription().toString(),
                    request.getUrl().toString());
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (webViewListener != null) {
            webViewListener.onReceivedError(TextUtils.equals(mWebView.getUrl(), failingUrl), errorCode, description, failingUrl);
        }
    }

    //-------------------BaseWebViewClient end--------------------------------
}
