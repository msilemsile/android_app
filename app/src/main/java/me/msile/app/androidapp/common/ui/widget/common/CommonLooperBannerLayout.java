package me.msile.app.androidapp.common.ui.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.ui.widget.looperviewpager.LooperRecyclerViewPager;
import me.msile.app.androidapp.common.ui.widget.pageindicator.ViewPagerIndicator;
import me.msile.app.androidapp.common.utils.DensityUtil;

/**
 * 通用banner循环广告位
 */
public class CommonLooperBannerLayout extends FrameLayout {

    private CommonAutoLooperViewPager looperRecyclerViewPager;
    private ViewPagerIndicator viewPagerIndicator;

    private float mWHRatio = 0;

    public CommonLooperBannerLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CommonLooperBannerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommonLooperBannerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, me.msile.app.androidapp.R.styleable.CommonLooperBannerLayout);
        mWHRatio = a.getFloat(R.styleable.CommonLooperBannerLayout_banner_ratio_w_h, 0);
        int looperTime = a.getInt(R.styleable.CommonLooperBannerLayout_looper_banner_time, 4);
        boolean showIndicator = a.getBoolean(R.styleable.CommonLooperBannerLayout_show_indicator, false);
        int indicatorBottomMargin = a.getDimensionPixelSize(R.styleable.CommonLooperBannerLayout_indicator_margin_bottom, 0);
        a.recycle();

        //add looper pager
        looperRecyclerViewPager = new CommonAutoLooperViewPager(context);
        looperRecyclerViewPager.setLooperTime(looperTime);
        addView(looperRecyclerViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //add indicator
        if (showIndicator) {
            viewPagerIndicator = new ViewPagerIndicator(context);
            if (indicatorBottomMargin <= 0) {
                indicatorBottomMargin = DensityUtil.dip2px(10);
            }
            LayoutParams indicatorParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            indicatorParams.gravity = Gravity.BOTTOM;
            indicatorParams.bottomMargin = indicatorBottomMargin;
            addView(viewPagerIndicator, indicatorParams);
            looperRecyclerViewPager.setOnPageChangeListener(new LooperRecyclerViewPager.OnPageChangeListener() {
                @Override
                public void onPagerSelected(int pageIndex) {
                    viewPagerIndicator.setCurrentIndicator(pageIndex);
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWHRatio > 0) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            if (widthMode == MeasureSpec.EXACTLY) {
                int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                if (widthSize > 0) {
                    int heightSize = (int) (widthSize * 1.0f / mWHRatio);
                    int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
                    super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
                    return;
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public CommonAutoLooperViewPager getLooperRecyclerViewPager() {
        return looperRecyclerViewPager;
    }

    public ViewPagerIndicator getViewPagerIndicator() {
        return viewPagerIndicator;
    }
}
