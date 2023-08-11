package me.msile.app.androidapp.test;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.Arrays;
import java.util.Random;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.camera.SimpleCameraActivity;
import me.msile.app.androidapp.common.camera.SimpleCameraXActivity;
import me.msile.app.androidapp.common.permissions.PermissionHelper;
import me.msile.app.androidapp.common.permissions.callback.PermissionCallback;
import me.msile.app.androidapp.common.permissions.request.CameraPermissionRequest;
import me.msile.app.androidapp.common.picker.FilePickerHelper;
import me.msile.app.androidapp.common.picker.SimpleFilePickerDialog;
import me.msile.app.androidapp.common.player.PlayerManager;
import me.msile.app.androidapp.common.qrcode.QrCodeActivity;
import me.msile.app.androidapp.common.ui.adapter.holder.CommonRecyclerViewHolder;
import me.msile.app.androidapp.common.ui.dialog.AppAlertDialog;
import me.msile.app.androidapp.common.ui.toast.AppToast;
import me.msile.app.androidapp.common.ui.widget.shapelayout.ShapeTextView;
import me.msile.app.androidapp.common.web.WebManager;

public class AppComViewHolder extends CommonRecyclerViewHolder<AppComBean> {
    private TextView tvComName;
    private ImageView ivComSpreadOut;
    private TextView tvComDesc;
    private ShapeTextView stvComTest;
    private FrameLayout flComDesc;
    private FrameLayout flComName;

    public AppComViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void initViews(View itemView) {
        tvComName = (TextView) findViewById(R.id.tv_com_name);
        ivComSpreadOut = (ImageView) findViewById(R.id.iv_com_spread_out);
        tvComDesc = (TextView) findViewById(R.id.tv_com_desc);
        stvComTest = (ShapeTextView) findViewById(R.id.stv_com_test);
        flComDesc = (FrameLayout) findViewById(R.id.fl_com_desc);
        flComName = (FrameLayout) findViewById(R.id.fl_com_name);
    }

    @Override
    public void initData(AppComBean data) {
        tvComName.setText(data.getComName() == null ? "" : data.getComName());
        tvComDesc.setText(data.getComDesc() == null ? "" : data.getComDesc());
        if (data.isSpreadOut()) {
            ivComSpreadOut.setSelected(true);
            flComDesc.setVisibility(View.VISIBLE);
        } else {
            ivComSpreadOut.setSelected(false);
            flComDesc.setVisibility(View.GONE);
        }
        View.OnClickListener spreadOutClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setSpreadOut(!data.isSpreadOut());
                notifyItemDataChange();
            }
        };
        flComName.setOnClickListener(spreadOutClick);
        ivComSpreadOut.setOnClickListener(spreadOutClick);
        if (data.isCanTest()) {
            stvComTest.setVisibility(View.VISIBLE);
            stvComTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAppComClick();
                }
            });
        } else {
            stvComTest.setVisibility(View.GONE);
            stvComTest.setOnClickListener(null);
        }
    }

    private void setAppComClick() {
        final String webUrl = "https://www.baidu.com";
        final String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        int comType = mData.getComType();
        switch (comType) {
            case AppComBean.COM_TYPE_NET:
                AppAlertDialog.build()
                        .setTitleText("网络框架")
                        .setContentText("Okhttp + Retrofit")
                        .setConfirmText("确定")
                        .show(mContext);
                break;
            case AppComBean.COM_TYPE_PIC_LOADER:
                AppAlertDialog.build()
                        .setTitleText("图片框架")
                        .setContentText("Glide")
                        .setConfirmText("确定")
                        .show(mContext);
                break;
            case AppComBean.COM_TYPE_LOCAL_DATA:
                AppAlertDialog.build()
                        .setTitleText("存储框架")
                        .setContentText("MMKV + GreenDAO")
                        .setConfirmText("确定")
                        .show(mContext);
                break;
            case AppComBean.COM_TYPE_WEB_VIEW:
                Intent commonWebIntent = WebManager.INSTANCE.getCommonWebIntent(mContext, webUrl);
                mContext.startActivity(commonWebIntent);
                break;
            case AppComBean.COM_TYPE_PICKER:
                SimpleFilePickerDialog.build()
                        .setShowAllPickOperate(true)
                        .setPickFileListener(new FilePickerHelper.OnPickFileListener() {

                            @Override
                            public void onPickFile(@Nullable Uri[] uri, int pickType) {
                                if (uri != null) {
                                    AppAlertDialog.build()
                                            .setTitleText("文件路径")
                                            .setContentText(Arrays.toString(uri))
                                            .setConfirmText("确定")
                                            .show(mContext);
                                }
                            }

                            @Override
                            public void onPickCancel(int pickType) {
                                AppToast.toastMsg("pick cancel");
                            }
                        })
                        .show(mContext);
                break;
            case AppComBean.COM_TYPE_PLAYER:
                boolean changeExoplayer = new Random().nextInt(10) % 2 == 0;
                if (changeExoplayer) {
                    PlayerManager.goToExoPlay(mContext, videoUrl, "exoPlayer");
                } else {
                    PlayerManager.goToMediaPlay(mContext, videoUrl, "mediaPlayer");
                }
                break;
            case AppComBean.COM_TYPE_PERMISSION:
                PermissionHelper.with((FragmentActivity) mContext)
                        .requestPermission(new CameraPermissionRequest(), new PermissionCallback() {
                            @Override
                            public void onGranted() {
                                AppToast.toastMsg("授权成功");
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
                break;
            case AppComBean.COM_TYPE_QR_CODE:
                QrCodeActivity.goToPage(mContext);
                break;
            case AppComBean.COM_TYPE_ROUTER:
                AppAlertDialog.build()
                        .setTitleText("路由框架")
                        .setContentText("简单例子:schema://domain/path?data={json编码后数据}")
                        .setConfirmText("确定")
                        .show(mContext);
                break;
            case AppComBean.COM_TYPE_CAMERA:
                boolean changeCameraHelper = new Random().nextInt(10) % 2 == 0;
                if (changeCameraHelper) {
                    SimpleCameraXActivity.goToPage(mContext);
                } else {
                    SimpleCameraActivity.goToPage(mContext);
                }
                break;
            case AppComBean.COM_TYPE_DOWNLOAD:
                DownloadTestDialog.build().show(mContext);
                break;
        }
    }

    public static class Factory implements CommonRecyclerViewHolder.Factory<AppComBean> {

        @Override
        public CommonRecyclerViewHolder<AppComBean> createViewHolder(View itemView) {
            return new AppComViewHolder(itemView);
        }

        @Override
        public int getLayResId() {
            return R.layout.item_app_compoment;
        }

        @Override
        public Class<AppComBean> getItemDataClass() {
            return AppComBean.class;
        }
    }
}
