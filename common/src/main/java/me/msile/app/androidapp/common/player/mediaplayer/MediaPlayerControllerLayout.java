package me.msile.app.androidapp.common.player.mediaplayer;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Formatter;
import java.util.Locale;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.utils.ActivityUtils;

/**
 * 简单视频操作界面
 */

public class MediaPlayerControllerLayout extends FrameLayout {

    private int mCurrentLayout = LAYOUT_SMALL;
    private static final int LAYOUT_FULL = 1;             //全屏布局
    private static final int LAYOUT_SMALL = 0;            //默认布局
    private static final int STATE_UNLOCK = 0;
    private static final int STATE_LOCK = 1;

    public static final int DEFAULT_FAST_FORWARD_MS = 10000;
    public static final int DEFAULT_REWIND_MS = 10000;
    private MediaController.MediaPlayerControl mPlayer;
    private PlayerControlListener mPlayerControlListener;
    private static final int sDefaultTimeout = 4000;

    private View mCenterStateLay;                           //中间状态布局
    private View mBottomControlLay;                         //底部控制布局
    private ImageView mCenterStateIv;                       //中间状态
    private ProgressBar mLoadingPb;                         //loading
    private ImageView mStartPauseIv;                        //暂停/播放
    private ImageView mFullScreenIv;                        //全屏
    private SeekBar mProgressSeekBar;                       //进度条
    private TextView mStartTimeTv;                          //开始时间
    private TextView mEndTimeTv;                            //结束时间
    private TextView mCenterTimeTv;                         //中心视频播放时间
    private View mValueLay;                                 //音量、亮度布局
    private ImageView mValueIv;                             //音量、亮度图标
    private ProgressBar mValuePb;                           //音量、亮度大小
    private ImageView ivLockState;                          //布局锁
    private View mRenderOverView;                           //遮盖层

    private View mBottomControlFlOverLay;
    private ImageView mPreIv;
    private ImageView mRewindIv;
    private ImageView mForwardIv;
    private ImageView mNextIv;

    private boolean mIsShowing;                             //是否正在展示
    private boolean mDragging;                              //是否正在拖动进度条
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private boolean scrollValue;
    private float touchValueSize;
    private float currentValueDis;

    private boolean enableFullScreen = true;                //是否可以全屏
    private int lockState = STATE_UNLOCK;

    public MediaPlayerControllerLayout(Context context) {
        super(context);
        init(context);
    }

