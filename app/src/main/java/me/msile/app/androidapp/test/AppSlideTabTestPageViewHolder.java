package me.msile.app.androidapp.test;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.ui.adapter.holder.CommonRecyclerViewHolder;
import me.msile.app.androidapp.common.ui.widget.slidetablayout.AppSlideTabInfo;

public class AppSlideTabTestPageViewHolder extends CommonRecyclerViewHolder<AppSlideTabInfo> {

    private TextView tvTabName;

    public AppSlideTabTestPageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void initViews(View itemView) {
        tvTabName = (TextView) itemView.findViewById(R.id.tv_page);
    }

    @Override
    public void initData(AppSlideTabInfo data) {
        tvTabName.setText(data.getTabName());
    }

    public static class Factory implements CommonRecyclerViewHolder.Factory<AppSlideTabInfo> {
        @Override
        public CommonRecyclerViewHolder<AppSlideTabInfo> createViewHolder(View itemView) {
            return new AppSlideTabTestPageViewHolder(itemView);
        }

        @Override
        public int getLayResId() {
            return me.msile.app.androidapp.R.layout.item_slide_tab_test_page;
        }

        @Override
        public Class<AppSlideTabInfo> getItemDataClass() {
            return AppSlideTabInfo.class;
        }
    }
}
