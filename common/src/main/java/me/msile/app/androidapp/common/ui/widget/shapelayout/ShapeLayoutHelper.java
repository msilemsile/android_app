package me.msile.app.androidapp.common.ui.widget.shapelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import me.msile.app.androidapp.common.R;

public class ShapeLayoutHelper {

    private GradientDrawable gradientDrawable = new GradientDrawable();
    private int circleRadius;
    private int radius;
    private int bottomLeftRadius;
    private int bottomRightRadius;
    private int topLeftRadius;
    private int topRightRadius;
    private int solidColor;
    private int strokeColor;
    private int strokeWidth;
    private int dashWidth;
    private int dashGap;

    public void init(Context context, AttributeSet attributeSet, View view) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeLayoutHelper);
        circleRadius = a.getDimensionPixelSize(R.styleable.ShapeLayoutHelper_circleRadiusT, 0);
        radius = a.getDimensionPixelSize(R.styleable.ShapeLayoutHelper_radiusT, 0);
        bottomLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeLayoutHelper_bottomLeftRadiusT, 0);
        bottomRightRadius = a.getDimensionPixelSize(R.styleable.ShapeLayoutHelper_bottomRightRadiusT, 0);
        topLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeLayoutHelper_topLeftRadiusT, 0);
        topRightRadius = a.getDimensionPixelSize(R.styleable.ShapeLayoutHelper_topRightRadiusT, 0);
        solidColor = a.getColor(R.styleable.ShapeLayoutHelper_solidColorT, Color.TRANSPARENT);
        strokeColor = a.getColor(R.styleable.ShapeLayoutHelper_strokeColorT, Color.TRANSPARENT);
        strokeWidth = a.getDimensionPixelSize(R.styleable.ShapeLayoutHelper_strokeWidthT, 0);
        dashWidth = a.getDimensionPixelSize(R.styleable.ShapeLayoutHelper_dashWidthT, 0);
        dashGap = a.getDimensionPixelSize(R.styleable.ShapeLayoutHelper_dashGapT, 0);
        a.recycle();
        view.setWillNotDraw(false);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setRadius(int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        this.topLeftRadius = topLeftRadius;
        this.topRightRadius = topRightRadius;
        this.bottomLeftRadius = bottomLeftRadius;
        this.bottomRightRadius = bottomRightRadius;
    }

    public void setStrokeColor(int color) {
        this.strokeColor = color;
    }

    public void setStrokeWidth(int width) {
        this.strokeWidth = width;
    }

    public void setSolidColor(int solidColor) {
        this.solidColor = solidColor;
    }

    public void setDashGap(int dashGap) {
        this.dashGap = dashGap;
    }

    public void setDashWidth(int dashWidth) {
        this.dashWidth = dashWidth;
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
        if (circleRadius > 0) {
            gradientDrawable.setShape(GradientDrawable.OVAL);
            int circleSize = circleRadius * 2;
            gradientDrawable.setSize(circleSize, circleSize);
            gradientDrawable.setBounds(0, 0, circleSize, circleSize);
        } else {
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            if (radius != 0) {
                gradientDrawable.setCornerRadius(radius);
            } else {
                gradientDrawable.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
            }
            gradientDrawable.setBounds(0, 0, view.getWidth(), view.getHeight());
        }
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap);
        gradientDrawable.draw(canvas);
    }

    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;

    public void measuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;
        if (circleRadius > 0) {
            int newMeasureSpec = View.MeasureSpec.makeMeasureSpec(circleRadius * 2, View.MeasureSpec.EXACTLY);
            mWidthMeasureSpec = newMeasureSpec;
            mHeightMeasureSpec = newMeasureSpec;
        }
    }

    public int getWidthMeasureSpec() {
        return mWidthMeasureSpec;
    }

    public int getHeightMeasureSpec() {
        return mHeightMeasureSpec;
    }

}
