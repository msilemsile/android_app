package me.msile.app.androidapp.common.web.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.widget.linelayout.LineFrameLayout;
import me.msile.app.androidapp.common.utils.DensityUtil;
import me.msile.app.androidapp.common.utils.StatusBarUtils;

/**
 * 网页标题
 */
public class WebTitleLayout extends LineFrameLayout implements View.OnClickListener {

    public static final int TYPE_BACK = 0;
    public static final int TYPE_CLOSE = 1;
    public static final int TYPE_REFRESH = 2;

    private int defaultHeight;
    private int statusBarHeight;
    private boolean isFullScreen;

    private boolean showBottomLine;
    private String titleText;
    private boolean titleBold;
    private boolean showBack;
    private boolean showClose;
    private boolean showRefresh;
    private boolean forbidCloseBtn;

    private ImageView ivBack;
    private ImageView ivClose;
    private TextView tvTitle;
    private ImageView ivRefresh;

    private OnTitleClickListener onTitleClickListener;

    public WebTitleLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public WebTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WebTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        defaultHeight = DensityUtil.dip2px(48);
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.WebTitleLayout);
        titleText = array.getString(R.styleable.WebTitleLayout_wtl_title_text);
        titleBold = array.getBoolean(R.styleable.WebTitleLayout_wtl_title_bold, false);
        showBack = array.getBoolean(R.styleable.WebTitleLayout_wtl_show_back, true);
        showClose = array.getBoolean(R.styleable.WebTitleLayout_wtl_show_close, false);
        showRefresh = array.getBoolean(R.styleable.WebTitleLayout_wtl_show_refresh, true);
        showBottomLine = array.getBoolean(R.styleable.WebTitleLayout_wtl_show_bottom_line, true);
        isFullScreen = array.getBoolean(R.styleable.WebTitleLayout_wtl_full_screen, false);
        array.recycle();
        LayoutInflater.from(context).inflate(R.layout.merge_app_web_title, this);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRefresh = (ImageView) findViewById(R.id.iv_refresh);
        ivBack.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onTitleClickListener != null) {
                    onTitleClickListener.onLongClickTitle(v);
                }
                return true;
            }
        });
        setTitleText(titleText);
        setTitleBold(titleBold);
        setShowBack(showBack);
        setShowClose(showClose);
        setShowRefresh(showRefresh);
        setShowBottomLine(showBottomLine);
        setFullScreen(isFullScreen);
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        if (tvTitle != null && titleText != null) {
            tvTitle.setText(titleText);
        }
    }

    public View getTitleTextView() {
        return tvTitle;
    }

    public void setTitleBold(boolean titleBold) {
        this.titleBold = titleBold;
        if (tvTitle != null && titleBold) {
            tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    public void setShowBack(boolean showBack) {
        this.showBack = showBack;
        if (ivBack != null) {
            ivBack.setVisibility(showBack ? VISIBLE : GONE);
        }
    }

    public void setShowClose(boolean showClose) {
        if (forbidCloseBtn) {
            if (ivClose != null && ivClose.getVisibility() == VISIBLE) {
                ivClose.setVisibility(GONE);
            }
        } else {
            this.showClose = showClose;
            if (ivClose != null) {
                ivClose.setVisibility(showClose ? VISIBLE : GONE);
            }
        }
    }

    /**
     * 禁用关闭按钮
     */
    public void forbidCloseBtn(boolean forbidCloseBtn) {
        this.forbidCloseBtn = forbidCloseBtn;
        if (forbidCloseBtn) {
            setShowClose(false);
        }
    }

    public void setShowRefresh(boolean showRefresh) {
        this.showRefresh = showRefresh;
        if (ivRefresh != null) {
            ivRefresh.setVisibility(showRefresh ? VISIBLE : GONE);
        }
    }

    public void setRefreshClickable(boolean canRefresh) {
        if (ivRefresh != null) {
            ivRefresh.setClickable(canRefresh);
        }
    }

    public void setShowBottomLine(boolean showBottomLine) {
        this.showBottomLine = showBottomLine;
        if (showBottomLine) {
            setLineBottom(true);
            setLineColor(getResources().getColor(R.color.color_e5e5e5));
            setLineWidth(DensityUtil.dip2px(1));
        } else {
            setLineBottom(false);
        }
        invalidate();
    }

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onTitleClickListener != null) {
            int id = v.getId();
            if (id == R.id.iv_back) {
                onTitleClickListener.onClick(TYPE_BACK, v);
            } else if (id == R.id.iv_close) {
                onTitleClickListener.onClick(TYPE_CLOSE, v);
            } else if (id == R.id.iv_refresh) {
                ivRefresh.animate().rotationBy(360).setDuration(300).start();
                onTitleClickListener.onClick(TYPE_REFRESH, v);
            }
        }
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
        if (ivBack == null) {
            return;
        }
        if (isFullScreen) {
            statusBarHeight = StatusBarUtils.getStatusBarHeight();
            setPadding(0, statusBarHeight, 0, 0);
        } else {
            statusBarHeight = 0;
            setPadding(0, 0, 0, 0);
        }
        requestLayout();
    }

    public interface OnTitleClickListener {
        void onClick(int type, View view);

        default void onLongClickTitle(View view) {

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(defaultHeight + statusBarHeight, View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

}
