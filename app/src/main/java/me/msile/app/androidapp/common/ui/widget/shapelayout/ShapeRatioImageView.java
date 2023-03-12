package me.msile.app.androidapp.common.ui.widget.shapelayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import me.msile.app.androidapp.common.ui.widget.ratiolayout.RatioImageView;

/**
 * 自定义的可以设置背景
 */
public class ShapeRatioImageView extends RatioImageView {

    private ShapeLayoutHelper layoutHelper;

    public ShapeRatioImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ShapeRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        layoutHelper = new ShapeLayoutHelper();
        layoutHelper.init(context, attributeSet, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        layoutHelper.measuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(layoutHelper.getWidthMeasureSpec(), layoutHelper.getHeightMeasureSpec());
    }

    public void setRadius(int radius) {
        layoutHelper.setRadius(radius);
        postInvalidate();
    }

    public void setRadius(int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        layoutHelper.setRadius(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        postInvalidate();
    }

    public void setStrokeColor(int color) {
        layoutHelper.setStrokeColor(color);
        postInvalidate();
    }

    public void setStrokeWidth(int width) {
        layoutHelper.setStrokeWidth(width);
        postInvalidate();
    }

    public void setSolidColor(int solidColor) {
        layoutHelper.setSolidColor(solidColor);
        postInvalidate();
    }

    public void setDashGap(int dashGap) {
        layoutHelper.setDashGap(dashGap);
        postInvalidate();
    }

    public void setDashWidth(int dashWidth) {
        layoutHelper.setDashWidth(dashWidth);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        layoutHelper.draw(canvas, this);
        super.onDraw(canvas);
    }
}
