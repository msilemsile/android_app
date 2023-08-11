package me.msile.app.androidapp.test;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.download.DownloadFileCallback;
import me.msile.app.androidapp.common.download.DownloadFileHelper;
import me.msile.app.androidapp.common.download.DownloadFileInfo;
import me.msile.app.androidapp.common.permissions.PermissionHelper;
import me.msile.app.androidapp.common.permissions.callback.PermissionCallback;
import me.msile.app.androidapp.common.permissions.request.WriteStoragePermissionRequest;
import me.msile.app.androidapp.common.storage.StorageHelper;
import me.msile.app.androidapp.common.ui.dialog.BaseRecyclerDialog;
import me.msile.app.androidapp.common.utils.IntentUtils;

public class DownloadTestDialog extends BaseRecyclerDialog implements DownloadFileCallback {

    private TextView contentText;
    private TextView cancelButton;
    private TextView confirmButton;

    public DownloadTestDialog() {
        setCancelable(false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_download_test;
    }

    @Override
    protected void initViews(View rootView) {
        contentText = (TextView) findViewById(R.id.content_text);
        cancelButton = (TextView) findViewById(R.id.cancel_button);
        confirmButton = (TextView) findViewById(R.id.confirm_button);
    }

    @Override
    protected void initData(boolean isFirstInit, @Nullable Bundle savedInstanceState) {
        final String downloadUrl = "https://gitee.com/msilemsile/web_sqlite_test/releases/download/1.0/android.apk";
        contentText.setText("下载地址为:" + downloadUrl);
        cancelButton.setText("取消");
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFileHelper.INSTANCE.removeDownloadFileCallback(DownloadTestDialog.this);
                dismiss();
            }
        });
        confirmButton.setText("下载");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.with(mActivity)
                        .requestPermission(new WriteStoragePermissionRequest(), new PermissionCallback() {
                            @Override
                            public void onGranted() {
                                DownloadFileHelper.INSTANCE.addDownloadFileCallback(DownloadTestDialog.this);
                                DownloadFileHelper.INSTANCE.downloadFile(new DownloadFileInfo(downloadUrl));
                            }
                        });
            }
        });
    }

    public static DownloadTestDialog build() {
        return new DownloadTestDialog();
    }

    @Override
    public void onDownloadStart(DownloadFileInfo downloadFileInfo) {
        contentText.setText(downloadFileInfo.getFileName() + "开始下载");
    }

    @Override
    public void onDownloadProgress(DownloadFileInfo downloadFileInfo, int progress) {
        confirmButton.setText("下载中");
        contentText.setText(downloadFileInfo.getFileName() + "下载中: " + progress);
    }

    @Override
    public void onDownloadFail(DownloadFileInfo downloadFileInfo, String errorMsg) {
        contentText.setText(downloadFileInfo.getFileName() + "下载失败: " + errorMsg);
    }

    @Override
    public void onDownloadResult(DownloadFileInfo downloadFileInfo, String downloadResultMsg) {
        contentText.setText(downloadFileInfo.getFileName() + downloadResultMsg + "\n下载目录为:" + StorageHelper.getRelativeDownloadsDirPath());
        confirmButton.setText("前往");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(IntentUtils.getToDownloadDirIntent());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
