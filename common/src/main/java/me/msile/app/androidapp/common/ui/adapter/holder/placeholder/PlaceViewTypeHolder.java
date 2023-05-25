package me.msile.app.androidapp.common.ui.adapter.holder.placeholder;

import me.msile.app.androidapp.common.ui.adapter.holder.ItemViewTypeHolder;

public class PlaceViewTypeHolder implements ItemViewTypeHolder {

    @Override
    public int getItemViewType(Object obj) {
        if (obj instanceof PlaceModel) {
            PlaceModel holderModel = (PlaceModel) obj;
            return holderModel.getLayoutResId();
        }
        return 0;
    }
}
