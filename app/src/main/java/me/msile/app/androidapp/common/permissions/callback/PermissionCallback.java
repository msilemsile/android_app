package me.msile.app.androidapp.common.permissions.callback;

/**
 * 权限回调
 */
public interface PermissionCallback {
    //授权成功
    default void onGranted() {
    }

    //取消
    default void onCancel() {
    }

    //去系统设置
    default void onGoSetting(){
    }
}
