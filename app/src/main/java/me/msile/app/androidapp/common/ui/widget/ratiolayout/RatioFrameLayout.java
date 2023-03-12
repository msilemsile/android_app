package me.msile.app.androidapp.common.ui.widget.ratiolayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RatioFrameLayout extends FrameLayout {

    private RatioLayoutHelper layoutHelper;

    public RatioFrameLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public RatioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        layoutHelper = new RatioLayoutHelper();
        layoutHelper.readRatioAttr(context, attributeSet);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        layoutHelper.measuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(layoutHelper.getWidthMeasureSpec(), layoutHelper.getHeightMeasureSpec());
    }

    public void setRatioWH(float mRatioWH) {
        layoutHelper.setRatioWH(mRatioWH);
        requestLayout();
    }
}
