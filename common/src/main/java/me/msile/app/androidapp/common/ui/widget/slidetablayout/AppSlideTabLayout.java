package me.msile.app.androidapp.common.ui.widget.slidetablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.adapter.CommonRecyclerAdapter;

public class AppSlideTabLayout extends RecyclerView {

    private AppSlideTabItemConfig tabItemConfig;
    private CommonRecyclerAdapter mAdapter;
    private int mLastSelectTab = -1;
    private OnTabChangeListener mOnTabChangeListener;
    private LinearLayoutManager linearLayoutManager;

    public AppSlideTabLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AppSlideTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AppSlideTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        tabItemConfig = new AppSlideTabItemConfig();
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, me.msile.app.androidapp.common.R.styleable.AppSlideTabLayout);
        float mEqualTabCount = typedArray.getFloat(R.styleable.AppSlideTabLayout_stl_equal_tab_count, 0);
        tabItemConfig.setEqualTabCount(mEqualTabCount);
        boolean mEqualTabs = typedArray.getBoolean(R.styleable.AppSlideTabLayout_stl_equal_tabs, false);
        tabItemConfig.setEqualTabs(mEqualTabs);
        int mTabsGravity = typedArray.getInt(R.styleable.AppSlideTabLayout_stl_tabs_gravity, 0);
        tabItemConfig.setTabsGravity(mTabsGravity);
        int mTabLeftMargin = typedArray.getDimensionPixelSize(R.styleable.AppSlideTabLayout_stl_tab_left_margin, 0);
        tabItemConfig.setTabLeftMargin(mTabLeftMargin);
        int mTabRightMargin = typedArray.getDimensionPixelSize(R.styleable.AppSlideTabLayout_stl_tab_right_margin, 0);
        tabItemConfig.setTabRightMargin(mTabRightMargin);
        int mTabTopMargin = typedArray.getDimensionPixelSize(R.styleable.AppSlideTabLayout_stl_tab_top_margin, 0);
        tabItemConfig.setTabTopMargin(mTabTopMargin);
        int mTabBottomMargin = typedArray.getDimensionPixelSize(R.styleable.AppSlideTabLayout_stl_tab_bottom_margin, 0);
        tabItemConfig.setTabBottomMargin(mTabBottomMargin);
        Drawable mIndicatorDrawable = typedArray.getDrawable(R.styleable.AppSlideTabLayout_stl_tab_indicator_drawable);
        tabItemConfig.setIndicatorDrawable(mIndicatorDrawable);
        int mIndicatorFixWidth = typedArray.getDimensionPixelSize(R.styleable.AppSlideTabLayout_stl_tab_indicator_fix_width, 0);
        tabItemConfig.setIndicatorFixWidth(mIndicatorFixWidth);
        int mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.AppSlideTabLayout_stl_tab_indicator_height, 0);
        tabItemConfig.setIndicatorHeight(mIndicatorHeight);
        int mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.AppSlideTabLayout_stl_tab_indicator_margin, 0);
        tabItemConfig.setIndicatorMargin(mIndicatorMargin);
        int mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.AppSlideTabLayout_stl_tab_text_size, 0);
        tabItemConfig.setTabTextSize(mTabTextSize);
        int defaultNormalColor = getResources().getColor(R.color.color_999999);
        int mTabTextNormalColor = typedArray.getColor(R.styleable.AppSlideTabLayout_stl_tab_text_normal_color, defaultNormalColor);
        tabItemConfig.setTabTextNormalColor(mTabTextNormalColor);
        int defaultSelectColor = getResources().getColor(R.color.color_333333);
        int mTabTextSelectColor = typedArray.getColor(R.styleable.AppSlideTabLayout_stl_tab_text_select_color, defaultSelectColor);
        tabItemConfig.setTabTextSelectColor(mTabTextSelectColor);
        int mTabIndicatorColor = typedArray.getColor(R.styleable.AppSlideTabLayout_stl_tab_indicator_color, 0);
        tabItemConfig.setIndicatorColor(mTabIndicatorColor);
        typedArray.recycle();
        initTabLayout(context);
    }

    private void initTabLayout(Context context) {
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(linearLayoutManager);
        mAdapter = new CommonRecyclerAdapter(false);
        mAdapter.addViewHolderFactory(new AppSlideTabItemViewHolder.Factory());
        mAdapter.addItemEventListener(new CommonRecyclerAdapter.OnItemEventListener() {
            @Override
            public void onClickItemView(View view, Object obj) {
                int itemIndex = mAdapter.findItemDataIndex(obj);
                setCurrentTab(itemIndex, true);
            }
        });
        setAdapter(mAdapter);
    }

    public void setOnTabChangeListener(OnTabChangeListener mOnTabChangeListener) {
        this.mOnTabChangeListener = mOnTabChangeListener;
    }

    public void addTabList(List<AppSlideTabInfo> tabInfoList) {
        if (tabInfoList != null && !tabInfoList.isEmpty()) {
            if (tabItemConfig.isEqualTabs()) {
                tabItemConfig.setEqualTabCount(tabInfoList.size());
            }
            mAdapter.addDataList(tabInfoList);
        }
    }

    public void addTabStringList(List<String> tabNameList) {
        if (tabNameList != null && !tabNameList.isEmpty()) {
            List<AppSlideTabInfo> tabInfoList = new ArrayList<>();
            for (String tabName : tabNameList) {
                tabInfoList.add(new AppSlideTabInfo(tabName));
            }
            addTabList(tabInfoList);
        }
    }

    public void clearTabList() {
        mLastSelectTab=-1;
        mAdapter.removeAllData();
    }

    public void setCurrentTab(int tabIndex) {
        setCurrentTab(tabIndex, false);
    }

    public void setCurrentTab(int tabIndex, boolean needCallback) {
        if (mLastSelectTab == tabIndex) {
            return;
        }
        changeTabSelectState(mLastSelectTab, false);
        mLastSelectTab = tabIndex;
        changeTabSelectState(mLastSelectTab, true);
        if (needCallback && mOnTabChangeListener != null) {
            AppSlideTabInfo tabInfo = (AppSlideTabInfo) mAdapter.getData(tabIndex);
            mOnTabChangeListener.onTabSelected(tabIndex, tabInfo);
            mOnTabChangeListener.onTabSelected(tabIndex);
        }
        int scrollOffset = 0;
        View tabItemView = linearLayoutManager.findViewByPosition(tabIndex);
        if (tabItemView != null) {
            int tabItemViewWidth = tabItemView.getWidth();
            scrollOffset = (getWidth() - tabItemViewWidth) / 2;
        }
        linearLayoutManager.scrollToPositionWithOffset(mLastSelectTab, scrollOffset);
    }

    public void changeTabSelectState(int index, boolean isSelect) {
        AppSlideTabInfo data = (AppSlideTabInfo) mAdapter.getData(index);
        if (data != null) {
            data.setSelect(isSelect);
            mAdapter.notifyItemChanged(index);
        }
    }

    public interface OnTabChangeListener {
        default void onTabSelected(int tabIndex, AppSlideTabInfo tabInfo) {
        }

        default void onTabSelected(int tabIndex) {
        }
    }

    public AppSlideTabItemConfig getTabItemConfig() {
        return tabItemConfig;
    }

    public List getTabInfoList() {
        return mAdapter.getDataList();
    }
}
