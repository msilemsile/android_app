package me.msile.app.androidapp.common.ui.adapter.holder.placeholder;

import androidx.annotation.LayoutRes;

/**
 * 占位数据信息
 */
public class PlaceModel {

    private @LayoutRes
    int layoutResId;

    public PlaceModel(@LayoutRes int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }
}
