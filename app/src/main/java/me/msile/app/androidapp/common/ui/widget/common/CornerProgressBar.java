package me.msile.app.androidapp.common.ui.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import me.msile.app.androidapp.R;

public class CornerProgressBar extends View {

    private Paint mPaint;
    private int mBgColor;
    private int mProColor;
    private int mProWidth;
    private int mCornerRadius;
    private RectF mTargetRectF = new RectF();
    private int mCurrentPercent;

    public CornerProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CornerProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CornerProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, me.msile.app.androidapp.R.styleable.CornerProgressBar);
        int defBgColor = getResources().getColor(R.color.color_cccccc);
        mBgColor = typedArray.getColor(R.styleable.CornerProgressBar_csb_bar_bg_color, defBgColor);
        int defProColor = getResources().getColor(R.color.color_0091FF);
        mProColor = typedArray.getColor(R.styleable.CornerProgressBar_csb_bar_pro_color, defProColor);
        mProWidth = typedArray.getDimensionPixelSize(R.styleable.CornerProgressBar_csb_bar_pro_width, 0);
        mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.CornerProgressBar_csb_corner_radius, 0);
        mCurrentPercent = typedArray.getInt(R.styleable.CornerProgressBar_csb_bar_pro_value, 0);
        typedArray.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        setScrollProgress(mCurrentPercent);
    }

    public void setScrollProgress(int percent) {
        if (percent < 0 || percent > 100) {
            return;
        }
        mCurrentPercent = percent;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) {
            return;
        }
        if (mCornerRadius == 0) {
            mCornerRadius = height / 2;
        }
        //draw bg
        mPaint.setColor(mBgColor);
        mTargetRectF.set(0, 0, width, height);
        canvas.drawRoundRect(mTargetRectF, mCornerRadius, mCornerRadius, mPaint);
        //draw pro
        if (mProWidth == 0) {
            return;
        }
        mPaint.setColor(mProColor);
        int totalProWidth = width - mProWidth;
        int currentProStart = (int) (mCurrentPercent / 100.0f * totalProWidth);
        mTargetRectF.set(currentProStart, 0, currentProStart + mProWidth, height);
        canvas.drawRoundRect(mTargetRectF, mCornerRadius, mCornerRadius, mPaint);
    }
}