    public MediaPlayerControllerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MediaPlayerControllerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.lay_mediaplayer_controller, this);
        mRenderOverView = findViewById(R.id.render_over_view);
        mCenterStateLay = findViewById(R.id.center_state_fl);
        mBottomControlLay = findViewById(R.id.bottom_control_ll);
        mBottomControlFlOverLay = findViewById(R.id.bottom_control_fl_over);
        mPreIv = findViewById(R.id.pre_iv);
        mPreIv.setOnClickListener(itemClickListener);
        mRewindIv = findViewById(R.id.rewind_iv);
        mRewindIv.setOnClickListener(itemClickListener);
        mForwardIv = findViewById(R.id.forward_iv);
        mForwardIv.setOnClickListener(itemClickListener);
        mNextIv = findViewById(R.id.next_iv);
        mNextIv.setOnClickListener(itemClickListener);
        mLoadingPb = (ProgressBar) findViewById(R.id.loading_pb);
        mCenterStateIv = (ImageView) findViewById(R.id.center_state_iv);
        mCenterStateIv.setOnClickListener(itemClickListener);
        mStartPauseIv = (ImageView) findViewById(R.id.start_pause_iv);
        mStartPauseIv.setOnClickListener(itemClickListener);
        mFullScreenIv = (ImageView) findViewById(R.id.full_screen_iv);
        mFullScreenIv.setOnClickListener(itemClickListener);
        mFullScreenIv.setVisibility(enableFullScreen ? VISIBLE : GONE);
        mProgressSeekBar = (SeekBar) findViewById(R.id.progress_seek_bar);
        mProgressSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        mStartTimeTv = (TextView) findViewById(R.id.start_time_tv);
        mEndTimeTv = (TextView) findViewById(R.id.end_time_tv);
        mCenterTimeTv = (TextView) findViewById(R.id.center_time_tv);
        mValueLay = findViewById(R.id.ll_value);
        mValueIv = findViewById(R.id.iv_value);
        mValuePb = findViewById(R.id.pb_value);
        ivLockState = findViewById(R.id.iv_lock_state);
        ivLockState.setOnClickListener(itemClickListener);
        initFormatter();
        initGesture(context);
    }

    public void setPlayerControlListener(PlayerControlListener mPlayerControlListener) {
        this.mPlayerControlListener = mPlayerControlListener;
    }

    //按钮点击
    private OnClickListener itemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkPlayerIsNull()) {
                return;
            }
            int resId = v.getId();
            if (resId == R.id.start_pause_iv || resId == R.id.center_state_iv) {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                } else {
                    mPlayer.start();
                }
                mediaController.show(sDefaultTimeout);
            } else if (resId == R.id.full_screen_iv) {
                if (mPlayer.isPlaying()) {
                    mediaController.hide();
                }
                Context context = getContext();
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (mCurrentLayout == LAYOUT_SMALL) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        mCurrentLayout = LAYOUT_FULL;
                    } else if (mCurrentLayout == LAYOUT_FULL) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        mCurrentLayout = LAYOUT_SMALL;
                    }
                }
            } else if (resId == R.id.iv_lock_state) {
                changeLockState();
            } else if (resId == R.id.pre_iv) {
                if (mPlayerControlListener != null) {
                    mPlayerControlListener.onPrevious();
                    resetControllerLay();
                }
            } else if (resId == R.id.rewind_iv) {
                long rewindDuration = mPlayer.getDuration();
                if (rewindDuration <= 0) {
                    return;
                }
                long rewindPosition = mPlayer.getCurrentPosition();
                rewindPosition -= DEFAULT_REWIND_MS;
                if (rewindPosition <= 0) {
                    rewindPosition = 0;
                }
                mPlayer.seekTo((int) (rewindPosition));
                mediaController.onPreparing();
            } else if (resId == R.id.forward_iv) {
                long forwardDuration = mPlayer.getDuration();
                if (forwardDuration <= 0) {
                    return;
                }
                long forwardPosition = mPlayer.getCurrentPosition();
                forwardPosition += DEFAULT_FAST_FORWARD_MS;
                if (forwardPosition >= forwardDuration) {
                    forwardPosition = forwardDuration;
                }
                mPlayer.seekTo((int) (forwardPosition));
                mediaController.onPreparing();
            } else if (resId == R.id.next_iv) {
                if (mPlayerControlListener != null) {
                    resetControllerLay();
                    mPlayerControlListener.onNext();
                }
            }
        }
    };

    private void changeLockState() {
        if (lockState == STATE_UNLOCK) {
            lockState(false);
            showLockTimeOutHide();
            lockScreenOrientation();
        } else {
            unLockState();
        }
    }

    public void lockState(boolean force) {
        removeCallbacks(hideLockAction);
        lockState = STATE_LOCK;
        ivLockState.setImageResource(R.mipmap.icon_lock);
        mediaController.hide();
    }

    public void unLockState() {
        removeCallbacks(hideLockAction);
        lockState = STATE_UNLOCK;
        ivLockState.setImageResource(R.mipmap.icon_unlock);
        mediaController.show();
    }

    private void lockScreenOrientation() {
        Activity activity = getAttachedActivity();
        if (activity != null) {
            if (isFullScreen()) {
                ActivityUtils.setScreenOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, true);
            } else {
                ActivityUtils.setScreenOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, true);
            }
        }
    }

    private void unLockScreenOrientation() {
        Activity activity = getAttachedActivity();
        if (activity != null) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }
    }

    private boolean isLockState() {
        return lockState == STATE_LOCK;
    }

    private void showLockTimeOutHide() {
        if (ivLockState.getVisibility() == VISIBLE) {
            removeCallbacks(hideLockAction);
            ivLockState.setVisibility(GONE);
            return;
        }
        ivLockState.setVisibility(VISIBLE);
        postDelayed(hideLockAction, 3000);
    }

    private Runnable hideLockAction = new Runnable() {
        @Override
        public void run() {
            ivLockState.setVisibility(GONE);
        }
    };


    //拖动进度条监听
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (checkPlayerIsNull()) {
                return;
            }
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }
            // changing the progress,but the player not seek
            long duration = mPlayer.getDuration();
            long newPosition = (duration * progress) / 1000L;
            if (mStartTimeTv != null) {
                mStartTimeTv.setText(stringForTime((int) newPosition));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mediaController.show(3600000);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            removeCallbacks(mShowProgress);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //when stop track,the player can seek to the progress
            if (checkPlayerIsNull()) {
                return;
            }
            long duration = mPlayer.getDuration();
            long newPosition = (duration * seekBar.getProgress()) / 1000L;
            mPlayer.seekTo((int) newPosition);
            mediaController.onPreparing();
            if (mStartTimeTv != null) {
                mStartTimeTv.setText(stringForTime((int) newPosition));
            }

            mDragging = false;
            updatePausePlay();

            mediaController.show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            post(mShowProgress);
        }
    };

    //控制回调
    private IMediaController mediaController = new IMediaController() {

        @Override
        public void setMediaPlayer(MediaController.MediaPlayerControl player) {
            mPlayer = player;
        }

        @Override
        public void setEnabled(boolean enabled) {
            if (mStartPauseIv != null) {
                mStartPauseIv.setEnabled(enabled);
            }
            if (mCenterStateLay != null) {
                mCenterStateLay.setEnabled(enabled);
            }
            if (mCenterStateIv != null) {
                mCenterStateIv.setEnabled(enabled);
            }
            if (mProgressSeekBar != null) {
                mProgressSeekBar.setEnabled(enabled);
            }
            if (mFullScreenIv != null) {
                mFullScreenIv.setEnabled(enabled);
            }
            MediaPlayerControllerLayout.super.setEnabled(enabled);
        }

        @Override
        public void onPreparing() {
            if (mLoadingPb != null) {
                mLoadingPb.setVisibility(VISIBLE);
            }
            if (mCenterStateLay != null) {
                mCenterStateLay.setVisibility(GONE);
            }
        }

        @Override
        public void onPrepared() {
            if (mRenderOverView != null) {
                mRenderOverView.setVisibility(INVISIBLE);
            }
            if (mLoadingPb != null) {
                mLoadingPb.setVisibility(GONE);
            }
            if (mCenterStateLay != null) {
                mCenterStateLay.setVisibility(GONE);
            }
            show();
        }

        @Override
        public void onComplete() {
            updateProgress();
            if (mCenterStateLay != null) {
                mCenterStateLay.setVisibility(VISIBLE);
                mCenterStateIv.setImageResource(R.mipmap.icon_retry);
            }
            if (mLoadingPb != null) {
                mLoadingPb.setVisibility(GONE);
            }
        }

        @Override
        public void onError() {
            Toast.makeText(getContext(), "播放出错!", Toast.LENGTH_LONG).show();
            if (mCenterStateLay != null) {
                mCenterStateLay.setVisibility(VISIBLE);
                mCenterStateIv.setImageResource(R.mipmap.icon_retry);
            }
            if (mLoadingPb != null) {
                mLoadingPb.setVisibility(GONE);
            }
        }

        @Override
        public void show(int timeout) {
            if (isLockState()) {
                showLockTimeOutHide();
                return;
            }
            if (!mIsShowing) {
                setControlVisibility(VISIBLE);
                ivLockState.setVisibility(VISIBLE);
                mIsShowing = true;
            }
            updatePausePlay();

            // cause the progress bar to be updated even if mShowing
            // was already true.  This happens, for example, if we're
            // paused with the progress bar showing the user hits play.
            removeCallbacks(mShowProgress);
            post(mShowProgress);

            if (timeout != 0) {
                removeCallbacks(mFadeOut);
                postDelayed(mFadeOut, timeout);
            }
        }

        @Override
        public void show() {
            show(sDefaultTimeout);
        }

        @Override
        public void hide() {
            if (mIsShowing) {
                mIsShowing = false;
                removeCallbacks(mFadeOut);
                removeCallbacks(mShowProgress);
                ivLockState.setVisibility(GONE);
            }
            setControlVisibility(GONE);
        }

        @Override
        public boolean isShowing() {
            return mIsShowing;
        }
    };

    //隐藏控制层
    private Runnable mFadeOut = new Runnable() {
        @Override
        public void run() {
            mediaController.hide();
        }
    };

    //进度条更新
    private Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            int pos = updateProgress();
            if (mPlayer != null && !mDragging && mIsShowing && mPlayer.isPlaying()) {
                postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    public IMediaController getMediaController() {
        return mediaController;
    }

    private void initFormatter() {
        if (mFormatter == null) {
            mFormatBuilder = new StringBuilder();
            mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        }
    }

    //手势
    private GestureDetector mGestureDetector;
    private boolean isMultiPointEvent;
    private boolean isScrollX;
    private boolean isScrollY;
    private boolean scrubbing;
    private int mTouchSeekPro;
    private long mNewSeekPro;

    private void initGesture(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                if (isLockState()) {
                    return true;
                }
                if (isMultiPointEvent) {
                    return true;
                }
                float absDistanceX = Math.abs(distanceX);
                float absDistanceY = Math.abs(distanceY);
                if (isScrollX) {
                    gestureXDistance(-distanceX, false);
                }
                int[] screenPos = getDownScreenPos(e1);
                if (isScrollY) {
                    gestureYDistance(distanceY, false, screenPos);
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
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                isScrollX = false;
                isScrollY = false;
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isLockState()) {
                    showLockTimeOutHide();
                    return true;
                }
                if (checkPlayerIsNull()) {
                    return true;
                }
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                } else {
                    mPlayer.start();
                }
                updatePausePlay();
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (!mediaController.isShowing()) {
                    mediaController.show();
                } else {
                    mediaController.hide();
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (mPlayerControlListener != null) {
                    mPlayerControlListener.onLongClick();
                }
            }
        });
    }

    /**
     * 获取当前触未知(左半边亮度、右半边音量)
     */
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            isMultiPointEvent = false;
            scrubbing = false;
            mTouchSeekPro = 0;
            mNewSeekPro = 0;
            scrollValue = false;
            touchValueSize = 0;
            currentValueDis = 0;
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            isMultiPointEvent = true;
            return true;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (scrubbing) {
                gestureXDistance(0, true);
            }
            if (scrollValue) {
                gestureYDistance(0, true, new int[]{0, 0});
            }
        }
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 更新暂停/播放按钮
     */
    private void updatePausePlay() {
        if (checkPlayerIsNull()) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mStartPauseIv.setImageResource(R.mipmap.icon_pause);
            mCenterStateLay.setVisibility(GONE);
        } else {
            mStartPauseIv.setImageResource(R.mipmap.icon_play);
            mCenterStateLay.setVisibility(VISIBLE);
            mCenterStateIv.setImageResource(R.mipmap.icon_play_s);
        }
    }

    /**
     * 更新进度
     */
    private int updateProgress() {
        if (checkPlayerIsNull() || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgressSeekBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgressSeekBar.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgressSeekBar.setSecondaryProgress(percent * 10);
        }

        if (mEndTimeTv != null)
            mEndTimeTv.setText(stringForTime(duration));

        if (mStartTimeTv != null) {
            mStartTimeTv.setText(stringForTime(position));
        }

        Log.i("PlayerControllerLayout", "=position=" + position + "==duration==" + duration);
        return position;
    }

    /**
     * 手势滑动进度
     */
    private void gestureXDistance(float distanceX, boolean isGestureSeekEnd) {
        if (checkPlayerIsNull()) {
            return;
        }
        if (mCenterTimeTv != null) {
            if (isGestureSeekEnd) {
                mTouchSeekPro = 0;
                long currentPos = mPlayer.getCurrentPosition();
                if (Math.abs(currentPos - mNewSeekPro) > 1000L) {
                    mPlayer.seekTo((int) mNewSeekPro);
                    mediaController.onPreparing();
                }
                mCenterTimeTv.setVisibility(GONE);
                return;
            }
            final int width = isFullScreen() ? mProgressSeekBar.getWidth() * 4 : mProgressSeekBar.getWidth() * 2;
            long duration = mPlayer.getDuration();
            if (width <= 0 || duration <= 0) {
                return;
            }
            if (mTouchSeekPro == 0) {
                long position = mPlayer.getCurrentPosition();
                mTouchSeekPro = (int) (position * 1.0f / duration * width);
            }
            mTouchSeekPro += distanceX;
            mNewSeekPro = (long) (mTouchSeekPro * 1.0f / width * duration);
            if (mNewSeekPro <= 0) {
                mNewSeekPro = 0;
            }
            if (mNewSeekPro >= duration) {
                mNewSeekPro = duration;
            }
            String durationStr = stringForTime((int) duration);
            String timeStr = stringForTime((int) mNewSeekPro);
            mCenterTimeTv.setText(timeStr + " / " + durationStr);
            mCenterTimeTv.setVisibility(VISIBLE);
            scrubbing = true;
        }
    }


    private void gestureYDistance(float yDis, boolean isCancel, int[] downScreenPos) {
        int height = getHeight();
        if (height <= 0) {
            return;
        }
        if (isCancel) {
            mValueLay.setVisibility(GONE);
            return;
        }
        if (downScreenPos != null) {
            int LR = downScreenPos[0];
            if (!isFullScreen()) {
                yDis = yDis * 3;
            }
            if (LR == 1) {
                //音量
                mValueLay.setVisibility(VISIBLE);
                mValueIv.setImageResource(R.mipmap.icon_volume);
                AudioManager audioManager = getAudioManager();
                float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 1.0f;
                if (!scrollValue) {
                    scrollValue = true;
                    float volumeSize = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    mValuePb.setProgress((int) (volumeSize / maxVolume * 100));
                    currentValueDis = volumeSize / maxVolume * height;
                }
                currentValueDis += yDis;
                float proPercent = currentValueDis / height;
                touchValueSize = proPercent * maxVolume;
                if (touchValueSize <= 0) {
                    touchValueSize = 0;
                }
                if (touchValueSize >= maxVolume) {
                    touchValueSize = maxVolume;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) touchValueSize, 0);
                mValuePb.setProgress((int) (proPercent * 100));
            } else if (LR == -1) {
                //亮度
                mValueLay.setVisibility(VISIBLE);
                mValueIv.setImageResource(R.mipmap.icon_brightness);
                Activity activity = getAttachedActivity();
                if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                    return;
                }
                Window window = activity.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                if (!scrollValue) {
                    scrollValue = true;
                    float brightnessSize = lp.screenBrightness;
                    if (brightnessSize < 0) {
                        ContentResolver contentResolver = activity.getContentResolver();
                        int currBrightness = Settings.System.getInt(contentResolver,
                                Settings.System.SCREEN_BRIGHTNESS, 125);
                        brightnessSize = currBrightness * 1.0f / 255;
                    }
                    mValuePb.setProgress((int) (brightnessSize * 100));
                    currentValueDis = brightnessSize * height;
                }
                currentValueDis += yDis;
                float proPercent = currentValueDis / height;
                touchValueSize = proPercent;
                if (touchValueSize <= 0) {
                    touchValueSize = 0;
                }
                if (touchValueSize >= 1) {
                    touchValueSize = 1;
                }
                lp.screenBrightness = touchValueSize;
                window.setAttributes(lp);
                mValuePb.setProgress((int) (proPercent * 100));
            }
        }
    }

    private Activity getAttachedActivity() {
        Context context = getContext();
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    private AudioManager getAudioManager() {
        return (AudioManager) getContext().getSystemService(Service.AUDIO_SERVICE);
    }

    //格式化时间
    private String stringForTime(int timeMs) {
        initFormatter();
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 检查播放器是否为null
     */
    private boolean checkPlayerIsNull() {
        return mPlayer == null || mStartPauseIv == null;
    }

    /**
     * 屏幕发生变化时
     */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig != null) {
            boolean isFullScreen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
            updateFullScreen(isFullScreen);
            Context context = getContext();
            if (context instanceof Activity) {
                Window window = ((Activity) context).getWindow();
                if (window != null) {
                    if (isFullScreen) {
                        mCurrentLayout = LAYOUT_FULL;
                        WindowManager.LayoutParams params = window.getAttributes();
                        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                        window.setAttributes(params);
                        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    } else {
                        mCurrentLayout = LAYOUT_SMALL;
                        WindowManager.LayoutParams params = window.getAttributes();
                        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        window.setAttributes(params);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    }
                }
            }
            if (lockState != STATE_LOCK) {
                if (isFullScreen) {
                    mediaController.hide();
                } else {
                    mediaController.show();
                }
            }
        }
    }

    /**
     * 设置支持横向全屏模式
     *
     * @param enableFullScreen 横向全屏
     */
    public void setEnableFullScreen(boolean enableFullScreen) {
        this.enableFullScreen = enableFullScreen;
        if (mFullScreenIv != null) {
            mFullScreenIv.setVisibility(enableFullScreen ? VISIBLE : GONE);
        }
    }

    /**
     * 是否是全屏
     */
    public boolean isFullScreen() {
        return mCurrentLayout == LAYOUT_FULL;
    }

    public void updateFullScreen(boolean isFullScreen) {
        if (mFullScreenIv != null) {
            mFullScreenIv.setImageResource(isFullScreen ? R.mipmap.icon_shrink_screen : R.mipmap.icon_full_screen);
        }
    }

    //对外
    public void pause() {
        if (checkPlayerIsNull()) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mStartPauseIv.setImageResource(R.mipmap.icon_play);
            mCenterStateLay.setVisibility(VISIBLE);
            mCenterStateIv.setImageResource(R.mipmap.icon_play_s);
        }
    }

    //对外
    public void resume() {
        if (checkPlayerIsNull()) {
            return;
        }
        if (!mPlayer.isPlaying()) {
            mPlayer.start();
            mStartPauseIv.setImageResource(R.mipmap.icon_pause);
            mCenterStateLay.setVisibility(GONE);
        }
    }

    private void setControlVisibility(int visibility) {
        mBottomControlLay.setVisibility(visibility);
        mBottomControlFlOverLay.setVisibility(visibility);
        if (mPlayerControlListener != null) {
            mPlayerControlListener.onVisibilityChange(visibility);
        }
    }

    /**
     * Listener to be notified about changes of the visibility of the UI control.
     */
    public interface PlayerControlListener {

        /**
         * Called when the visibility changes.
         *
         * @param visibility The new visibility. Either {@link View#VISIBLE} or {@link View#GONE}.
         */
        void onVisibilityChange(int visibility);

        /**
         * Called long click.
         */
        void onLongClick();

        /**
         * Called pre/next media
         */
        void onPrevious();

        void onNext();
    }

    private void resetControllerLay() {
        mRenderOverView.setVisibility(VISIBLE);
        mProgressSeekBar.setProgress(0);
        mProgressSeekBar.setSecondaryProgress(0);
    }

}
