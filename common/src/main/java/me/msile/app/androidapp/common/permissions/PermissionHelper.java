package me.msile.app.androidapp.common.permissions;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.util.ArrayList;
import java.util.List;

import me.msile.app.androidapp.common.core.ActivityWeakRefHolder;
import me.msile.app.androidapp.common.core.ApplicationHolder;
import me.msile.app.androidapp.common.permissions.base.BasePermissionRequest;
import me.msile.app.androidapp.common.permissions.callback.PermissionCallback;
import me.msile.app.androidapp.common.ui.dialog.AppAlertDialog;
import me.msile.app.androidapp.common.utils.IntentUtils;

/**
 * 应用权限处理类
 */
public class PermissionHelper extends ActivityWeakRefHolder {

    private BasePermissionRequest mRequest;
    private @Nullable
    PermissionCallback mCallback;

    private PermissionHelper(@NonNull FragmentActivity mActivity) {
        super(mActivity);
    }

    public static PermissionHelper with(FragmentActivity mActivity) {
        return new PermissionHelper(mActivity);
    }

    public void requestPermission(BasePermissionRequest permissionRequest, PermissionCallback permissionCallback) {
        requestPermission(permissionRequest, permissionCallback, true);
    }

    /**
     * 申请权限
     *
     * @param permissionRequest  权限请求
     * @param permissionCallback 权限请求回调
     */
    private void requestPermission(BasePermissionRequest permissionRequest, @Nullable PermissionCallback permissionCallback, boolean showExplainDialog) {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (permissionRequest == null || activityWithCheck == null) {
            return;
        }
        mRequest = permissionRequest;
        mCallback = permissionCallback;
        //判断权限是否全部申请过
        String[] permissions = permissionRequest.getPermissions();
        List<String> needRequestPermissionList = null;
        for (String permission : permissions) {
            if (!PermissionX.isGranted(activityWithCheck, permission)) {
                if (needRequestPermissionList == null) {
                    needRequestPermissionList = new ArrayList<>();
                }
                needRequestPermissionList.add(permission);
            }
        }
        //权限全部通过，直接回调
        if (needRequestPermissionList == null) {
            if (permissionCallback != null) {
                permissionCallback.onGranted();
            }
            return;
        }
        //设置需要申请的权限
        mRequest.setNeedRequestPermission(needRequestPermissionList);
        //是否需要展示请求权限原因弹窗,否则直接去申请权限
        if (showExplainDialog) {
            showRequestExplainDialog();
        } else {
            handlePermissionRequest();
        }
    }

    /**
     * 权限请求弹窗（原因说明）
     */
    private void showRequestExplainDialog() {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (activityWithCheck == null) {
            return;
        }
        AppAlertDialog.build()
                .setTitleText(mRequest.getRequestTitle())
                .setContentText(mRequest.getRequestExplain())
                .setCancelText("取消")
                .setCancelClickListener(new AppAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(AppAlertDialog appAlertDialog) {
                        if (mCallback != null) {
                            mCallback.onCancel();
                        }
                    }
                })
                .setConfirmText("确定")
                .setConfirmClickListener(new AppAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(AppAlertDialog appAlertDialog) {
                        handlePermissionRequest();
                    }
                }).show(activityWithCheck);
    }

    /**
     * 再次权限请求弹窗
     */
    private void showAgainRequestExplainDialog() {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (activityWithCheck == null) {
            return;
        }
        AppAlertDialog.build()
                .setTitleText("提示")
                .setContentText(mRequest.getAgainRequestExplain())
                .setCancelText("取消")
                .setCancelClickListener(new AppAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(AppAlertDialog appAlertDialog) {
                        if (mCallback != null) {
                            mCallback.onCancel();
                        }
                    }
                })
                .setConfirmText("确定")
                .setConfirmClickListener(new AppAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(AppAlertDialog appAlertDialog) {
                        requestPermission(mRequest, mCallback, false);
                    }
                }).show(activityWithCheck);
    }

    /**
     * 拒绝之后弹窗（提示用户）
     */
    private void showRationaleReasonDialog() {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (activityWithCheck == null) {
            return;
        }
        AppAlertDialog.build()
                .setTitleText("提示")
                .setContentText(mRequest.getRationaleReason())
                .setCancelText("取消")
                .setCancelClickListener(new AppAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(AppAlertDialog appAlertDialog) {
                        if (mCallback != null) {
                            mCallback.onCancel();
                        }
                    }
                })
                .setConfirmText("去设置")
                .setConfirmClickListener(new AppAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(AppAlertDialog appAlertDialog) {
                        if (mCallback != null) {
                            mCallback.onGoSetting();
                        }
                        Intent appDetailsSettingsIntent = IntentUtils.getLaunchAppDetailsSettingsIntent(ApplicationHolder.getAppContext().getPackageName(), false);
                        activityWithCheck.startActivity(appDetailsSettingsIntent);
                    }
                }).show(activityWithCheck);
    }


    /**
     * 请求权限处理
     */
    private void handlePermissionRequest() {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (!(activityWithCheck instanceof FragmentActivity)) {
            return;
        }
        FragmentActivity activity = (FragmentActivity) activityWithCheck;
        if (mRequest != null && mRequest.getNeedRequestPermission() != null) {
            PermissionX.init(activity).permissions(mRequest.getNeedRequestPermission())
                    .request(new RequestCallback() {
                        @Override
                        public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                            if (allGranted) {
                                if (mCallback != null) {
                                    mCallback.onGranted();
                                }
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    boolean shouldShowRationale = false;
                                    for (String deniedPermission : deniedList) {
                                        if (activity.shouldShowRequestPermissionRationale(deniedPermission)) {
                                            shouldShowRationale = true;
                                        }
                                    }
                                    if (!shouldShowRationale) {
                                        //拒绝了权限(始终)
                                        showRationaleReasonDialog();
                                    } else {
                                        //拒绝了权限(再请求)
                                        showAgainRequestExplainDialog();
                                    }
                                } else {
                                    //拒绝了权限(始终)
                                    showRationaleReasonDialog();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onClear() {
        mCallback = null;
    }
}
