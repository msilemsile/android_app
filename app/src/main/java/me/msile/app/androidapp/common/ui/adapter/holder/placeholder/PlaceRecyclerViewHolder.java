package me.msile.app.androidapp.common.ui.adapter.holder.placeholder;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import me.msile.app.androidapp.common.ui.adapter.holder.CommonRecyclerViewHolder;

/**
 * 占位布局
 */
public class PlaceRecyclerViewHolder extends CommonRecyclerViewHolder<PlaceModel> {

    public PlaceRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void initViews(View itemView) {

    }

    @Override
    public void initData(PlaceModel data) {

    }

    public static class Factory implements CommonRecyclerViewHolder.Factory<PlaceModel> {

        private @LayoutRes int layoutResId;

        public Factory(@LayoutRes int layoutResId) {
            this.layoutResId = layoutResId;
        }

        @Override
        public CommonRecyclerViewHolder<PlaceModel> createViewHolder(View itemView) {
            return new PlaceRecyclerViewHolder(itemView);
        }

        @Override
        public int getLayResId() {
            return layoutResId;
        }

        @Override
        public Class<PlaceModel> getItemDataClass() {
            return PlaceModel.class;
        }
    }

}
