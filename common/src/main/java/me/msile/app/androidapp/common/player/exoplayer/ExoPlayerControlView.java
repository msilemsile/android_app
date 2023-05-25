package me.msile.app.androidapp.common.player.exoplayer;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.util.Util;

import java.util.Formatter;
import java.util.Locale;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.toast.AppToast;
import me.msile.app.androidapp.common.ui.widget.common.SimpleGestureView;
import me.msile.app.androidapp.common.utils.ActivityUtils;

/**
 * 简单视频操作界面
 */

public class ExoPlayerControlView extends FrameLayout {

    private static final int STATE_UNLOCK = 0;
    private static final int STATE_LOCK = 1;

    /**
     * The default fast forward increment, in milliseconds.
     */
    public static final int DEFAULT_FAST_FORWARD_MS = 10000;
    /**
     * The default rewind increment, in milliseconds.
     */
    public static final int DEFAULT_REWIND_MS = 10000;
    /**
     * The default show timeout, in milliseconds.
     */
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;
    /**
     * The default minimum interval between time bar position updates.
     */
    public static final int DEFAULT_TIME_BAR_MIN_UPDATE_INTERVAL_MS = 200;
    /**
     * The maximum number of windows that can be shown in a multi-window time bar.
     */
    public static final int MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR = 100;

    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;
    /**
     * The maximum interval between time bar position updates.
     */
    private static final int MAX_UPDATE_INTERVAL_MS = 1000;

    private FrameLayout mRootLay;

    private ImageView mCenterStateIv;                       //中间状态
    private TextView mCenterTimeTv;                         //中心视频播放时间

    private View mBottomControlLay;                         //底部控制布局
    private ImageView mStartPauseIv;                        //暂停/播放
    private ImageView mFullScreenIv;                        //全屏
    private DefaultTimeBar mProgressSeekBar;                //进度条
    private TextView mStartTimeTv;                          //开始时间
    private TextView mEndTimeTv;                            //结束时间
    private SimpleGestureView mGestureView;                 //播放器手势按钮
    private View mRenderOverView;                           //遮盖层
    private View pbBuffering;

    private View mBottomControlFlOverLay;
    private ImageView mPreIv;
    private ImageView mRewindIv;
    private ImageView mForwardIv;
    private ImageView mNextIv;

    private View mValueLay;                                 //音量、亮度布局
    private ImageView mValueIv;                             //音量、亮度图标
    private ProgressBar mValuePb;                           //音量、亮度大小
    private ImageView ivLockState;                          //布局锁

    private ExoPlayer mPlayer;

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private ComponentListener mComponentListener;
    private PlayerControlListener mPlayerControlListener;

    private Timeline.Window window;
    private Runnable updateProgressAction;
    private Runnable hideAction;
    private Runnable hideLockAction;
    private boolean scrubbing;
    private int showTimeoutMs;
    private int timeBarMinUpdateIntervalMs;
    private long touchSeekProMS;
    private float currentSeekProDis;
    private boolean scrollValue;
    private float touchValueSize;
    private float currentValueDis;

    private int lockState = STATE_UNLOCK;

    public ExoPlayerControlView(Context context) {
        this(context, null);
    }

    public ExoPlayerControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExoPlayerControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mComponentListener = new ComponentListener();
        window = new Timeline.Window();
        showTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;
        timeBarMinUpdateIntervalMs = DEFAULT_TIME_BAR_MIN_UPDATE_INTERVAL_MS;
        updateProgressAction = this::updateProgress;
        hideAction = this::hide;
        hideLockAction = this::hideLock;

