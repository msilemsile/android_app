package me.msile.app.androidapp.test;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.adapter.holder.CommonRecyclerViewHolder;

public class AppWidgetViewHolder extends CommonRecyclerViewHolder<AppWidgetBean> {
    private TextView tvWidgetName;
    private ImageView ivWidgetSpreadOut;
    private TextView tvWidgetDesc;
    private FrameLayout flWidgetDesc;
    private FrameLayout flWidgetName;

    public AppWidgetViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void initViews(View itemView) {
        tvWidgetName = (TextView) findViewById(R.id.tv_widget_name);
        ivWidgetSpreadOut = (ImageView) findViewById(R.id.iv_widget_spread_out);
        tvWidgetDesc = (TextView) findViewById(R.id.tv_widget_desc);
        flWidgetDesc = (FrameLayout) findViewById(R.id.fl_widget_desc);
        flWidgetName = (FrameLayout) findViewById(R.id.fl_widget_name);
    }

    @Override
    public void initData(AppWidgetBean data) {
        tvWidgetName.setText(data.getWidgetName() == null ? "" : data.getWidgetName());
        tvWidgetDesc.setText(data.getWidgetDesc() == null ? "" : data.getWidgetDesc());
        if (data.isSpreadOut()) {
            ivWidgetSpreadOut.setSelected(true);
            flWidgetDesc.setVisibility(View.VISIBLE);
        } else {
            ivWidgetSpreadOut.setSelected(false);
            flWidgetDesc.setVisibility(View.GONE);
        }
        View.OnClickListener spreadOutClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setSpreadOut(!data.isSpreadOut());
                notifyItemDataChange();
            }
        };
        flWidgetName.setOnClickListener(spreadOutClick);
        ivWidgetSpreadOut.setOnClickListener(spreadOutClick);
    }

    public static class Factory implements CommonRecyclerViewHolder.Factory<AppWidgetBean> {

        @Override
        public CommonRecyclerViewHolder<AppWidgetBean> createViewHolder(View itemView) {
            return new AppWidgetViewHolder(itemView);
        }

        @Override
        public int getLayResId() {
            return R.layout.item_app_widget;
        }

        @Override
        public Class<AppWidgetBean> getItemDataClass() {
            return AppWidgetBean.class;
        }
    }
}
