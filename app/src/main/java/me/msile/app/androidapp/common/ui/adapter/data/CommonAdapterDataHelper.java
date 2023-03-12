package me.msile.app.androidapp.common.ui.adapter.data;

import androidx.annotation.Nullable;

import me.msile.app.androidapp.common.core.MainThreadHolder;

public abstract class CommonAdapterDataHelper<T> {

    private T mData;
    private OnAdapterDataListener<T> adapterDataListener;
    private boolean canRequestData = true;

    public CommonAdapterDataHelper() {
    }

    public void setAdapterData(@Nullable T t) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                mData = t;
                if (adapterDataListener != null) {
                    adapterDataListener.onGetAdapterData(t);
                }
            }
        });
    }

    public T getAdapterData() {
        return mData;
    }

    public boolean isCanRequestData() {
        return canRequestData;
    }

    public void setCanRequestData(boolean canRequestData) {
        this.canRequestData = canRequestData;
    }

    public void setAdapterDataListener(OnAdapterDataListener<T> adapterDataListener) {
        this.adapterDataListener = adapterDataListener;
    }

    public interface OnAdapterDataListener<T> {
        void onGetAdapterData(@Nullable T t);
    }
}