        inflate(context, R.layout.lay_exoplayer_control_view, this);
        mRootLay = findViewById(R.id.fl_play_control_root);
        mRenderOverView = findViewById(R.id.render_over_view);
        pbBuffering = findViewById(R.id.pb_buffering);
        mGestureView = findViewById(R.id.player_gesture);
        mBottomControlLay = findViewById(R.id.bottom_control_ll);
        mBottomControlFlOverLay = findViewById(R.id.bottom_control_fl_over);
        mPreIv = findViewById(R.id.pre_iv);
        mPreIv.setOnClickListener(mComponentListener);
        mRewindIv = findViewById(R.id.rewind_iv);
        mRewindIv.setOnClickListener(mComponentListener);
        mForwardIv = findViewById(R.id.forward_iv);
        mForwardIv.setOnClickListener(mComponentListener);
        mNextIv = findViewById(R.id.next_iv);
        mNextIv.setOnClickListener(mComponentListener);
        mCenterStateIv = findViewById(R.id.center_state_iv);
        mCenterStateIv.setOnClickListener(mComponentListener);
        mStartPauseIv = findViewById(R.id.start_pause_iv);
        mStartPauseIv.setOnClickListener(mComponentListener);
        mFullScreenIv = findViewById(R.id.full_screen_iv);
        mFullScreenIv.setOnClickListener(mComponentListener);
        mProgressSeekBar = findViewById(R.id.progress_seek_bar);
        mProgressSeekBar.addListener(mComponentListener);
        mStartTimeTv = findViewById(R.id.start_time_tv);
        mEndTimeTv = findViewById(R.id.end_time_tv);
        mCenterTimeTv = findViewById(R.id.center_time_tv);
        mValueLay = findViewById(R.id.ll_value);
        mValueIv = findViewById(R.id.iv_value);
        mValuePb = findViewById(R.id.pb_value);
        ivLockState = findViewById(R.id.iv_lock_state);
        ivLockState.setOnClickListener(mComponentListener);
        initGestureListener();
        boolean isFullScreen = isFullScreen();
        updateFullScreen(isFullScreen);
        ActivityUtils.setActivityFullScreen((Activity) context, isFullScreen);
    }

    /**
     * 屏幕发生变化时
     */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("PlayerControlView", "newConfig.orientation " + newConfig.orientation);
        boolean isFullScreen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        Activity activity = getAttachedActivity();
        if (activity != null) {
            ActivityUtils.setActivityFullScreen(activity, isFullScreen);
            updateFullScreen(isFullScreen);
            if (lockState != STATE_LOCK) {
                if (isFullScreen) {
                    hide(true);
                } else {
                    show();
                }
            }
        }
    }

    public void setPlayer(ExoPlayer player) {
        if (mPlayer != null) {
            mPlayer.removeListener(mComponentListener);
        }
        mPlayer = player;
        if (mPlayer != null) {
            mPlayer.addListener(mComponentListener);
        }
        updateAll();
    }

    private void updateAll() {
        updateBuffering();
        updatePlayPauseButton();
        updateNavigation();
        updateTimeline();
    }

    private void updateErrorMessage() {
        if (checkPlayerIsNull()) {
            return;
        }
        ExoPlaybackException error = mPlayer.getPlayerError();
        if (error != null) {
            String errorMsgType = "unknown";
            int errorType = error.type;
            //@IntDef({TYPE_SOURCE, TYPE_RENDERER, TYPE_UNEXPECTED, TYPE_REMOTE, TYPE_OUT_OF_MEMORY})
            switch (errorType) {
                case ExoPlaybackException.TYPE_SOURCE:
                    errorMsgType = "TYPE_SOURCE";
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    errorMsgType = "TYPE_RENDERER";
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    errorMsgType = "TYPE_UNEXPECTED";
                    break;
                case ExoPlaybackException.TYPE_REMOTE:
                    errorMsgType = "TYPE_REMOTE";
                    break;
            }
            AppToast.toastLongMsg("播放出错: " + errorMsgType);
        }
    }

    private void updateBuffering() {
        if (checkPlayerIsNull()) {
            return;
        }
        pbBuffering.setVisibility(mPlayer.getPlaybackState() == Player.STATE_BUFFERING ? View.VISIBLE : View.GONE);
    }

    private void updatePlayPauseButton() {
        if (checkPlayerIsNull()) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mStartPauseIv.setImageResource(R.mipmap.icon_pause);
            mCenterStateIv.setVisibility(GONE);
            mCenterStateIv.setImageResource(R.mipmap.icon_play_s);
        } else {
            mStartPauseIv.setImageResource(R.mipmap.icon_play);
            int playState = mPlayer.getPlaybackState();
            if (playState == Player.STATE_IDLE) {
                mCenterStateIv.setVisibility(VISIBLE);
                mCenterStateIv.setImageResource(R.mipmap.icon_retry);
            } else if (playState == Player.STATE_BUFFERING) {
                mCenterStateIv.setVisibility(GONE);
            } else {
                mCenterStateIv.setVisibility(VISIBLE);
                mCenterStateIv.setImageResource(R.mipmap.icon_play_s);
            }
        }
    }

    private void updateNavigation() {
        if (checkPlayerIsNull()) {
            return;
        }
        boolean enableSeeking = false;
        boolean enableNext = false;
        Timeline timeline = mPlayer.getCurrentTimeline();
        if (!timeline.isEmpty()) {
            timeline.getWindow(mPlayer.getCurrentMediaItemIndex(), window);
            boolean isSeekAble = window.isSeekable;
            enableSeeking = isSeekAble;
            enableNext = window.isDynamic || mPlayer.hasNext();
        }
        mProgressSeekBar.setEnabled(enableSeeking);
    }

    private void updateTimeline() {
        if (checkPlayerIsNull()) {
            return;
        }
        long durationMs = mPlayer.getDuration();
        mEndTimeTv.setText(Util.getStringForTime(mFormatBuilder, mFormatter, durationMs));
        mProgressSeekBar.setDuration(durationMs);
        updateProgress();
    }

    private void seekToTimeBarPosition(long positionMs) {
        if (checkPlayerIsNull()) {
            return;
        }
        int windowIndex = mPlayer.getCurrentMediaItemIndex();
        seekTo(windowIndex, positionMs);
    }

    public void seekTo(int windowIndex, long positionMs) {
        if (checkPlayerIsNull()) {
            return;
        }
        mPlayer.seekTo(windowIndex, positionMs);
        int playState = mPlayer.getPlaybackState();
        if (playState == Player.STATE_IDLE) {
            playOrPause();
        }
    }

    private void updateProgress() {
        if (checkPlayerIsNull()) {
            return;
        }
        long position = mPlayer.getCurrentPosition();
        long bufferedPosition = mPlayer.getBufferedPosition();

        mStartTimeTv.setText(Util.getStringForTime(mFormatBuilder, mFormatter, position));
        mProgressSeekBar.setPosition(position);
        mProgressSeekBar.setBufferedPosition(bufferedPosition);

        // Cancel any pending updates and schedule a new one if necessary.
        removeCallbacks(updateProgressAction);
        int playbackState = mPlayer.getPlaybackState();
        if (mPlayer.isPlaying()) {
            long mediaTimeDelayMs = mProgressSeekBar.getPreferredUpdateDelay();

            // Limit delay to the start of the next full second to ensure position display is smooth.
            long mediaTimeUntilNextFullSecondMs = 1000 - position % 1000;
            mediaTimeDelayMs = Math.min(mediaTimeDelayMs, mediaTimeUntilNextFullSecondMs);

            // Calculate the delay until the next update in real time, taking playbackSpeed into account.
            float playbackSpeed = mPlayer.getPlaybackParameters().speed;
            long delayMs =
                    playbackSpeed > 0 ? (long) (mediaTimeDelayMs / playbackSpeed) : MAX_UPDATE_INTERVAL_MS;

            // Constrain the delay to avoid too frequent / infrequent updates.
            delayMs = Util.constrainValue(delayMs, timeBarMinUpdateIntervalMs, MAX_UPDATE_INTERVAL_MS);
            postDelayed(updateProgressAction, delayMs);
        } else if (playbackState != Player.STATE_ENDED && playbackState != Player.STATE_IDLE) {
            postDelayed(updateProgressAction, MAX_UPDATE_INTERVAL_MS);
        }
    }

    public void updateFullScreen(boolean isFullScreen) {
        if (mFullScreenIv != null) {
            mFullScreenIv.setImageResource(isFullScreen ? R.mipmap.icon_shrink_screen : R.mipmap.icon_full_screen);
        }
    }

    public void previous() {
        if (mPlayer.hasPrevious()) {
            resetControlLay();
            mPlayer.previous();
            int playState = mPlayer.getPlaybackState();
            if (playState == Player.STATE_IDLE) {
                playOrPause();
            }
        } else {
            AppToast.toastMsg("到头了!");
        }
    }

    public void next() {
        if (mPlayer.hasNext()) {
            resetControlLay();
            mPlayer.next();
            int playState = mPlayer.getPlaybackState();
            if (playState == Player.STATE_IDLE) {
                playOrPause();
            }
        } else {
            AppToast.toastMsg("到底了!");
        }
    }

    public void rewind() {
        if (mPlayer.isCurrentWindowSeekable()) {
            seekToOffset(-DEFAULT_REWIND_MS);
        }
    }

    public void fastForward() {
        if (mPlayer.isCurrentMediaItemSeekable()) {
            seekToOffset(DEFAULT_FAST_FORWARD_MS);
        }
    }

    private void seekToOffset(long offsetMs) {
        long positionMs = mPlayer.getCurrentPosition() + offsetMs;
        long durationMs = mPlayer.getDuration();
        if (durationMs != C.TIME_UNSET) {
            positionMs = Math.min(positionMs, durationMs);
        }
        positionMs = Math.max(positionMs, 0);
        seekTo(mPlayer.getCurrentMediaItemIndex(), positionMs);
    }

    /**
     * 检查播放器是否为null
     */
    private boolean checkPlayerIsNull() {
        return mPlayer == null || mStartPauseIv == null;
    }

    /**
     * 是否是全屏
     */
    public boolean isFullScreen() {
        return !ActivityUtils.isPortraitScreen(getContext());
    }

    public void show() {
        if (isLockState()) {
            showLockTimeOutHide();
            return;
        }
        if (!isControlVisible()) {
            setControlVisibility(VISIBLE, false);
            ivLockState.setVisibility(VISIBLE);
            updateAll();
        }
        // Call hideAfterTimeout even if already visible to reset the timeout.
        hideAfterTimeout();
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean force) {
        setControlVisibility(GONE, force);
        ivLockState.setVisibility(GONE);
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
    }

    private void setControlVisibility(int visibility, boolean force) {
        mBottomControlLay.setVisibility(visibility);
        mBottomControlFlOverLay.setVisibility(visibility);
        if (mPlayerControlListener != null) {
            mPlayerControlListener.onVisibilityChange(visibility, force);
        }
    }

    private boolean isControlVisible() {
        return mBottomControlLay.getVisibility() == VISIBLE;
    }

    private void hideAfterTimeout() {
        removeCallbacks(hideAction);
        postDelayed(hideAction, showTimeoutMs);
    }

    private void scrollYDistance(float yDis, boolean isCancel, int[] downScreenPos) {
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

    private void scrollXDistance(float xDis, boolean isCancel) {
        int width = getWidth();
        if (width <= 0) {
            return;
        }
        if (isCancel) {
            mCenterTimeTv.setVisibility(GONE);
            seekToTimeBarPosition(touchSeekProMS);
            return;
        }
        xDis = (-xDis) / 3;
        long duration = mPlayer.getDuration();
        if (!scrubbing) {
            long position = mPlayer.getCurrentPosition();
            currentSeekProDis = (position * 1.0f / duration) * width;
        }
        currentSeekProDis += xDis;
        touchSeekProMS = (long) ((currentSeekProDis * duration) / width);
        if (touchSeekProMS <= 0) {
            touchSeekProMS = 0;
        }
        if (touchSeekProMS >= duration) {
            touchSeekProMS = duration;
        }
        if (!scrubbing) {
            scrubbing = true;
            mCenterTimeTv.setVisibility(VISIBLE);
        }
        String timeStr = Util.getStringForTime(mFormatBuilder, mFormatter, touchSeekProMS);
        String durationStr = Util.getStringForTime(mFormatBuilder, mFormatter, duration);
        mCenterTimeTv.setText(timeStr + " / " + durationStr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            scrubbing = false;
            currentSeekProDis = 0;
            touchSeekProMS = 0;
            scrollValue = false;
            touchValueSize = 0;
            currentValueDis = 0;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (scrubbing) {
                scrollXDistance(0, true);
            }
            if (scrollValue) {
                scrollYDistance(0, true, new int[]{0, 0});
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initGestureListener() {
        mGestureView.setGestureListener(new SimpleGestureView.OnSimpleGestureListener() {
            @Override
            public void onSingleTap() {
                if (!isControlVisible()) {
                    show();
                } else {
                    hide();
                }
            }

            @Override
            public void onDoubleTap() {
                if (isLockState()) {
                    showLockTimeOutHide();
                    return;
                }
                playOrPause();
            }

            @Override
            public void onScroll(int scrollDirection, float distanceX, float distanceY, int[] downScreenPos) {
                if (isLockState()) {
                    return;
                }
                if (scrollDirection == 1) {
                    scrollXDistance(distanceX, false);
                } else if (scrollDirection == 2) {
                    scrollYDistance(distanceY, false, downScreenPos);
                }
            }

            @Override
            public void onLongPress() {
                if (isLockState()) {
                    return;
                }
                if (mPlayerControlListener != null) {
                    mPlayerControlListener.onLongClick();
                }
            }

            @Override
            public void onScaleEvent(int scaleType, float scaleDiff) {
                if (isLockState()) {
                    return;
                }
                float absScaleDiff = Math.abs(scaleDiff);
                if (absScaleDiff >= 0.1f && scaleType != 0) {
                    playScreenMode();
                }
            }
        });
    }

    public void setPlayControlListener(PlayerControlListener playerControlListener) {
        this.mPlayerControlListener = playerControlListener;
    }

    private final class ComponentListener
            implements Player.Listener, TimeBar.OnScrubListener, OnClickListener {

        @Override
        public void onScrubStart(TimeBar timeBar, long position) {
            scrubbing = true;
            mCenterTimeTv.setVisibility(VISIBLE);
            String timeStr = Util.getStringForTime(mFormatBuilder, mFormatter, position);
            mStartTimeTv.setText(timeStr);
            mCenterTimeTv.setText(timeStr);
        }

        @Override
        public void onScrubMove(TimeBar timeBar, long position) {
            String timeStr = Util.getStringForTime(mFormatBuilder, mFormatter, position);
            mStartTimeTv.setText(timeStr);
            mCenterTimeTv.setText(timeStr);
        }

        @Override
        public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
            scrubbing = false;
            mCenterTimeTv.setVisibility(GONE);
            if (!canceled) {
                seekToTimeBarPosition(position);
            }
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, @Player.State int playbackState) {
            updateBuffering();
            updatePlayPauseButton();
            updateProgress();
            if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
                mRenderOverView.setVisibility(INVISIBLE);
                show();
            }
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            updateErrorMessage();
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            updateProgress();
        }

        @Override
        public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
            updateNavigation();
            updateTimeline();
        }

        @Override
        public void onTimelineChanged(Timeline timeline, @Player.TimelineChangeReason int reason) {
            updateNavigation();
            updateTimeline();
        }

        @Override
        public void onClick(View view) {
            if (checkPlayerIsNull()) {
                return;
            }
            int resId = view.getId();
            if (resId == R.id.start_pause_iv || resId == R.id.center_state_iv) {
                playOrPause();
            } else if (resId == R.id.full_screen_iv) {
                playScreenMode();
            } else if (resId == R.id.iv_lock_state) {
                changeLockState();
            } else if (resId == R.id.pre_iv) {
                previous();
            } else if (resId == R.id.rewind_iv) {
                rewind();
            } else if (resId == R.id.forward_iv) {
                fastForward();
            } else if (resId == R.id.next_iv) {
                next();
            }
        }
    }

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
        hide(force);
    }

    public void unLockState() {
        removeCallbacks(hideLockAction);
        lockState = STATE_UNLOCK;
        ivLockState.setImageResource(R.mipmap.icon_unlock);
        show();
    }

    public void lockScreenOrientation() {
        Activity activity = getAttachedActivity();
        if (activity != null) {
            if (isFullScreen()) {
                ActivityUtils.setScreenOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, true);
            } else {
                ActivityUtils.setScreenOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, true);
            }
        }
    }

    public void unLockScreenOrientation() {
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

    private void hideLock() {
        ivLockState.setVisibility(GONE);
    }

    public void playOrPause() {
        int playbackState = mPlayer.getPlaybackState();
        if (playbackState == Player.STATE_BUFFERING) {
            return;
        }
        if (playbackState == Player.STATE_ENDED) {
            seekTo(mPlayer.getCurrentMediaItemIndex(), C.TIME_UNSET);
        } else if (playbackState == Player.STATE_IDLE) {
            ExoPlaybackException playbackError = mPlayer.getPlayerError();
            if (playbackError != null) {
                mPlayer.prepare();
            }
        } else {
            mPlayer.setPlayWhenReady(!mPlayer.isPlaying());
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

    private void playScreenMode() {
        Activity activity = getAttachedActivity();
        if (activity != null) {
            if (isFullScreen()) {
                ActivityUtils.setScreenOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, true);
            } else {
                ActivityUtils.setScreenOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, true);
            }
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
        void onVisibilityChange(int visibility, boolean force);

        /**
         * Called long click.
         */
        void onLongClick();
    }

    public void resetControlLay() {
        mRenderOverView.setVisibility(VISIBLE);
        mProgressSeekBar.setPosition(0);
        mProgressSeekBar.setBufferedPosition(0);
        mProgressSeekBar.setDuration(0);
    }

}
