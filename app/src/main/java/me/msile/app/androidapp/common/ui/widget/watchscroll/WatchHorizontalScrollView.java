package me.msile.app.androidapp.common.ui.widget.watchscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class WatchHorizontalScrollView extends HorizontalScrollView {

    private WatchScrollListener watchScrollListener;

    public WatchHorizontalScrollView(Context context) {
        super(context);
    }

    public WatchHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WatchHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
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
