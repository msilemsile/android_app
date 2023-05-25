package me.msile.app.androidapp.common.ui.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.utils.DensityUtil;

/**
 * 索引view
 */
public class IndexBarView extends View {

    private Paint mCirclePaint;
    private Paint mTextPaint;
    private int mIndexPaddingTop;
    private int mIndexPaddingBottom;
    private int mIndexPaddingLeft;
    private int mIndexPaddingRight;
    private float mPerLetterAreaHeight;
    private Rect mTextRect = new Rect();
    private RectF mIndexCircleRect = new RectF();
    private int mTextNormalColor;
    private int mTextPressColor;
    private int mLastIndex = -1;
    private int mCurrentIndex = -1;
    private List<String> mIndexList = new ArrayList<>();
    private int mMaxTextSize;
    private boolean mTouchTextBold;
    private boolean mIsTouchingIndexBar;
    private int mLeftDrawTextRange;
    private int mRightDrawTextRange;
    private int mTopDrawTextRange;
    private int mBottomDrawTextRange;
    private int mMaxIndexLength = 1;
    private int mIndexCircleRadius;
    private int mIndexCircleOffset;
    private int mIndexCircleColor;
    private int mIndexCircleTextColor;

    public IndexBarView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public IndexBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndexBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, me.msile.app.androidapp.common.R.styleable.IndexBarView);
        int defaultMaxSize = DensityUtil.dip2px(14);
        int defaultIndexPadding = DensityUtil.dip2px(2);
        mMaxTextSize = typedArray.getDimensionPixelSize(R.styleable.IndexBarView_ibv_max_text_size, defaultMaxSize);
        mIndexPaddingLeft = typedArray.getDimensionPixelOffset(R.styleable.IndexBarView_ibv_index_padding_left, defaultIndexPadding);
        mIndexPaddingTop = typedArray.getDimensionPixelOffset(R.styleable.IndexBarView_ibv_index_padding_top, defaultIndexPadding);
        mIndexPaddingRight = typedArray.getDimensionPixelOffset(R.styleable.IndexBarView_ibv_index_padding_right, defaultIndexPadding);
        mIndexPaddingBottom = typedArray.getDimensionPixelOffset(R.styleable.IndexBarView_ibv_index_padding_bottom, defaultIndexPadding);
        mTextNormalColor = typedArray.getColor(R.styleable.IndexBarView_ibv_text_normal_color, 0);
        mTextPressColor = typedArray.getColor(R.styleable.IndexBarView_ibv_text_press_color, 0);
        mTouchTextBold = typedArray.getBoolean(R.styleable.IndexBarView_ibv_text_press_bold, false);
        mIndexCircleRadius = DensityUtil.dip2px(30);
        mIndexCircleOffset = mIndexCircleRadius;
        mIndexCircleColor = getResources().getColor(R.color.color_aeaeae);
        mIndexCircleTextColor = getResources().getColor(R.color.white);
        typedArray.recycle();
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mIndexCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        String[] defaultLetters = new String[]{"A", "B", "C", "D",
                "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
        mIndexList.addAll(Arrays.asList(defaultLetters));
    }

    public void setIndexList(List<String> indexList) {
        if (indexList == null || indexList.isEmpty()) {
            return;
        }
        mIndexList = indexList;
        invalidateMaxTextLength();
    }

    public void addFirstIndex(String firstIndex) {
        if (TextUtils.isEmpty(firstIndex)) {
            return;
        }
        mIndexList.add(0, firstIndex);
    }

    public void addLastIndex(String lastIndex) {
        if (TextUtils.isEmpty(lastIndex)) {
            return;
        }
        mIndexList.add(lastIndex);
    }

    public void invalidateMaxTextLength() {
        int maxTextLength = 1;
        for (int i = 0; i < mIndexList.size(); i++) {
            int length = mIndexList.get(i).length();
            if (length > maxTextLength) {
                maxTextLength = length;
            }
        }
        if (maxTextLength > mMaxIndexLength) {
            mMaxIndexLength = maxTextLength;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIndexList == null || mIndexList.isEmpty()) {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) {
            return;
        }
        int circleSize = mIndexCircleRadius * 2;

        mPerLetterAreaHeight = (height - circleSize) * 1.0f / mIndexList.size();
        float mTextSize = mPerLetterAreaHeight - mIndexPaddingTop - mIndexPaddingBottom;
        if (mTextSize <= 0) {
            return;
        }
        if (mTextSize >= mMaxTextSize) {
            mTextSize = mMaxTextSize;
            int padding = (int) ((mPerLetterAreaHeight - mTextSize) / 2);
            mIndexPaddingTop = padding;
            mIndexPaddingBottom = padding;
        }
        mTextPaint.setTextSize(mTextSize);

        int l = (int) (width - mTextSize * mMaxIndexLength - mIndexPaddingLeft - 2 * mIndexPaddingRight);
        int t;
        int r = width - mIndexPaddingRight;
        int b;
        mLeftDrawTextRange = l;
        mRightDrawTextRange = r;
        mTopDrawTextRange = mIndexCircleRadius;
        mBottomDrawTextRange = height - mIndexCircleRadius;

        for (int i = 0; i < mIndexList.size(); i++) {
            String content = mIndexList.get(i);
            l = (int) (width - mTextSize * content.length() - mIndexPaddingLeft - mIndexPaddingRight);
            t = (int) (mPerLetterAreaHeight * i) + mTopDrawTextRange;
            b = (int) (t + mPerLetterAreaHeight);
            //构建你的文本所处的矩形
            mTextRect.set(l, t, mRightDrawTextRange, b);

            if (i == mCurrentIndex && mIsTouchingIndexBar) {
                mTextPaint.setColor(mTextPressColor);
                mTextPaint.setTextSize(mTextSize);
                mTextPaint.setFakeBoldText(mTouchTextBold);
                Paint.FontMetricsInt textFontMetrics = mTextPaint.getFontMetricsInt();
                int baseline = (mTextRect.bottom + mTextRect.top - textFontMetrics.bottom - textFontMetrics.top) / 2;
                canvas.drawText(content, mTextRect.centerX(), baseline, mTextPaint);
                //左侧绘制当前圆形索引
                mIndexCircleRect.set(mLeftDrawTextRange - circleSize - mIndexCircleOffset, baseline - mIndexCircleRadius, mLeftDrawTextRange - mIndexCircleOffset, baseline + mIndexCircleRadius);
                canvas.drawCircle(mIndexCircleRect.centerX(), mIndexCircleRect.centerY(), mIndexCircleRadius, mCirclePaint);
                mTextPaint.setColor(mIndexCircleTextColor);
                mTextPaint.setTextSize(mIndexCircleRadius);
                mTextPaint.setFakeBoldText(true);
                Paint.FontMetricsInt circleTextFontMetrics = mTextPaint.getFontMetricsInt();
                int textY = (int) mIndexCircleRect.centerY() - (circleTextFontMetrics.bottom + circleTextFontMetrics.top) / 2;
                canvas.drawText(content, mIndexCircleRect.centerX(), textY, mTextPaint);
            } else {
                mTextPaint.setColor(mTextNormalColor);
                mTextPaint.setTextSize(mTextSize);
                mTextPaint.setFakeBoldText(false);
                Paint.FontMetricsInt textFontMetrics = mTextPaint.getFontMetricsInt();
                int baseline = (mTextRect.bottom + mTextRect.top - textFontMetrics.bottom - textFontMetrics.top) / 2;
                canvas.drawText(content, mTextRect.centerX(), baseline, mTextPaint);
            }
        }
    }

    private boolean checkIsTouchIndexBar(float x, float y) {
        return x >= mLeftDrawTextRange;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mIsTouchingIndexBar = checkIsTouchIndexBar(event.getX(), event.getY());
        }
        if (!mIsTouchingIndexBar) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float mIndexTouchY = event.getY() - mTopDrawTextRange;
                int touchDownIndex = (int) (mIndexTouchY / mPerLetterAreaHeight);
                if (touchDownIndex < 0) {
                    touchDownIndex = 0;
                }
                if (touchDownIndex > mIndexList.size() - 1) {
                    touchDownIndex = mIndexList.size() - 1;
                }
                mCurrentIndex = touchDownIndex;
                invalidate();
                if (mOnTouchIndexBarListener != null) {
                    mOnTouchIndexBarListener.onTouchIndexBar(touchDownIndex, mIndexList.get(mCurrentIndex));
                    mOnTouchIndexBarListener.onTouchIndexBarAllTheTime(touchDownIndex, mIndexTouchY);
                }
                mLastIndex = touchDownIndex;
                break;
            case MotionEvent.ACTION_MOVE:
                mIndexTouchY = event.getY() - mTopDrawTextRange;
                int touchMoveIndex = (int) (mIndexTouchY / mPerLetterAreaHeight);
                if (touchMoveIndex < 0) {
                    touchMoveIndex = 0;
                }
                if (touchMoveIndex > mIndexList.size() - 1) {
                    touchMoveIndex = mIndexList.size() - 1;
                }
                if (touchMoveIndex != mLastIndex) {
                    mCurrentIndex = touchMoveIndex;
                    invalidate();
                    if (mOnTouchIndexBarListener != null) {
                        mOnTouchIndexBarListener.onTouchIndexBar(touchMoveIndex, mIndexList.get(mCurrentIndex));
                    }
                    mLastIndex = touchMoveIndex;
                }
                if (mOnTouchIndexBarListener != null) {
                    mOnTouchIndexBarListener.onTouchIndexBarAllTheTime(touchMoveIndex, mIndexTouchY);
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsTouchingIndexBar = false;
                invalidate();
                if (mOnTouchIndexBarListener != null) {
                    mOnTouchIndexBarListener.onTouchActionUp();
                }
                break;
        }
        return true;
    }

    public void setOnTouchIndexBarListener(OnTouchIndexBarListener onTouchIndexBarListener) {
        mOnTouchIndexBarListener = onTouchIndexBarListener;
    }

    private OnTouchIndexBarListener mOnTouchIndexBarListener;

    public interface OnTouchIndexBarListener {
        /**
         * 当手指选中1个字母时，在一个字母的区间范围内，只调用1次，提高效率
         * （例如：文本内容、文本圈颜色在自己范围内不要变化，对应的列表moveToPosition方法只调用一次）
         */
        default void onTouchIndexBar(int index, String indexName) {
        }

        /**
         * 当手指触摸indexBar时，一直不停地回调此方法，主要是为了让指示文本圈随着手指一直移动
         */
        default void onTouchIndexBarAllTheTime(int index, float y) {
        }

        /**
         * 当手指松开时，回调此方法，用于隐藏指示文本，indexBar回复透明状态
         */
        default void onTouchActionUp() {
        }
    }
}