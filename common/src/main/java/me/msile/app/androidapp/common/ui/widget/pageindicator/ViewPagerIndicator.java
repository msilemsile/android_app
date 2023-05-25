package me.msile.app.androidapp.common.ui.widget.pageindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.utils.DensityUtil;

/**
 * viewPager底部指示器
 */
public class ViewPagerIndicator extends View {

    private Paint mPaint;
    private int mTotalSize;
    private int mCurrentIndex;
    private int mIndicatorRadius;
    private int mIndicatorMargin;
    private int mSelectColor;
    private int mNormalColor;

    public ViewPagerIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mNormalColor = a.getColor(R.styleable.ViewPagerIndicator_normal_color, 0xFFE5E5E5);
        mSelectColor = a.getColor(R.styleable.ViewPagerIndicator_select_color, 0xFF0091FF);
        mIndicatorRadius = a.getDimensionPixelSize(R.styleable.ViewPagerIndicator_indicator_circle_radius, 0);
        mIndicatorMargin = a.getDimensionPixelSize(R.styleable.ViewPagerIndicator_indicator_circle_margin, 0);
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        if (mIndicatorRadius <= 0) {
            mIndicatorRadius = DensityUtil.dip2px(2);
        }
        if (mIndicatorMargin <= 0) {
            mIndicatorMargin = DensityUtil.dip2px(4);
        }
    }

    public void initPager(int totalSize, int initIndex) {
        mTotalSize = totalSize;
        mCurrentIndex = initIndex;
        postInvalidate();
    }

    public void setCurrentIndicator(int currentIndex) {
        if (mTotalSize <= 1) {
            return;
        }
        mCurrentIndex = currentIndex;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mIndicatorRadius * 2;
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTotalSize <= 1) {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        if (width < 0 || height < 0) {
            return;
        }
        int itemWidth = mIndicatorRadius * 2 + mIndicatorMargin;
        int totalHalfWidth = (mTotalSize - 1) * itemWidth / 2;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        if (totalHalfWidth >= halfWidth) {
            totalHalfWidth = halfWidth;
        }
        int startX = halfWidth - totalHalfWidth;
        for (int i = 0; i < mTotalSize; i++) {
            if (mCurrentIndex == i) {
                mPaint.setColor(mSelectColor);
            } else {
                mPaint.setColor(mNormalColor);
            }
            int centerX = startX + i * itemWidth;
            canvas.drawCircle(centerX, halfHeight, mIndicatorRadius, mPaint);
        }
    }
}
