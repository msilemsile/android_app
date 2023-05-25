package me.msile.app.androidapp.common.ui.widget.watchscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class WatchScrollView extends ScrollView {

    private WatchScrollListener watchScrollListener;

    public WatchScrollView(Context context) {
        super(context);
    }

    public WatchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WatchScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (watchScrollListener != null) {
            watchScrollListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setWatchScrollListener(WatchScrollListener watchScrollListener) {
        this.watchScrollListener = watchScrollListener;
    }
}
