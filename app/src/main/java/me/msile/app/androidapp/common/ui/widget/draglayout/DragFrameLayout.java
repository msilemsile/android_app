package me.msile.app.androidapp.common.ui.widget.draglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * 拖拽view 布局
 */

public class DragFrameLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;
    private boolean isTouchChild;
    private int touchChildX, touchChildY;
    private boolean isMoveChild;
    private View touchChild;
    private OnClickChildListener onClickChildListener;

    public DragFrameLayout(Context context) {
        super(context);
        init();
    }

    public DragFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnClickChildListener(OnClickChildListener onClickChildListener) {
        this.onClickChildListener = onClickChildListener;
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - child.getWidth() - leftBound;

                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - child.getHeight() - topBound;

                final int newTop = Math.min(Math.max(top, topBound), bottomBound);
                return newTop;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                int parentWidth = getWidth();
                int childWidth = releasedChild.getWidth();
                if (parentWidth <= 0 || childWidth <= 0) {
                    return;
                }
                int widthOffset = parentWidth - childWidth;
                int halfWidth = widthOffset / 2;
                int top = releasedChild.getTop();
                int left = releasedChild.getLeft();
                if (left <= halfWidth) {
                    mDragHelper.smoothSlideViewTo(releasedChild, 0, top);
                } else {
                    mDragHelper.smoothSlideViewTo(releasedChild, widthOffset, top);
                }
                ViewCompat.postInvalidateOnAnimation(DragFrameLayout.this);
            }
        });
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(DragFrameLayout.this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            touchChild = mDragHelper.findTopChildUnder((int) ev.getX(), (int) ev.getY());
            isTouchChild = touchChild != null;
        }
        return isTouchChild && mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouchChild) {
            return false;
        }
        mDragHelper.processTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchChildX = (int) event.getX();
                touchChildY = (int) event.getY();
                isMoveChild = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                if (Math.abs(touchChildX - moveX) >= mDragHelper.getTouchSlop() || Math.abs(touchChildY - moveY) >= mDragHelper.getTouchSlop()) {
                    isMoveChild = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isMoveChild && onClickChildListener != null) {
                    onClickChildListener.onClick(touchChild);
                }
                break;
        }
        return true;
    }

    public interface OnClickChildListener {
        void onClick(View childView);
    }

}