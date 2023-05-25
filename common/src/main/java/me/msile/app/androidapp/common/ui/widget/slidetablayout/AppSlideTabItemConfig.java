package me.msile.app.androidapp.common.ui.widget.slidetablayout;

import android.graphics.drawable.Drawable;

public class AppSlideTabItemConfig {

    public static final int TEXT_GRAVITY_ALIGN = -1;
    public static final int TEXT_GRAVITY_CENTER = 0;
    public static final int TEXT_GRAVITY_LEFT = 1;
    public static final int TEXT_GRAVITY_RIGHT = 2;

    private float mEqualTabCount;
    private boolean mEqualTabs;
    private int mTabsGravity;
    private int mTabLeftMargin;
    private int mTabRightMargin;
    private int mTabTopMargin;
    private int mTabBottomMargin;
    private Drawable mIndicatorDrawable;
    private int mIndicatorColor;
    private int mIndicatorFixWidth;
    private int mIndicatorHeight;
    private int mIndicatorMargin;
    private int mTabTextSize;
    private int mTabTextNormalColor;
    private int mTabTextSelectColor;

    public AppSlideTabItemConfig() {
    }

    public float getEqualTabCount() {
        return mEqualTabCount;
    }

    public void setEqualTabCount(float mEqualTabCount) {
        this.mEqualTabCount = mEqualTabCount;
    }

    public Drawable getIndicatorDrawable() {
        return mIndicatorDrawable;
    }

    public void setIndicatorDrawable(Drawable mIndicatorDrawable) {
        this.mIndicatorDrawable = mIndicatorDrawable;
    }

    public int getIndicatorFixWidth() {
        return mIndicatorFixWidth;
    }

    public void setIndicatorFixWidth(int mIndicatorFixWidth) {
        this.mIndicatorFixWidth = mIndicatorFixWidth;
    }

    public int getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public void setIndicatorHeight(int mIndicatorHeight) {
        this.mIndicatorHeight = mIndicatorHeight;
    }

    public int getIndicatorMargin() {
        return mIndicatorMargin;
    }

    public void setIndicatorMargin(int mIndicatorMargin) {
        this.mIndicatorMargin = mIndicatorMargin;
    }

    public int getTabTextSize() {
        return mTabTextSize;
    }

    public void setTabTextSize(int mTabTextSize) {
        this.mTabTextSize = mTabTextSize;
    }

    public int getTabTextNormalColor() {
        return mTabTextNormalColor;
    }

    public void setTabTextNormalColor(int mTabTextNormalColor) {
        this.mTabTextNormalColor = mTabTextNormalColor;
    }

    public int getTabTextSelectColor() {
        return mTabTextSelectColor;
    }

    public void setTabTextSelectColor(int mTabTextSelectColor) {
        this.mTabTextSelectColor = mTabTextSelectColor;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public void setIndicatorColor(int mIndicatorColor) {
        this.mIndicatorColor = mIndicatorColor;
    }

    public int getTabLeftMargin() {
        return mTabLeftMargin;
    }

    public void setTabLeftMargin(int mTabLeftMargin) {
        this.mTabLeftMargin = mTabLeftMargin;
    }

    public int getTabRightMargin() {
        return mTabRightMargin;
    }

    public void setTabRightMargin(int mTabRightMargin) {
        this.mTabRightMargin = mTabRightMargin;
    }

    public int getTabTopMargin() {
        return mTabTopMargin;
    }

    public void setTabTopMargin(int mTabTopMargin) {
        this.mTabTopMargin = mTabTopMargin;
    }

    public int getTabBottomMargin() {
        return mTabBottomMargin;
    }

    public void setTabBottomMargin(int mTabBottomMargin) {
        this.mTabBottomMargin = mTabBottomMargin;
    }

    public boolean isEqualTabs() {
        return mEqualTabs;
    }

    public void setEqualTabs(boolean mEqualTabs) {
        this.mEqualTabs = mEqualTabs;
    }

    public int getTabsGravity() {
        return mTabsGravity;
    }

    public void setTabsGravity(int mTabsGravity) {
        this.mTabsGravity = mTabsGravity;
    }
}
