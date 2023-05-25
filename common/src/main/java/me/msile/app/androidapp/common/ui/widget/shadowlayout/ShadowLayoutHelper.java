package me.msile.app.androidapp.common.ui.widget.shadowlayout;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.drawable.ShadowDrawable;

public class ShadowLayoutHelper {

    private ShadowDrawable mShadowDrawable;

    public void init(Context context, AttributeSet attributeSet, View view) {
        Resources resources = context.getResources();
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.ShadowLayoutHelper);
        int defShadowColor = resources.getColor(R.color.color_f1f1f1);
        int mShadowStartColor = array.getColor(R.styleable.ShadowLayoutHelper_shadow_start_color, defShadowColor);
        int mShadowEndColor = array.getColor(R.styleable.ShadowLayoutHelper_shadow_end_color, 0);
        int defSoldColor = resources.getColor(R.color.white);
        int mSoldColor = array.getColor(R.styleable.ShadowLayoutHelper_shadow_sold_color, defSoldColor);
        int mCornerRadius = array.getDimensionPixelSize(R.styleable.ShadowLayoutHelper_shadow_corner, 0);
        int defShadowElevation = 5;
        int mShadowElevation = array.getDimensionPixelSize(R.styleable.ShadowLayoutHelper_shadow_elevation, defShadowElevation);
        array.recycle();
        mShadowDrawable = new ShadowDrawable(mCornerRadius, mShadowStartColor, mShadowEndColor, mShadowElevation, mSoldColor);
        if (mShadowElevation > 0) {
            view.setPadding(mShadowElevation, mShadowElevation, mShadowElevation, mShadowElevation);
        }
        view.setWillNotDraw(false);
    }

    public void draw(Canvas canvas, View view) {
        if (canvas == null || view == null) {
            return;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        if (mShadowDrawable != null) {
            mShadowDrawable.setBounds(0, 0, width, height);
            mShadowDrawable.draw(canvas);
        }
    }

}
