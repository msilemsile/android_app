package me.msile.app.androidapp.common.ui.widget.loopnotifyview;

import android.graphics.Paint;
import android.text.TextPaint;

public class NotifyTextConfig {

    private TextPaint mTitleTextPaint;
    private Paint mTitleBgPaint;
    private int mTitleBgPaddingLR;
    private int mTitleBgPaddingTB;
    private int mTitleBgCornerSize;
    private int mTitleContentDistance;
    private int mContentTextSize;
    private int mContentTextColor;
    private int mMaxLines;
    private int mLineSpace;

    public NotifyTextConfig() {
    }

    public TextPaint getTitleTextPaint() {
        return mTitleTextPaint;
    }

    public void setTitleTextPaint(TextPaint mTitleTextPaint) {
        this.mTitleTextPaint = mTitleTextPaint;
    }

    public Paint getTitleBgPaint() {
        return mTitleBgPaint;
    }

    public void setTitleBgPaint(Paint mTitleBgPaint) {
        this.mTitleBgPaint = mTitleBgPaint;
    }

    public int getContentTextSize() {
        return mContentTextSize;
    }

    public void setContentTextSize(int mContentTextSize) {
        this.mContentTextSize = mContentTextSize;
    }

    public int getContentTextColor() {
        return mContentTextColor;
    }

    public void setContentTextColor(int mContentTextColor) {
        this.mContentTextColor = mContentTextColor;
    }

    public int getMaxLines() {
        return mMaxLines;
    }

    public void setMaxLines(int mMaxLines) {
        this.mMaxLines = mMaxLines;
    }

    public int getTitleBgPaddingLR() {
        return mTitleBgPaddingLR;
    }

    public void setTitleBgPaddingLR(int mTitleBgPaddingLR) {
        this.mTitleBgPaddingLR = mTitleBgPaddingLR;
    }

    public int getTitleBgCornerSize() {
        return mTitleBgCornerSize;
    }

    public void setTitleBgCornerSize(int mTitleBgCornerSize) {
        this.mTitleBgCornerSize = mTitleBgCornerSize;
    }

    public int getTitleContentDistance() {
        return mTitleContentDistance;
    }

    public void setTitleContentDistance(int mTitleContentDistance) {
        this.mTitleContentDistance = mTitleContentDistance;
    }

    public int getTitleBgPaddingTB() {
        return mTitleBgPaddingTB;
    }

    public void setTitleBgPaddingTB(int mTitleBgPaddingTB) {
        this.mTitleBgPaddingTB = mTitleBgPaddingTB;
    }

    public int getLineSpace() {
        return mLineSpace;
    }

    public void setLineSpace(int mLineSpace) {
        this.mLineSpace = mLineSpace;
    }
}
