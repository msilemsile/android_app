package me.msile.app.androidapp.common.ui.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 简单手势view
 */
public class SimpleGestureView extends View {

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private OnSimpleGestureListener mGestureListener;
    private boolean isScrollX = false;
    private boolean isScrollY = false;
    private int scaleType;
    private float beginScale;
    private boolean isMultiPointEvent;
    private boolean needScaleGesture = true;

    public SimpleGestureView(Context context) {
        this(context, null);
    }

    public SimpleGestureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleGestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.i("SimpleGestureView", "onScale " + detector.getScaleFactor());
                return super.onScale(detector);
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                scaleType = 0;
                beginScale = detector.getScaleFactor();
                Log.i("SimpleGestureView", "onScaleBegin " + detector.getScaleFactor());
                return super.onScaleBegin(detector);
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                float scaleDiff = detector.getScaleFactor() - beginScale;
                float absScaleDiff = Math.abs(scaleDiff);
                if (mGestureListener != null && absScaleDiff >= 0.1f) {
                    scaleType = scaleDiff > 0 ? 1 : -1;
                    mGestureListener.onScaleEvent(scaleType, scaleDiff);
                }
                Log.i("SimpleGestureView", "onScaleEnd " + detector.getScaleFactor() + "scaleDiff=" + absScaleDiff);
                super.onScaleEnd(detector);
            }
        });
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onLongPress(MotionEvent e) {
                if (mGestureListener != null) {
                    mGestureListener.onLongPress();
                }
                Log.i("SimpleGestureView", "onLongPress");
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                Log.i("SimpleGestureView", "onScroll");
                if (isMultiPointEvent) {
                    return true;
                }
                if (mGestureListener != null) {
                    int[] screenPos = getDownScreenPos(e1);
                    float absDistanceX = Math.abs(distanceX);
                    float absDistanceY = Math.abs(distanceY);
                    if (isScrollX) {
                        mGestureListener.onScroll(1, distanceX, distanceY, screenPos);
                    }
                    if (isScrollY) {
                        mGestureListener.onScroll(2, distanceX, distanceY, screenPos);
                    }
                    if (!isScrollX && !isScrollY) {
                        if (absDistanceX > absDistanceY) {
                            isScrollX = true;
                            isScrollY = false;
                        } else {
                            isScrollX = false;
                            isScrollY = true;
                        }
                    }
                }
                Log.i("SimpleGestureView", "onScroll");
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                isScrollX = false;
                isScrollY = false;
                Log.i("SimpleGestureView", "onDown");
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mGestureListener != null) {
                    mGestureListener.onDoubleTap();
                }
                Log.i("SimpleGestureView", "onDoubleTap");
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mGestureListener != null) {
                    mGestureListener.onSingleTap();
                }
                Log.i("SimpleGestureView", "onSingleTapConfirmed");
                return true;
            }
        });
    }

    private int[] getDownScreenPos(MotionEvent event) {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return new int[]{0, 0};
        }
        float eventX = event.getX();
        float eventY = event.getY();
        int halfWidth = width >> 1;
        int halfHeight = height >> 1;
        int LR = eventX > halfWidth ? 1 : -1;
        int TB = eventY > halfHeight ? 1 : -1;
        return new int[]{LR, TB};
    }

    public void setNeedScaleGesture(boolean needScaleGesture) {
        this.needScaleGesture = needScaleGesture;
    }

    public void setGestureListener(OnSimpleGestureListener gestureListener) {
        this.mGestureListener = gestureListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionEvent = event.getActionMasked();
        if (needScaleGesture) {
            scaleGestureDetector.onTouchEvent(event);
        }
        if (actionEvent == MotionEvent.ACTION_DOWN) {
            isMultiPointEvent = false;
        }
        if (actionEvent == MotionEvent.ACTION_POINTER_DOWN) {
            isMultiPointEvent = true;
            return true;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    public interface OnSimpleGestureListener {
        default void onSingleTap() {
        }

        default void onDoubleTap() {
        }

        default void onScroll(int scrollDirection, float distanceX, float distanceY, int[] downScreenPos) {
        }

        default void onLongPress() {
        }

        default void onScaleEvent(int scaleType, float scaleDiff) {
        }
    }

}
