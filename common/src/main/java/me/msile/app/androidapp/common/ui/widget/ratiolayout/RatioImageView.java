package me.msile.app.androidapp.common.ui.widget.ratiolayout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class RatioImageView extends AppCompatImageView {

    private RatioLayoutHelper layoutHelper;

    public RatioImageView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public RatioImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatioImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
