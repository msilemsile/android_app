package me.msile.app.androidapp.common.ui.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.rx.DefaultDisposeObserver;
import me.msile.app.androidapp.common.rx.RxTransformerUtils;
import me.msile.app.androidapp.common.ui.widget.looperviewpager.LooperRecyclerViewPager;

/**
 * 自动滚动viewPager
 */
public class CommonAutoLooperViewPager extends LooperRecyclerViewPager {

    private DefaultDisposeObserver<Long> mLooperBannerObserver;
    private int mLooperInternalTime = 4;

    public CommonAutoLooperViewPager(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CommonAutoLooperViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommonAutoLooperViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, me.msile.app.androidapp.R.styleable.CommonAutoLooperViewPager);
        mLooperInternalTime = a.getInt(R.styleable.CommonAutoLooperViewPager_looper_internal_time, 4);
        int mVpOrientation = a.getInt(R.styleable.CommonAutoLooperViewPager_vp_orientation, 0);
        boolean touchEnable = a.getBoolean(R.styleable.CommonAutoLooperViewPager_vp_touch_enable, true);
        a.recycle();
        setOrientation(mVpOrientation == 0 ? ViewPager2.ORIENTATION_HORIZONTAL : ViewPager2.ORIENTATION_VERTICAL);
        setUserInputEnabled(touchEnable);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == GONE) {
            stopLooper();
        } else if (visibility == VISIBLE) {
            Log.d("BANNER", "start");
            startLooper();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isUserInputEnable()) {
            return super.dispatchTouchEvent(ev);
        }
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stopLooper();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                postDelayed(delayStartLooperCallback, mLooperInternalTime);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private final Runnable delayStartLooperCallback = new Runnable() {
        @Override
        public void run() {
            startLooper();
        }
    };

    /**
     * 设置自动滚动间隔时间
     */
    public void setLooperTime(int looperTime) {
        this.mLooperInternalTime = looperTime;
    }

    /**
     * 开始looper自动滚动
     */
    public void startLooper() {
        if (!canLooperPager()) {
            return;
        }
        if (mLooperBannerObserver != null) {
            return;
        }
        mLooperBannerObserver = new DefaultDisposeObserver<Long>() {
            @Override
            protected void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                scrollToNext();
            }
        };
        Observable.interval(mLooperInternalTime, TimeUnit.SECONDS)
                .compose(RxTransformerUtils.mainSchedulers())
                .subscribe(mLooperBannerObserver);
    }

    /**
     * 关闭自动滚动
     */
    public void stopLooper() {
        removeCallbacks(delayStartLooperCallback);
        if (mLooperBannerObserver != null) {
            mLooperBannerObserver.dispose();
            mLooperBannerObserver = null;
        }
    }
}
