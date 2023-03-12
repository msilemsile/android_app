package me.msile.app.androidapp.common.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.msile.app.androidapp.common.constants.AppCommonConstants;
import me.msile.app.androidapp.common.core.ApplicationHolder;
import me.msile.app.androidapp.common.provider.FileProviderHelper;
import me.msile.app.androidapp.common.rx.DefaultObserver;
import me.msile.app.androidapp.common.ui.toast.AppToast;
import me.msile.app.androidapp.common.utils.FileUtils;

/**
 * 多媒体操作工具类
 */
public class MediaInsertHelper {

    public static void insertPicToGallery(String picPath) {
        insertPicToGallery(picPath, false);
    }

    public static void insertPicToGallery(String picPath, boolean hasAlpha) {
        insertPicToGallery(picPath, hasAlpha, null);
    }

    public static void insertPicToGallery(String picPath, boolean hasAlpha, InsertMediaCallback mediaCallback) {
        if (TextUtils.isEmpty(picPath)) {
            if (mediaCallback != null) {
                mediaCallback.onFail();
            }
            return;
        }
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        String fileName = AppCommonConstants.APP_PREFIX_TAG + "_pic_" + System.currentTimeMillis() + (hasAlpha ? ".png" : ".jpg");
                        String mimeType = "image/*";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            ContentResolver resolver = ApplicationHolder.getAppContext().getContentResolver();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                            contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, StorageHelper.getRelativeDCIMDirPath());
                            //插入数据库
                            Uri contentUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                            //保存文件
                            OutputStream outputStream = resolver.openOutputStream(contentUri);
                            FileUtils.copyFileToOtherFile(new File(picPath), (FileOutputStream) outputStream);
                            emitter.onNext("");
                        } else {
                            File inFile = new File(picPath);
                            File outFile = new File(StorageHelper.getPublicDCIMDirPath(), fileName);
                            //保存文件
                            FileUtils.copyFileToOtherFile(inFile, outFile);
                            ContentValues values = new ContentValues();
                            String outFilePath = outFile.getAbsolutePath();
                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                            values.put(MediaStore.Images.Media.DATA, outFilePath);
                            //插入数据库
                            ApplicationHolder.getAppContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            emitter.onNext(outFilePath);
                        }
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            sendUpdateGalleryBroadcast(s);
                        }
                        if (mediaCallback != null) {
                            mediaCallback.onSuccess();
                        } else {
                            AppToast.toastMsg("已保存到相册");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (mediaCallback != null) {
                            mediaCallback.onFail();
                        } else {
                            AppToast.toastMsg("保存失败");
                        }
                    }
                });
    }

    public static void insertPicToGallery(Bitmap bitmap) {
        insertPicToGallery(bitmap, null);
    }

    public static void insertPicToGallery(Bitmap bitmap, InsertMediaCallback mediaCallback) {
        if (bitmap == null || bitmap.isRecycled()) {
            if (mediaCallback != null) {
                mediaCallback.onFail();
            }
            return;
        }
        WeakReference<Bitmap> bitmapWeakReference = new WeakReference<>(bitmap);
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        Bitmap bitmapRefer = bitmapWeakReference.get();
                        if (bitmapRefer == null || bitmapRefer.isRecycled()) {
                            emitter.onError(new Throwable());
                            return;
                        }
                        boolean hasAlpha = bitmapRefer.hasAlpha();
                        String fileName = AppCommonConstants.APP_PREFIX_TAG + "_pic_" + System.currentTimeMillis() + (hasAlpha ? ".png" : ".jpg");
                        String mimeType = "image/*";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            ContentResolver resolver = ApplicationHolder.getAppContext().getContentResolver();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                            contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, StorageHelper.getRelativeDCIMDirPath());
                            //插入数据库
                            Uri contentUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                            OutputStream outputStream = resolver.openOutputStream(contentUri);
                            //保存文件
                            bitmapRefer.compress(hasAlpha ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, outputStream);
                            emitter.onNext("");
                        } else {
                            File outFile = new File(StorageHelper.getPublicDCIMDirPath(), fileName);
                            //保存文件
                            bitmapRefer.compress(hasAlpha ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(outFile));
                            ContentValues values = new ContentValues();
                            String outFilePath = outFile.getAbsolutePath();
                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                            values.put(MediaStore.Images.Media.DATA, outFilePath);
                            //插入数据库
                            ApplicationHolder.getAppContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            emitter.onNext(outFilePath);
                        }
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            sendUpdateGalleryBroadcast(s);
                        }
                        if (mediaCallback != null) {
                            mediaCallback.onSuccess();
                        } else {
                            AppToast.toastMsg("已保存到相册");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (mediaCallback != null) {
                            mediaCallback.onFail();
                        } else {
                            AppToast.toastMsg("保存失败");
                        }
                    }
                });
    }

    public static void insertVideoToMedia(String videoPath) {
        insertVideoToMedia(videoPath, null);
    }

    public static void insertVideoToMedia(String videoPath, InsertMediaCallback mediaCallback) {
        if (TextUtils.isEmpty(videoPath)) {
            if (mediaCallback != null) {
                mediaCallback.onFail();
            }
            return;
        }
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        String fileName = AppCommonConstants.APP_PREFIX_TAG + "_video_" + System.currentTimeMillis() + ".mp4";
                        String mimeType = "video/*";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            ContentResolver resolver = ApplicationHolder.getAppContext().getContentResolver();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                            contentValues.put(MediaStore.Video.Media.MIME_TYPE, mimeType);
                            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, StorageHelper.getRelativeDCIMDirPath());
                            //插入数据库
                            Uri contentUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                            OutputStream outputStream = resolver.openOutputStream(contentUri);
                            //保存文件
                            FileUtils.copyFileToOtherFile(new File(videoPath), (FileOutputStream) outputStream);
                            emitter.onNext("");
                        } else {
                            File inFile = new File(videoPath);
                            File outFile = new File(StorageHelper.getPublicDCIMDirPath(), fileName);
                            //保存文件
                            FileUtils.copyFileToOtherFile(inFile, outFile);
                            ContentValues values = new ContentValues();
                            String outFilePath = outFile.getAbsolutePath();
                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                            values.put(MediaStore.Video.Media.MIME_TYPE, mimeType);
                            values.put(MediaStore.Video.Media.DATA, outFilePath);
                            //插入数据库
                            ApplicationHolder.getAppContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                            emitter.onNext(outFilePath);
                        }
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            sendUpdateGalleryBroadcast(s);
                        }
                        if (mediaCallback != null) {
                            mediaCallback.onSuccess();
                        } else {
                            AppToast.toastMsg("已保存到相册");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (mediaCallback != null) {
                            mediaCallback.onFail();
                        } else {
                            AppToast.toastMsg("保存失败");
                        }
                    }
                });
    }

    /**
     * 更新相册广播
     */
    private static void sendUpdateGalleryBroadcast(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        mediaScanIntent.setData(FileProviderHelper.fromFile(f));
        FileProviderHelper.addFileWritePermission(mediaScanIntent);
        ApplicationHolder.getAppContext().sendBroadcast(mediaScanIntent);
    }

    public interface InsertMediaCallback {
        void onSuccess();

        void onFail();
    }

}
