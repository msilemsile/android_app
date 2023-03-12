package me.msile.app.androidapp.common.ui.widget.looperviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.HashSet;
import java.util.Set;

/**
 * 循环RecyclerView Pager (4 * itemTotalCount)
 * <p>
 * 1 --> 2 --> 3 --> 4|startEdge|1 --> 2 --> 3 --> 4|initPosition|1 --> 2 --> 3 --> 4|endEdge|1 --> 2 --> 3 --> 4
 * <p>
 * When current scroll position = startEdge|| endEdge ,Then current scroll position will reset = initPosition
 */

public class LooperRecyclerViewPager extends FrameLayout {

    private ViewPager2 vp2Content;
    private @Nullable
    LooperRecyclerAdapterWrapper mAdapterWrapper;
    private @Nullable
    RecyclerView.Adapter mRealAdapter;
    private @Nullable
    OnPageChangeListener onPageChangeListener;
    private Set<OnPageChangeListener> onPageChangeListenerSet = new HashSet<>();
    private boolean userInputEnable = true;

    public LooperRecyclerViewPager(Context context) {
        super(context);
        init();
    }

    public LooperRecyclerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LooperRecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        vp2Content = new ViewPager2(getContext());
        addView(vp2Content, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        vp2Content.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Log.d("LooperRecyclerViewPager", "onPageSelected pos = " + position);
                if (mAdapterWrapper == null) {
                    return;
                }
                int realPosition = mAdapterWrapper.getRealPosition(position);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPagerSelected(realPosition);
                    onPageChangeListener.onItemSelect(position);
                }
                try {
                    for (OnPageChangeListener onPageChangeListener : onPageChangeListenerSet) {
                        onPageChangeListener.onPagerSelected(realPosition);
                        onPageChangeListener.onItemSelect(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    scrollToInitPosition();
                }
            }
        });
    }

    public boolean isUserInputEnable() {
        return userInputEnable;
    }

    public void setUserInputEnabled(boolean inputEnabled) {
        userInputEnable = inputEnabled;
        vp2Content.setUserInputEnabled(inputEnabled);
    }

    public void setOrientation(@ViewPager2.Orientation int orientation) {
        vp2Content.setOrientation(orientation);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null && mRealAdapter != adapter) {
            mRealAdapter = adapter;
            mAdapterWrapper = new LooperRecyclerAdapterWrapper(mRealAdapter);
            vp2Content.setAdapter(mAdapterWrapper);
            scrollToInitPosition();
        }
    }

    public void addOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        if (onPageChangeListener != null) {
            this.onPageChangeListenerSet.add(onPageChangeListener);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void notifyDataSetChange() {
        if (mAdapterWrapper != null) {
            mAdapterWrapper.notifyDataSetChanged();
        }
    }

    public int getAdapterCount() {
        if (mAdapterWrapper != null) {
            return mAdapterWrapper.getItemCount();
        }
        return -1;
    }

    public int getRealAdapterCount() {
        if (mAdapterWrapper != null) {
            return mAdapterWrapper.getRealAdapterCount();
        }
        return -1;
    }

    public int getRealCurrentPosition() {
        if (mAdapterWrapper != null) {
            int currentItem = vp2Content.getCurrentItem();
            return mAdapterWrapper.getRealPosition(currentItem);
        }
        return -1;
    }

    public int getCurrentPosition() {
        if (mAdapterWrapper != null) {
            return vp2Content.getCurrentItem();
        }
        return -1;
    }

    /**
     * 滚动到初始化位置
     */
    private void scrollToInitPosition() {
        if (mAdapterWrapper == null) {
            return;
        }
        int currentPos = vp2Content.getCurrentItem();
        int realAdapterCount = mAdapterWrapper.getRealAdapterCount();
        int realPosition = mAdapterWrapper.getRealPosition(currentPos);
        int endLooperEdge = realAdapterCount * 3;
        if (realAdapterCount > 1 && (currentPos <= realAdapterCount || currentPos >= endLooperEdge)) {
            int scrollPos = 0;
            if (currentPos <= realAdapterCount) {
                scrollPos = realAdapterCount + realPosition;
            }
            if (currentPos >= endLooperEdge) {
                scrollPos = realAdapterCount * 2 + realPosition;
            }
            if (scrollPos == 0) {
                scrollPos = realAdapterCount * 2;
            }
            vp2Content.setCurrentItem(scrollPos, false);
        }
        Log.d("LooperRecyclerViewPager", "scrollToInitPosition pos = " + currentPos + "| realPos = " + realPosition);
    }

    public boolean canLooperPager() {
        return mAdapterWrapper != null && mAdapterWrapper.getRealAdapterCount() > 1;
    }

    /**
     * 滚动到下一页
     */
    public void scrollToNext() {
        if (!canLooperPager()) {
            return;
        }
        int currentPos = vp2Content.getCurrentItem();
        Log.d("LooperRecyclerViewPager", "onPageScrollStateChanged pos = " + currentPos);
        currentPos++;
        vp2Content.setCurrentItem(currentPos);
    }

    public interface OnPageChangeListener {
        default void onPagerSelected(int pageIndex) {

        }

        default void onItemSelect(int currentItem) {

        }
    }

}
