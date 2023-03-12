package me.msile.app.androidapp.common.ui.dialog;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.permissions.PermissionHelper;
import me.msile.app.androidapp.common.permissions.callback.PermissionCallback;
import me.msile.app.androidapp.common.permissions.request.CameraPermissionRequest;
import me.msile.app.androidapp.common.storage.StorageHelper;
import me.msile.app.androidapp.common.ui.toast.AppToast;
import me.msile.app.androidapp.common.provider.FileProviderHelper;

/**
 * 选择文件弹窗（文件、相册、拍照、录像）
 */
public class AppPickDialog extends BaseRecyclerDialog {

    public static final int PICK_TYPE_CANCEL = 1;
    public static final int PICK_TYPE_FILE = 2;
    public static final int PICK_TYPE_GALLERY = 3;
    public static final int PICK_TYPE_IMAGE_CAPTURE = 4;
    public static final int PICK_TYPE_VIDEO_CAPTURE = 5;

    private LinearLayout llPickContent;
    private OnAppPickFileListener pickFileListener;

    private boolean allowPickMultiFile;
    private boolean showGallery;
    private boolean showFilePick;
    private boolean showImageCapture;
    private boolean showVideoCapture;
    private boolean showAllPickOperate;
    private int autoStartPickType;
    private int currentPickType;

    @Override
    protected int getLayoutResId() {
        return me.msile.app.androidapp.R.layout.dialog_app_pick_file;
    }

    @Override
    protected void initViews(View rootView) {
        llPickContent = rootView.findViewById(R.id.ll_pick_content);
        llPickContent.removeAllViews();
    }

