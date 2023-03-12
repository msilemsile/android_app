package me.msile.app.androidapp.common.web.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.tencent.smtt.sdk.WebView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import me.msile.app.androidapp.common.core.ApplicationHolder;
import me.msile.app.androidapp.common.rx.DefaultObserver;
import me.msile.app.androidapp.common.rx.RxTransformerUtils;
import me.msile.app.androidapp.common.storage.StorageHelper;
import me.msile.app.androidapp.common.utils.BitmapUtils;
import top.zibin.luban.Luban;

public class WebViewHelper {

    public static void longScreenShotWebView(View mTitleView, View mWebView, BitmapUtils.ViewCacheBitmapCallback bitmapCallback) {
        longScreenShotWebView(mTitleView, mWebView, "", true, bitmapCallback);
    }

    /**
     * WebView长截图
     */
    public static void longScreenShotWebView(View mTitleView, View mWebView, @Nullable String picFileName, boolean needCompress, BitmapUtils.ViewCacheBitmapCallback bitmapCallback) {
        if (mWebView instanceof com.tencent.smtt.sdk.WebView) {
            com.tencent.smtt.sdk.WebView x5WebView = (WebView) mWebView;
            if (x5WebView.getX5WebViewExtension() == null) {
                getWebViewCacheBitmap(mTitleView, mWebView, picFileName, needCompress, bitmapCallback);
                return;
            }
            int wholeWidth = x5WebView.computeHorizontalScrollRange();
            int wholeHeight = x5WebView.computeVerticalScrollRange();
            int contentWidth = x5WebView.getContentWidth();
            int contentHeight = x5WebView.getContentHeight();
            if (wholeWidth == 0 || wholeHeight == 0 || contentWidth == 0 || contentHeight == 0) {
                if (bitmapCallback != null) {
                    bitmapCallback.onFail();
                }
                return;
            }
            Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                            //标题高度
                            mTitleView.setDrawingCacheEnabled(true);
                            mTitleView.buildDrawingCache();
                            Bitmap titleViewDrawingCache = mTitleView.getDrawingCache();
                            int titleCacheHeight = titleViewDrawingCache.getHeight();
                            int titleCacheWidth = titleViewDrawingCache.getWidth();

                            //截取720分辨率的长截图
                            int maxPixel = 720;
                            int scaleContentWidth = wholeWidth;
                            int scaleContentHeight = wholeHeight;
                            int scaleTitleWidth = titleCacheWidth;
                            int scaleTitleHeight = titleCacheHeight;
                            if (scaleContentWidth > maxPixel) {
                                scaleContentWidth = maxPixel;
                                scaleContentHeight = scaleContentWidth * wholeHeight / wholeWidth;
                                scaleTitleWidth = maxPixel;
                                scaleTitleHeight = scaleTitleWidth * titleCacheHeight / titleCacheWidth;
                            }
                            Bitmap x5bitmap = Bitmap.createBitmap(scaleContentWidth, scaleContentHeight, Bitmap.Config.RGB_565);
                            Canvas x5canvas = new Canvas(x5bitmap);
                            x5canvas.scale((float) scaleContentWidth / (float) contentWidth, (float) scaleContentHeight / (float) contentHeight);
                            x5WebView.getX5WebViewExtension().snapshotWholePage(x5canvas, false, false);

                            Bitmap resultBitmap = Bitmap.createBitmap(scaleContentWidth, scaleContentHeight + scaleTitleHeight, Bitmap.Config.RGB_565);
                            Canvas resultCanvas = new Canvas(resultBitmap);
                            //绘制标题
                            Paint paint = new Paint();
                            paint.setColor(Color.WHITE);
                            paint.setStyle(Paint.Style.FILL);
                            resultCanvas.drawRect(0, 0, scaleTitleWidth, scaleTitleHeight, paint);
                            if (scaleTitleWidth == maxPixel) {
                                Matrix matrix = new Matrix();
                                matrix.setScale((float) scaleTitleWidth / (float) titleCacheWidth, (float) scaleTitleHeight / (float) titleCacheHeight);
                                resultCanvas.drawBitmap(titleViewDrawingCache, matrix, null);
                            } else {
                                resultCanvas.drawBitmap(titleViewDrawingCache, 0, 0, null);
                            }
                            //绘制网页
                            resultCanvas.save();
                            resultCanvas.translate(0, scaleTitleHeight);
                            resultCanvas.drawBitmap(x5bitmap, 0, 0, null);
                            resultCanvas.restore();
                            String drawCachePath = "";
                            String fileName = picFileName;
                            if (TextUtils.isEmpty(fileName)) {
                                fileName = "view_cache_" + System.currentTimeMillis() + ".jpg";
                            }
                            File shareFile = StorageHelper.createShareFile(fileName);
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(shareFile));
                            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                            bos.flush();
                            bos.close();
                            if (needCompress) {
                                List<File> fileList = Luban.with(ApplicationHolder.getAppContext())
                                        .load(shareFile)
                                        .setTargetDir(StorageHelper.getExternalShareDirPath())
                                        .ignoreBy(250)
                                        .get();
                                if (fileList != null && !fileList.isEmpty()) {
                                    drawCachePath = fileList.get(0).getAbsolutePath();
                                } else {
                                    drawCachePath = shareFile.getAbsolutePath();
                                }
                            } else {
                                drawCachePath = shareFile.getAbsolutePath();
                            }
                            mTitleView.setDrawingCacheEnabled(false);
                            mTitleView.destroyDrawingCache();
                            x5bitmap.recycle();
                            resultBitmap.recycle();
                            emitter.onNext(drawCachePath);
                            emitter.onComplete();
                        }
                    }).compose(RxTransformerUtils.mainSchedulers())
                    .subscribe(new DefaultObserver<String>() {
                        @Override
                        protected void onSuccess(@NonNull String s) {
                            if (bitmapCallback != null) {
                                if (!TextUtils.isEmpty(s)) {
                                    bitmapCallback.onComplete(s);
                                } else {
                                    bitmapCallback.onFail();
                                }
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            if (bitmapCallback != null) {
                                getWebViewCacheBitmap(mTitleView, mWebView, picFileName, needCompress, bitmapCallback);
                            }
                        }
                    });
        } else {
            getWebViewCacheBitmap(mTitleView, mWebView, picFileName, needCompress, bitmapCallback);
        }
    }

    private static void getWebViewCacheBitmap(View mTitleView, View mWebView, BitmapUtils.ViewCacheBitmapCallback bitmapCallback) {
        getWebViewCacheBitmap(mTitleView, mWebView, "", true, bitmapCallback);
    }

    private static void getWebViewCacheBitmap(View mTitleView, View mWebView, @Nullable String picFileName, boolean needCompress, BitmapUtils.ViewCacheBitmapCallback bitmapCallback) {
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        mTitleView.setDrawingCacheEnabled(true);
                        mTitleView.buildDrawingCache();

                        mWebView.setDrawingCacheEnabled(true);
                        mWebView.buildDrawingCache();

                        Bitmap titleDrawingCache = mTitleView.getDrawingCache();
                        Bitmap webViewDrawingCache = mWebView.getDrawingCache();

                        int titleCacheHeight = titleDrawingCache.getHeight();
                        int webViewCacheHeight = webViewDrawingCache.getHeight();
                        int webViewCacheWidth = webViewDrawingCache.getWidth();

                        Bitmap resultBitmap = Bitmap.createBitmap(webViewCacheWidth, webViewCacheHeight + titleCacheHeight, Bitmap.Config.RGB_565);
                        Canvas canvas = new Canvas(resultBitmap);
                        //绘制标题
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(0, 0, webViewCacheWidth, titleCacheHeight, paint);
                        canvas.drawBitmap(titleDrawingCache, 0, 0, null);
                        //绘制网页
                        canvas.drawBitmap(webViewDrawingCache, 0, titleCacheHeight, null);

                        mTitleView.setDrawingCacheEnabled(false);
                        mTitleView.destroyDrawingCache();
                        mWebView.setDrawingCacheEnabled(false);
                        mWebView.destroyDrawingCache();

                        String drawCachePath = "";
                        String fileName = picFileName;
                        if (TextUtils.isEmpty(fileName)) {
                            fileName = "view_cache_" + System.currentTimeMillis() + ".jpg";
                        }
                        File shareFile = StorageHelper.createShareFile(fileName);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(shareFile));
                        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                        bos.flush();
                        bos.close();
                        if (needCompress) {
                            List<File> fileList = Luban.with(ApplicationHolder.getAppContext())
                                    .load(shareFile)
                                    .setTargetDir(StorageHelper.getExternalShareDirPath())
                                    .ignoreBy(250)
                                    .get();
                            if (fileList != null && !fileList.isEmpty()) {
                                drawCachePath = fileList.get(0).getAbsolutePath();
                            } else {
                                drawCachePath = shareFile.getAbsolutePath();
                            }
                        } else {
                            drawCachePath = shareFile.getAbsolutePath();
                        }
                        resultBitmap.recycle();
                        emitter.onNext(drawCachePath);
                        emitter.onComplete();
                    }
                }).compose(RxTransformerUtils.mainSchedulers())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    protected void onSuccess(@NonNull String s) {
                        if (bitmapCallback != null) {
                            if (!TextUtils.isEmpty(s)) {
                                bitmapCallback.onComplete(s);
                            } else {
                                bitmapCallback.onFail();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (bitmapCallback != null) {
                            bitmapCallback.onFail();
                        }
                    }
                });
    }

    /**
     * 原生WebView长截图
     * android.webkit.WebView.enableSlowWholeDocumentDraw();
     */
    public static Bitmap getWebViewBitmap(android.webkit.WebView webView) {
        Bitmap bitmap = null;
        try {
            float scale = webView.getScale();
            int width = webView.getWidth();
            int height = (int) (webView.getContentHeight() * scale + 0.5);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            webView.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            Log.d("WebViewHelper", "webView OutOfMemoryError");
        } catch (Exception e) {
            Log.d("WebViewHelper", "webView Exception");
        }
        return bitmap;
    }


    /**
     * x5WebView截长图
     */
    public static Bitmap getX5WebViewBitmap(com.tencent.smtt.sdk.WebView webView) {
        if (webView.getX5WebViewExtension() == null) {
            return null;
        }
        try {
            int wholeWidth = webView.computeHorizontalScrollRange();
            int wholeHeight = webView.computeVerticalScrollRange();
            Bitmap x5bitmap = Bitmap.createBitmap(wholeWidth, wholeHeight, Bitmap.Config.RGB_565);
            Canvas x5canvas = new Canvas(x5bitmap);
            x5canvas.scale((float) wholeWidth / (float) webView.getContentWidth(), (float) wholeHeight / (float) webView.getContentHeight());
            webView.getX5WebViewExtension().snapshotWholePage(x5canvas, false, false);
            return x5bitmap;
        } catch (OutOfMemoryError e) {
            Log.d("WebViewHelper", "x5webView OutOfMemoryError");
        } catch (Exception e) {
            Log.d("WebViewHelper", "x5webView Exception");
        }
        return null;
    }

    /**
     * 判断X5内核是否加载完成
     */
    public static boolean isX5WebCoreHasLoaded(View view) {
        if (view instanceof com.tencent.smtt.sdk.WebView) {
            com.tencent.smtt.sdk.WebView webView = (WebView) view;
            return webView.getX5WebViewExtension() != null;
        }
        return false;
    }

}
