package me.msile.app.androidapp.common.qrcode;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import java.util.regex.Matcher;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.permissions.PermissionHelper;
import me.msile.app.androidapp.common.permissions.callback.PermissionCallback;
import me.msile.app.androidapp.common.permissions.request.CameraPermissionRequest;
import me.msile.app.androidapp.common.ui.activity.ImmerseActivity;
import me.msile.app.androidapp.common.ui.dialog.AppAlertDialog;
import me.msile.app.androidapp.common.ui.toast.AppToast;
import me.msile.app.androidapp.common.utils.ClipboardUtils;
import me.msile.app.androidapp.common.web.WebManager;

public class QrCodeActivity extends ImmerseActivity implements QRCodeView.Delegate {
    private static final String TAG = QrCodeActivity.class.getSimpleName();

    private ZXingView zxingView;
    private ImageView ivLight;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initViews();
    }

    private void initViews() {
        zxingView = findViewById(R.id.zxingview);
        zxingView.setDelegate(this);
        ivLight = findViewById(R.id.iv_light);
        ivLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ivLight.isSelected();
                if (isChecked) {
                    ivLight.setSelected(false);
                    zxingView.closeFlashlight(); // 关闭闪光灯
                } else {
                    ivLight.setSelected(true);
                    zxingView.openFlashlight(); // 打开闪光灯
                }
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        PermissionHelper.with(this)
                .requestPermission(new CameraPermissionRequest(), new PermissionCallback() {
                    @Override
                    public void onGranted() {
                        zxingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
                        zxingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                    }

                    @Override
                    public void onCancel() {
                        AppToast.toastMsg("取消授权");
                    }

                    @Override
                    public void onGoSetting() {
                        AppToast.toastMsg("去授权");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ivLight.isSelected()) {
            zxingView.closeFlashlight();
        }
    }

    @Override
    protected void onStop() {
        zxingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zxingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(200);
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        if (TextUtils.isEmpty(result)) {
            return;
        }
        vibrate();
        Matcher matcherUrl = Patterns.WEB_URL.matcher(result);
        if (matcherUrl.matches()) {
            Intent commonWebIntent = WebManager.INSTANCE.getCommonWebIntent(this, result);
            startActivity(commonWebIntent);
        } else {
            try {
                Uri uri = Uri.parse(result);
                if (TextUtils.isEmpty(uri.getScheme())) {
                    showScanResultDialog(result);
                } else {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
                    onBackPressed();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showScanResultDialog(result);
            }
        }
    }

    private void showScanResultDialog(String result) {
        AppAlertDialog.build()
                .setTitleText("识别结果")
                .setCancelText("确定")
                .setCancelClickListener(new AppAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(AppAlertDialog appAlertDialog) {
                        appAlertDialog.dismiss();
                        zxingView.startSpot(); // 开始识别
                    }
                })
                .setConfirmText("复制")
                .setConfirmClickListener(new AppAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(AppAlertDialog appAlertDialog) {
                        appAlertDialog.dismiss();
                        ClipboardUtils.copyClipboardInfo(result);
                        AppToast.toastMsg("已复制!");
                        zxingView.startSpot();// 开始识别
                    }
                }).show(this);
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = zxingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zxingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                zxingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        AppToast.toastMsg("打开相机出错!");
    }

    public static void goToPage(Context context) {
        Intent intent = new Intent(context, QrCodeActivity.class);
        context.startActivity(intent);
    }

}