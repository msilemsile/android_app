package me.msile.app.androidapp.common.ui.widget.slidetablayout;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.adapter.holder.CommonRecyclerViewHolder;
import me.msile.app.androidapp.common.ui.widget.linelayout.LineTextView;

public class AppSlideTabItemViewHolder extends CommonRecyclerViewHolder<AppSlideTabInfo> {

    private LineTextView tvTabName;
    private AppSlideTabItemConfig tabItemConfig;

    public AppSlideTabItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void initViews(View itemView) {
        tvTabName = (LineTextView) itemView.findViewById(R.id.tv_tab_name);
    }

    @Override
    public void initData(AppSlideTabInfo data) {
        tvTabName.setText(data.getTabName());
        if (tabItemConfig != null) {
            if (data.isSelect()) {
                tvTabName.setLineBottom(true);
                tvTabName.setTextColor(tabItemConfig.getTabTextSelectColor());
            } else {
                tvTabName.setLineBottom(false);
                tvTabName.setTextColor(tabItemConfig.getTabTextNormalColor());
            }
            if (tabItemConfig.isEqualTabs()) {
                int tabsGravity = tabItemConfig.getTabsGravity();
                switch (tabsGravity) {
                    case AppSlideTabItemConfig.TEXT_GRAVITY_CENTER:
                        tvTabName.setGravity(Gravity.CENTER);
                        break;
                    case AppSlideTabItemConfig.TEXT_GRAVITY_ALIGN:
                        int dataPosition = getDataPosition();
                        int adapterSize = mDataAdapter.getItemCount();
                        if (dataPosition == 0) {
                            tvTabName.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                        } else if (dataPosition == (adapterSize - 1)) {
                            tvTabName.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                        }
                        break;
                    case AppSlideTabItemConfig.TEXT_GRAVITY_LEFT:
                        tvTabName.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                        break;
                    case AppSlideTabItemConfig.TEXT_GRAVITY_RIGHT:
                        tvTabName.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                        break;
                }
            }
        }
        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataAdapter.notifyItemClickListener(v, mData);
            }
        });
    }

    @Override
    public RecyclerView.LayoutParams getCustomLayoutParams(ViewGroup parent) {
        if (parent instanceof AppSlideTabLayout) {
            RecyclerView.LayoutParams itemLayoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT);
            AppSlideTabLayout slideTabLayout = (AppSlideTabLayout) parent;
            tabItemConfig = slideTabLayout.getTabItemConfig();
            float equalTabCount = tabItemConfig.getEqualTabCount();
            if (equalTabCount > 0) {
                int parentWidth = slideTabLayout.getWidth() - slideTabLayout.getPaddingLeft() - slideTabLayout.getPaddingRight();
                int parentHeight = slideTabLayout.getHeight() - slideTabLayout.getPaddingTop() - slideTabLayout.getPaddingBottom();
                itemLayoutParams.width = (int) (parentWidth / equalTabCount);
                itemLayoutParams.height = parentHeight;
            }
            itemLayoutParams.setMargins(tabItemConfig.getTabLeftMargin(),
                    tabItemConfig.getTabTopMargin(),
                    tabItemConfig.getTabRightMargin(),
                    tabItemConfig.getTabBottomMargin());
            //init indicator
            tvTabName.setLineBottom(true);
            tvTabName.setLineWidth(tabItemConfig.getIndicatorHeight());
            tvTabName.setLineFixLength(tabItemConfig.getIndicatorFixWidth());
            tvTabName.setLineDrawable(tabItemConfig.getIndicatorDrawable());
            tvTabName.setLineColor(tabItemConfig.getIndicatorColor());
            tvTabName.setLineBottomMargin(tabItemConfig.getIndicatorMargin());
            int textSize = tabItemConfig.getTabTextSize();
            if (textSize > 0) {
                tvTabName.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabItemConfig.getTabTextSize());
            }
            return itemLayoutParams;
        }
        return super.getCustomLayoutParams(parent);
    }

    public static class Factory implements CommonRecyclerViewHolder.Factory<AppSlideTabInfo> {
        @Override
        public CommonRecyclerViewHolder<AppSlideTabInfo> createViewHolder(View itemView) {
            return new AppSlideTabItemViewHolder(itemView);
        }

        @Override
        public int getLayResId() {
            return me.msile.app.androidapp.common.R.layout.item_app_slide_tab_lay;
        }

        @Override
        public Class<AppSlideTabInfo> getItemDataClass() {
            return AppSlideTabInfo.class;
        }
    }
}
