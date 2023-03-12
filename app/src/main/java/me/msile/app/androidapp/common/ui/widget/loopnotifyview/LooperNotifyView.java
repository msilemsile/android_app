package me.msile.app.androidapp.common.ui.widget.loopnotifyview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.utils.DensityUtil;

public class LooperNotifyView extends FrameLayout {

    private NotifyTextView mTvFirst;
    private NotifyTextView mTvTwo;

    private LinkedList<NotifyTextView> mNotifyViewList = new LinkedList<>();

    private NotifyTextConfig mTextConfig;
    private ValueAnimator mAnimator;
    private int mAnimDuration;
    private int mPauseDuration;
    private int mCurrentLooperIndex = -1;
    private List<NotifyTextInfo> mTipsList;
    private boolean isLooped;
    private LoopNotifyListener loopNotifyListener;

    public LooperNotifyView(Context context) {
        super(context);
        init(context, null);
    }

    public LooperNotifyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LooperNotifyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mTvFirst = new NotifyTextView(context);
        mTvTwo = new NotifyTextView(context);
        mNotifyViewList.addLast(mTvFirst);
        mNotifyViewList.addLast(mTvTwo);
        addView(mTvFirst, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mTvTwo, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mTextConfig = new NotifyTextConfig();
        mTipsList = new ArrayList<>();
        //title
        Resources resources = getResources();
        Paint titleBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titleBgPaint.setStyle(Paint.Style.STROKE);
        titleBgPaint.setStrokeWidth(DensityUtil.dip2px(1));
        titleBgPaint.setColor(resources.getColor(me.msile.app.androidapp.R.color.color_FE3F53));
        mTextConfig.setTitleBgPaint(titleBgPaint);
        mTextConfig.setTitleContentDistance(DensityUtil.dip2px(5));
        mTextConfig.setTitleBgPaddingLR(DensityUtil.dip2px(3));
        mTextConfig.setTitleBgCornerSize(DensityUtil.dip2px(1));
        TextPaint titleTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LooperNotifyView);
        int tempTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.LooperNotifyView_lnv_title_size, 0);
        if (tempTitleTextSize <= 0) {
            tempTitleTextSize = DensityUtil.sp2px(13);
        }
        titleTextPaint.setTextSize(tempTitleTextSize);
        int tempTitleTextColor = typedArray.getColor(R.styleable.LooperNotifyView_lnv_title_color, 0);
        if (tempTitleTextColor == 0) {
            tempTitleTextColor = getResources().getColor(R.color.black);
        }
        titleTextPaint.setColor(tempTitleTextColor);
        mTextConfig.setTitleTextPaint(titleTextPaint);
        //content
        int tempContentTextSize = typedArray.getDimensionPixelSize(R.styleable.LooperNotifyView_lnv_text_size, 0);
        if (tempContentTextSize <= 0) {
            tempContentTextSize = DensityUtil.sp2px(14);
        }
        mTextConfig.setContentTextSize(tempContentTextSize);
        int tempContentTextColor = typedArray.getColor(R.styleable.LooperNotifyView_lnv_text_color, 0);
        if (tempContentTextColor == 0) {
            tempContentTextColor = getResources().getColor(R.color.black);
        }
        mTextConfig.setContentTextColor(tempContentTextColor);
        int durationTime = typedArray.getInt(R.styleable.LooperNotifyView_lnv_anim_duration, 0);
        if (durationTime > 0) {
            mAnimDuration = durationTime;
        } else {
            mAnimDuration = 1500;
        }
        int pauseTime = typedArray.getInt(R.styleable.LooperNotifyView_lnv_pause_duration, 0);
        if (pauseTime > 0) {
            mPauseDuration = pauseTime;
        } else {
            mPauseDuration = 1500;
        }
        int mMaxLines = typedArray.getInt(R.styleable.LooperNotifyView_lnv_max_lines, 1);
        mTextConfig.setMaxLines(mMaxLines);
        int lineSpace = typedArray.getDimensionPixelSize(R.styleable.LooperNotifyView_lnv_line_space, 0);
        mTextConfig.setLineSpace(lineSpace);
        typedArray.recycle();
        mAnimator = ValueAnimator.ofInt(0, 0);
        mAnimator.setDuration(mAnimDuration);
    }

    public void setLoopNotifyListener(LoopNotifyListener loopNotifyListener) {
        this.loopNotifyListener = loopNotifyListener;
    }

    public void addLooperTips(NotifyTextInfo tips) {
        if (isLooped() || tips == null || TextUtils.isEmpty(tips.getContent())) {
            return;
        }
        mTipsList.add(tips);
    }

    public int getCurrentLooperIndex() {
        if (mTipsList.size() == 1) {
            return 0;
        }
        if (mCurrentLooperIndex >= mTipsList.size()) {
            return 0;
        }
        return mCurrentLooperIndex;
    }

    public void addLooperTips(List<NotifyTextInfo> tipsList) {
        if (tipsList == null) {
            return;
        }
        clearLooperTips();
        mTipsList.addAll(tipsList);
    }

    public void clearLooperTips() {
        stopLooper();
        mTipsList.clear();
    }

    public void startLooper() {
        stopLooper();
        post(initTask);
    }

    public void stopLooper() {
        resetAnimator();
        removeCallbacks(initTask);
        removeCallbacks(looperTask);
        isLooped = false;
    }

    public boolean isLooped() {
        return isLooped;
    }

    private Runnable initTask = new Runnable() {
        @Override
        public void run() {
            int width = getWidth();
            int height = getHeight();
            if (width <= 0 || height <= 0) {
                stopLooper();
                return;
            }
            if (mTipsList.isEmpty()) {
                stopLooper();
                return;
            }
            NotifyTextInfo firstTextInfo;
            NotifyTextInfo twoTextInfo = null;
            if (mCurrentLooperIndex >= 0 && mCurrentLooperIndex < mTipsList.size()) {
                firstTextInfo = mTipsList.get(mCurrentLooperIndex);
                if (mTipsList.size() > 1) {
                    int nextNotifyIndex = mCurrentLooperIndex + 1;
                    if (nextNotifyIndex >= mTipsList.size()) {
                        nextNotifyIndex = 0;
                    }
                    twoTextInfo = mTipsList.get(nextNotifyIndex);
                }
            } else {
                mCurrentLooperIndex = 0;
                firstTextInfo = mTipsList.get(0);
                if (mTipsList.size() > 1) {
                    twoTextInfo = mTipsList.get(1);
                }
            }
            for (int i = 0; i < mNotifyViewList.size(); i++) {
                NotifyTextView notifyTextView = mNotifyViewList.get(i);
                notifyTextView.animate().cancel();
                if (i == 0) {
                    notifyTextView.setTranslationY(0);
                    notifyTextView.setNotifyInfo(firstTextInfo, mTextConfig);
                } else if (i == 1) {
                    notifyTextView.setTranslationY(height);
                    notifyTextView.setNotifyInfo(twoTextInfo, mTextConfig);
                }
            }
            if (firstTextInfo != null && twoTextInfo != null) {
                if (loopNotifyListener != null) {
                    loopNotifyListener.onLoopNext(mCurrentLooperIndex);
                }
                postDelayed(looperTask, mPauseDuration);
            }
        }
    };

    private void refreshLooperNotifyInfo() {
        if (mTipsList.isEmpty()) {
            stopLooper();
            return;
        }
        int nextNotifyIndex = mCurrentLooperIndex + 1;
        if (nextNotifyIndex >= mTipsList.size()) {
            nextNotifyIndex = 0;
        }
        NotifyTextInfo notifyTextInfo = mTipsList.get(nextNotifyIndex);
        NotifyTextView notifyTextView = mNotifyViewList.peekLast();
        if (notifyTextView != null) {
            notifyTextView.setNotifyInfo(notifyTextInfo, mTextConfig);
        }
        postDelayed(looperTask, mPauseDuration);
    }

    private void resetAnimator() {
        mAnimator.removeAllUpdateListeners();
        mAnimator.removeAllListeners();
        mAnimator.end();
    }

    private Runnable looperTask = new Runnable() {
        @Override
        public void run() {
            int width = getWidth();
            int height = getHeight();
            if (width <= 0 || height <= 0) {
                stopLooper();
                return;
            }
            NotifyTextView firstNotifyView = mNotifyViewList.pollFirst();
            NotifyTextView secondNotifyView = mNotifyViewList.pollFirst();
            if (firstNotifyView != null && secondNotifyView != null) {
                mNotifyViewList.addFirst(firstNotifyView);
                mNotifyViewList.addFirst(secondNotifyView);
                firstNotifyView.setTranslationY(0);
                secondNotifyView.setTranslationY(height);
                resetAnimator();
                mAnimator.setIntValues(0, height);
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Object animatedValue = animation.getAnimatedValue();
                        if (animatedValue instanceof Integer) {
                            Integer transY = (Integer) animatedValue;
                            firstNotifyView.setTranslationY(-transY);
                            secondNotifyView.setTranslationY(height - transY);
                        }
                    }
                });
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mCurrentLooperIndex++;
                        if (mCurrentLooperIndex >= mTipsList.size()) {
                            mCurrentLooperIndex = 0;
                        }
                        if (loopNotifyListener != null) {
                            loopNotifyListener.onLoopNext(mCurrentLooperIndex);
                        }
                        refreshLooperNotifyInfo();
                    }
                });
                mAnimator.start();
            }
        }
    };

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == GONE) {
            stopLooper();
        } else if (visibility == VISIBLE) {
            startLooper();
        }
    }

    public interface LoopNotifyListener {
        void onLoopNext(int nextIndex);
    }

}