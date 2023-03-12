package me.msile.app.androidapp.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.msile.app.androidapp.common.ui.adapter.CommonRecyclerAdapter;

/**
 * 首页底部tab切换整体布局
 */
public class HomeTabLayout extends RecyclerView {

    private CommonRecyclerAdapter mAdapter;
    private int mLastSelectTab = -1;
    private OnTabChangeListener mOnTabChangeListener;

    public HomeTabLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HomeTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        setLayoutManager(new GridLayoutManager(context, 3) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mAdapter = new CommonRecyclerAdapter(false);
        mAdapter.addViewHolderFactory(new HomeTabInfoViewHolder.Factory());
        mAdapter.addItemEventListener(new CommonRecyclerAdapter.OnItemEventListener() {
            @Override
            public void onClickItemView(View view, Object obj) {
                int itemIndex = mAdapter.findItemDataIndex(obj);
                setCurrentTab(itemIndex, true);
            }
        });
        setAdapter(mAdapter);
    }

    public void setAdapterItemChanged(int i) {
        mAdapter.notifyItemChanged(i);
    }

    public void setOnTabChangeListener(OnTabChangeListener mOnTabChangeListener) {
        this.mOnTabChangeListener = mOnTabChangeListener;
    }

    public void addTabList(List<HomeTabInfo> tabInfoList) {
        mAdapter.removeAllData();
        mAdapter.addDataList(tabInfoList);
    }

    public void notifyDataSetChangedAll() {
        mAdapter.notifyDataSetChanged();
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
            mOnTabChangeListener.onTabSelected(tabIndex);
        }
    }

    public void changeTabSelectState(int index, boolean isSelect) {
        HomeTabInfo data = (HomeTabInfo) mAdapter.getData(index);
        if (data != null) {
            data.setSelect(isSelect);
            mAdapter.notifyItemChanged(index);
        }
    }

    public interface OnTabChangeListener {
        void onTabSelected(int tabIndex);
    }
}
