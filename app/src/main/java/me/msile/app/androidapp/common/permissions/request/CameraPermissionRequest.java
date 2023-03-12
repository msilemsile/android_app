package me.msile.app.androidapp.common.permissions.request;

import android.Manifest;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.core.ApplicationHolder;
import me.msile.app.androidapp.common.permissions.base.BasePermissionRequest;

/**
 * 请求相机权限
 */
public class CameraPermissionRequest extends BasePermissionRequest {

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.CAMERA};
    }

    @Override
    public String getRequestExplain() {
        return "请求允许使用你的相机功能";
    }

    @Override
    public String getRationaleReason() {
        return "你已禁止使用相机功能，如需开启，请到该应用的系统设置页面打开。";
    }

    @Override
    public String getRequestTitle() {
        return ApplicationHolder.getAppContext().getString(R.string.app_name) + "请求使用相机功能";
    }

    @Override
    public String getAgainRequestExplain() {
        return "你已禁止使用相机功能，如需使用请允许。";
    }
}