    @Override
    protected void initData(boolean isFirstInit, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            allowPickMultiFile = savedInstanceState.getBoolean("allowPickMultiFile");
            showGallery = savedInstanceState.getBoolean("showGallery");
            showFilePick = savedInstanceState.getBoolean("showFilePick");
            showImageCapture = savedInstanceState.getBoolean("showImageCapture");
            showVideoCapture = savedInstanceState.getBoolean("showVideoCapture");
            showAllPickOperate = savedInstanceState.getBoolean("showAllPickOperate");
            autoStartPickType = savedInstanceState.getInt("autoStartPickType");
            currentPickType = savedInstanceState.getInt("currentPickType");
            Log.d("AppPickDialog", "initData restore savedInstanceState");
        }
        if (autoStartPickType != 0) {
            onClickPickFileItem(autoStartPickType);
        } else {
            initPickItems();
            //最后一个item 不展示底部分割线
            int childCount = llPickContent.getChildCount();
            if (childCount > 0) {
                View childAt = llPickContent.getChildAt(childCount - 1);
                View divider = childAt.findViewById(R.id.bottom_divider);
                if (divider != null) {
                    divider.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("allowPickMultiFile", allowPickMultiFile);
        outState.putBoolean("showGallery", showGallery);
        outState.putBoolean("showFilePick", showFilePick);
        outState.putBoolean("showImageCapture", showImageCapture);
        outState.putBoolean("showVideoCapture", showVideoCapture);
        outState.putBoolean("showAllPickOperate", showAllPickOperate);
        outState.putInt("autoStartPickType", autoStartPickType);
        outState.putInt("currentPickType", currentPickType);
        Log.d("AppPickDialog", "onSaveInstanceState");
    }

    private void initPickItems() {
        //拍照
        if (showImageCapture || showAllPickOperate) {
            buildPickFileItem(PICK_TYPE_IMAGE_CAPTURE);
        }
        //录像
        if (showVideoCapture || showAllPickOperate) {
            buildPickFileItem(PICK_TYPE_VIDEO_CAPTURE);
        }
        //相册
        if (showGallery || showAllPickOperate) {
            buildPickFileItem(PICK_TYPE_GALLERY);
        }
        //文件
        if (showFilePick || showAllPickOperate) {
            buildPickFileItem(PICK_TYPE_FILE);
        }
        //取消
        buildPickFileItem(PICK_TYPE_CANCEL);
    }

    public AppPickDialog setShowAllPickOperate(boolean showAllPickOperate) {
        this.showAllPickOperate = showAllPickOperate;
        return this;
    }

    public AppPickDialog setAllowPickMultiFile(boolean allowPickMultiFile) {
        this.allowPickMultiFile = allowPickMultiFile;
        return this;
    }

    public AppPickDialog setShowFilePick(boolean showFilePick) {
        this.showFilePick = showFilePick;
        return this;
    }

    public AppPickDialog setShowGallery(boolean showGallery) {
        this.showGallery = showGallery;
        return this;
    }

    public AppPickDialog setShowImageCapture(boolean showImageCapture) {
        this.showImageCapture = showImageCapture;
        return this;
    }

    public AppPickDialog setShowVideoCapture(boolean showVideoCapture) {
        this.showVideoCapture = showVideoCapture;
        return this;
    }

    public AppPickDialog setAutoStartPickType(int autoStartPickType) {
        this.autoStartPickType = autoStartPickType;
        return this;
    }

    public AppPickDialog setAppPickFileListener(OnAppPickFileListener appPickFileListener) {
        this.pickFileListener = appPickFileListener;
        return this;
    }

    private void buildPickFileItem(int pickType) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_app_pick_file, llPickContent, false);
        TextView tvPick = itemView.findViewById(R.id.tv_pick);
        switch (pickType) {
            case PICK_TYPE_FILE:
                tvPick.setText(allowPickMultiFile ? "文件(可多选)" : "文件");
                break;
            case PICK_TYPE_GALLERY:
                tvPick.setText("相册");
                break;
            case PICK_TYPE_IMAGE_CAPTURE:
                tvPick.setText("拍照");
                break;
            case PICK_TYPE_VIDEO_CAPTURE:
                tvPick.setText("录像");
                break;
            case PICK_TYPE_CANCEL:
                tvPick.setText("取消");
                tvPick.setTextColor(0xff999999);
                break;
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPickFileItem(pickType);
            }
        });
        llPickContent.addView(itemView);
    }

    private void onClickPickFileItem(int pickType) {
        switch (pickType) {
            case PICK_TYPE_FILE:
                startPickFile();
                break;
            case PICK_TYPE_GALLERY:
                startPickFromGallery();
                break;
            case PICK_TYPE_IMAGE_CAPTURE:
                startImageCapture();
                break;
            case PICK_TYPE_VIDEO_CAPTURE:
                startVideoRecord();
                break;
            case PICK_TYPE_CANCEL:
                if (pickFileListener != null) {
                    pickFileListener.onPickCancel();
                }
                dismiss();
                break;
        }
        currentPickType = pickType;
    }

    /**
     * 选择文件
     */
    private void startPickFile() {
        try {
            Intent pickFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
            pickFileIntent.setType("*/*");
            pickFileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowPickMultiFile);
            startActivityForResult(pickFileIntent, PICK_TYPE_FILE);
        } catch (Exception e) {
            AppToast.toastMsg("选择文件失败");
            cancelPickDialog();
            Log.d("AppPickDialog", "startPickFile error");
        }
    }

    /**
     * 相册
     */
    private void startPickFromGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, PICK_TYPE_GALLERY);
        } catch (Exception e) {
            AppToast.toastMsg("打开相册失败");
            cancelPickDialog();
            Log.d("AppPickDialog", "startPickFromGallery error");
        }
    }

    //处理拍照和录像 返回data可能null
    private Uri cachePickFileUri = null;
    private String cachePickFilePath = null;

    /**
     * 拍照
     */
    private void startImageCapture() {
        PermissionHelper.with(mActivity).requestPermission(new CameraPermissionRequest(), new PermissionCallback() {
            @Override
            public void onGranted() {
                try {
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String fileName = "image_capture_" + System.currentTimeMillis() + ".jpg";
                    File cacheImageFile = StorageHelper.createShareFile(fileName);
                    Uri uri = FileProviderHelper.fromFile(cacheImageFile);
                    cachePickFileUri = uri;
                    cachePickFilePath = cacheImageFile.getAbsolutePath();
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(captureIntent, PICK_TYPE_IMAGE_CAPTURE);
                } catch (Exception e) {
                    AppToast.toastMsg("打开相机失败");
                    cancelPickDialog();
                    Log.d("AppPickDialog", "onClickImageCapture error");
                }
            }

            @Override
            public void onCancel() {
                cancelPickDialog();
            }

            @Override
            public void onGoSetting() {
                cancelPickDialog();
            }
        });
    }

    /**
     * 录像
     */
    private void startVideoRecord() {
        PermissionHelper.with(mActivity).requestPermission(new CameraPermissionRequest(), new PermissionCallback() {
            @Override
            public void onGranted() {
                try {
                    Intent captureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    String fileName = "video_record_" + System.currentTimeMillis() + ".mp4";
                    File cacheVideoFile = StorageHelper.createShareFile(fileName);
                    Uri uri = FileProviderHelper.fromFile(cacheVideoFile);
                    cachePickFileUri = uri;
                    cachePickFilePath = cacheVideoFile.getAbsolutePath();
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(captureIntent, PICK_TYPE_VIDEO_CAPTURE);
                } catch (Exception e) {
                    AppToast.toastMsg("打开录像失败");
                    cancelPickDialog();
                    Log.d("AppPickDialog", "onClickImageCapture error");
                }
            }

            @Override
            public void onCancel() {
                cancelPickDialog();
            }

            @Override
            public void onGoSetting() {
                cancelPickDialog();
            }
        });
    }

    /**
     * 处理选择文件回调
     */
    private void handlePickFileResult(Intent intent) {
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
        if (pickFileListener != null) {
            pickFileListener.onPickFile(results);
        }
    }

    /**
     * 处理媒体文件
     */
    private void handlePickMediaFileResult(Intent data) {
        Uri result = null;
        if (data != null) {
            result = data.getData();
        }
        if (result == null) {
            if (cachePickFileUri != null) {
                result = cachePickFileUri;
            }
        }
        if (pickFileListener != null) {
            pickFileListener.onPickMediaFile(result, cachePickFilePath);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_TYPE_FILE:
                    handlePickFileResult(data);
                    break;
                case PICK_TYPE_GALLERY:
                case PICK_TYPE_IMAGE_CAPTURE:
                case PICK_TYPE_VIDEO_CAPTURE:
                    handlePickMediaFileResult(data);
                    break;
            }
        } else {
            if (pickFileListener != null) {
                pickFileListener.onPickCancel();
            }
        }
        dismiss();
    }

    public void cancelPickDialog() {
        if (autoStartPickType != 0) {
            if (pickFileListener != null) {
                pickFileListener.onPickCancel();
            }
            dismiss();
        }
    }

    public static AppPickDialog build() {
        return new AppPickDialog();
    }

    public interface OnAppPickFileListener {
        default void onPickFile(@Nullable Uri[] uri) {
        }

        default void onPickMediaFile(@Nullable Uri uri, String cachePickFilePath) {
        }

        default void onPickCancel() {
        }
    }
}
