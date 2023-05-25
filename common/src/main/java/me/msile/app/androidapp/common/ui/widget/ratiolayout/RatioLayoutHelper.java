package me.msile.app.androidapp.common.ui.widget.ratiolayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import me.msile.app.androidapp.common.R;

/**
 * 比例布局工具
 */
public class RatioLayoutHelper {

    private float mRatioWH = 1.0f;
    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;

    public RatioLayoutHelper() {
    }

    public void readRatioAttr(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.RatioLayoutHelper);
        mRatioWH = a.getFloat(R.styleable.RatioLayoutHelper_ratio_w_h, 1);
        a.recycle();
    }

    public void measuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatioWH <= 0) {
            mRatioWH = 1;
        }
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == View.MeasureSpec.EXACTLY) {
            int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
            if (widthSize > 0) {
                int heightSize = (int) (widthSize * 1.0f / mRatioWH);
                mHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightSize, View.MeasureSpec.EXACTLY);
            }
        } else if (heightMode == View.MeasureSpec.EXACTLY) {
            int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
            if (heightSize > 0) {
                int widthSize = (int) (heightSize * mRatioWH);
                mWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(widthSize, View.MeasureSpec.EXACTLY);
            }
        }
    }

    public int getWidthMeasureSpec() {
        return mWidthMeasureSpec;
    }

    public int getHeightMeasureSpec() {
        return mHeightMeasureSpec;
    }

    public void setRatioWH(float mRatioWH) {
        this.mRatioWH = mRatioWH;
    }
}