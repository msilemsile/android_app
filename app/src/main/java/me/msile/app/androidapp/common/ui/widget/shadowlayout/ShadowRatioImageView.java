package me.msile.app.androidapp.common.ui.widget.shadowlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.msile.app.androidapp.common.ui.widget.ratiolayout.RatioImageView;

/**
 * 带阴影frameLayout
 */
public class ShadowRatioImageView extends RatioImageView {

    private ShadowLayoutHelper layoutHelper;

    public ShadowRatioImageView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ShadowRatioImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShadowRatioImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        layoutHelper = new ShadowLayoutHelper();
        layoutHelper.init(context, attributeSet, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        layoutHelper.draw(canvas, this);
        super.onDraw(canvas);
    }
}
