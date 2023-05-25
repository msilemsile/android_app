package me.msile.app.androidapp.test;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.ui.adapter.holder.CommonRecyclerViewHolder;

/**
 * 首页底部切换的tab
 */
public class HomeTabInfoViewHolder extends CommonRecyclerViewHolder<HomeTabInfo> {

    private TextView tvTab;
    private View vTips;

    public HomeTabInfoViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void initViews(View itemView) {
        tvTab = (TextView) itemView.findViewById(R.id.tv_tab);
        vTips = (View) itemView.findViewById(R.id.v_tips);
    }

    @Override
    public void initData(HomeTabInfo data) {
        Object extraInfo = data.getExtraInfo();
        if (extraInfo instanceof String) {
            String tabName = String.valueOf(extraInfo);
            tvTab.setText(tabName);
        }
        int selBgColor = Color.parseColor("#0085D0");
        int unSelBgColor = Color.WHITE;

        if (data.isSelect()) {
            tvTab.setBackgroundColor(selBgColor);
            tvTab.setTextColor(unSelBgColor);
        } else {
            tvTab.setBackgroundColor(unSelBgColor);
            tvTab.setTextColor(selBgColor);
        }
        vTips.setVisibility(data.isNeedTips() ? View.VISIBLE : View.GONE);
        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataAdapter.notifyItemClickListener(mItemView, mData);
            }
        });
    }

    public static class Factory implements CommonRecyclerViewHolder.Factory<HomeTabInfo> {
        @Override
        public CommonRecyclerViewHolder<HomeTabInfo> createViewHolder(View itemView) {
            return new HomeTabInfoViewHolder(itemView);
        }

        @Override
        public int getLayResId() {
            return R.layout.item_home_tab_info;
        }

        @Override
        public Class<HomeTabInfo> getItemDataClass() {
            return HomeTabInfo.class;
        }
    }


}
