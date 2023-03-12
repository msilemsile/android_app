package me.msile.app.androidapp.common.ui.widget.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import me.msile.app.androidapp.R;
import me.msile.app.androidapp.common.rx.DefaultDisposeObserver;
import me.msile.app.androidapp.common.rx.RxTransformerUtils;

public class CountDownTextView extends AppCompatTextView {

    private DefaultDisposeObserver<Long> countDownCallback;
    private long totalCountDown = 60;
    private long mLastCountDown = -1;
    private String defaultSendText = "获取验证码";
    private String reSendText;

    public CountDownTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, me.msile.app.androidapp.R.styleable.CountDownTextView);
        defaultSendText = a.getString(R.styleable.CountDownTextView_default_send_text);
        reSendText = a.getString(R.styleable.CountDownTextView_re_send_text);
        totalCountDown = a.getInt(R.styleable.CountDownTextView_total_count_down, 60);
        a.recycle();
        resetCountDown("");
    }

    public void setTotalCountDown(long totalCountDown) {
        this.totalCountDown = totalCountDown;
    }

    public void setDefaultSendText(String defaultSendText) {
        if (defaultSendText != null) {
            this.defaultSendText = defaultSendText;
        }
    }

    public void setReSendText(String reSendText) {
        if (reSendText != null) {
            this.reSendText = reSendText;
        }
    }

    /**
     * 重置倒计时
     */
    private void resetCountDown(String sendText) {
        stopCountDown();
        mLastCountDown = -1;
        if (TextUtils.isEmpty(sendText)) {
            setText(defaultSendText);
        } else {
            setText(sendText);
        }
        setEnabled(true);
    }

    /**
     * 开始倒计时
     */
    public void startCountDown() {
        if (mLastCountDown == -1) {
            mLastCountDown = totalCountDown;
        }
        countDownCallback = new DefaultDisposeObserver<Long>() {
            @Override
            protected void onSuccess(@NonNull Long aLong) {
                if (aLong >= totalCountDown) {
                    resetCountDown("重新获取");
                } else {
                    mLastCountDown = totalCountDown - aLong;
                    if (!TextUtils.isEmpty(reSendText)) {
                        setText(reSendText + "(" + mLastCountDown + "s" + ")");
                    } else {
                        setText(mLastCountDown + "s");
                    }
                }
            }
        };
        Observable.intervalRange(1, totalCountDown, 0, 1, TimeUnit.SECONDS)
                .compose(RxTransformerUtils.mainSchedulers())
                .subscribe(countDownCallback);
        setEnabled(false);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopCountDown();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mLastCountDown != -1) {
            totalCountDown = mLastCountDown;
            startCountDown();
        }
    }

    public boolean isRunningCountDown() {
        return countDownCallback != null;
    }

    public void stopCountDown() {
        if (countDownCallback != null) {
            countDownCallback.dispose();
            countDownCallback = null;
        }
    }

}
