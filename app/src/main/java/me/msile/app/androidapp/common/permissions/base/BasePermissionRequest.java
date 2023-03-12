package me.msile.app.androidapp.common.permissions.base;

import java.util.List;

public abstract class BasePermissionRequest {

    public BasePermissionRequest() {
    }

    private List<String> needRequestPermission;

    public List<String> getNeedRequestPermission() {
        return needRequestPermission;
    }

    public void setNeedRequestPermission(List<String> needRequestPermission) {
        this.needRequestPermission = needRequestPermission;
    }

    /**
     * 需要请求的权限
     */
    public abstract String[] getPermissions();

    /**
     * 请求标题
     */
    public abstract String getRequestTitle();

    /**
     * 请求权限原因
     */
    public abstract String getRequestExplain();

    /**
     * 再次请求权限
     */
    public abstract String getAgainRequestExplain();

    /**
     * 权限被禁止
     */
    public abstract String getRationaleReason();

    /**
     * 当前权限请求唯一标识
     */
    public String getPermissionRequestTag() {
        return this.getClass().getCanonicalName();
    }

}
