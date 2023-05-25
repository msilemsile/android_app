package me.msile.app.androidapp.common.player.exoplayer;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.util.Util;

import java.util.Formatter;
import java.util.Locale;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.toast.AppToast;

/**
 * 简单视频操作界面(MINI)
 */

public class ExoPlayerMiniControlView extends FrameLayout {

    public static final int DEFAULT_SHOW_TIMEOUT_MS = 3000;
    /**
     * The default minimum interval between time bar position updates.
     */
    public static final int DEFAULT_TIME_BAR_MIN_UPDATE_INTERVAL_MS = 200;
    /**
     * The maximum interval between time bar position updates.
     */
    private static final int MAX_UPDATE_INTERVAL_MS = 1000;

    private FrameLayout mRootLay;

    private ImageView mCenterStateIv;                       //中间状态
    private TextView mCenterTimeTv;                         //中心视频播放时间
    private View pbBuffering;

    private DefaultTimeBar mProgressSeekBar;                //进度条

    private ExoPlayer mPlayer;

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private ComponentListener mComponentListener;

    private View mValueLay;                                 //音量布局
    private ProgressBar mValuePb;                           //音量大小

    private Runnable updateProgressAction;
    private Runnable hideAction;
    private boolean scrubbing;
    private int showTimeoutMs;
    private int timeBarMinUpdateIntervalMs;
    private long touchSeekProMS;
    private float currentSeekProDis;
    private boolean scrollValue;
    private float touchValueSize;
    private float currentValueDis;

    public ExoPlayerMiniControlView(Context context) {
        this(context, null);
    }

    public ExoPlayerMiniControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExoPlayerMiniControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mComponentListener = new ComponentListener();
        showTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;
        timeBarMinUpdateIntervalMs = DEFAULT_TIME_BAR_MIN_UPDATE_INTERVAL_MS;
        updateProgressAction = this::updateProgress;
        hideAction = this::hide;

        inflate(context, R.layout.lay_exoplayer_mini_control_view, this);
        mRootLay = findViewById(R.id.fl_play_control_root);
        pbBuffering = findViewById(R.id.pb_buffering);
        mCenterStateIv = findViewById(R.id.center_state_iv);
        mProgressSeekBar = findViewById(R.id.progress_seek_bar);
        mCenterTimeTv = findViewById(R.id.center_time_tv);
        mValueLay = findViewById(R.id.ll_value);
        mValuePb = findViewById(R.id.pb_value);
    }

    public void setPlayer(ExoPlayer player) {
        if (mPlayer != null) {
            mPlayer.removeListener(mComponentListener);
        }
        mPlayer = player;
        if (mPlayer != null) {
            mPlayer.addListener(mComponentListener);
            updateAll();
        }
    }

    private void updateAll() {
        mProgressSeekBar.setVisibility(VISIBLE);
        updateBuffering();
        updatePlayPauseButton();
        updateNavigation();
        updateTimeline();
    }

    private void updatePlayPauseButton() {
        if (checkPlayerIsNull()) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mCenterStateIv.setVisibility(GONE);
            mCenterStateIv.setImageResource(R.mipmap.icon_play_s);
        } else {
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
        mProgressSeekBar.setEnabled(false);
    }

    private void updateBuffering() {
        if (checkPlayerIsNull()) {
            return;
        }
        pbBuffering.setVisibility(mPlayer.getPlaybackState() == Player.STATE_BUFFERING ? View.VISIBLE : View.GONE);
    }

    private void updateTimeline() {
        if (checkPlayerIsNull()) {
            return;
        }
        long durationMs = mPlayer.getDuration();
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
    }

    private void updateProgress() {
        if (checkPlayerIsNull()) {
            return;
        }
        long position = mPlayer.getCurrentPosition();
        long bufferedPosition = mPlayer.getBufferedPosition();

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

    public void previous() {
        if (mPlayer.hasPrevious()) {
            mPlayer.previous();
        } else {
            AppToast.toastMsg("到头了!");
        }
    }

    public void next() {
        if (mPlayer.hasNext()) {
            mPlayer.next();
        } else {
            AppToast.toastMsg("到底了!");
        }
    }

    /**
     * 检查播放器是否为null
     */
    private boolean checkPlayerIsNull() {
        return mPlayer == null || mCenterStateIv == null;
    }

    public void show() {
        if (!isControlVisible()) {
            updateAll();
        }
        // Call hideAfterTimeout even if already visible to reset the timeout.
        hideAfterTimeout();
    }

    public void hide() {
        mProgressSeekBar.setVisibility(GONE);
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
    }

    public void showOrHide() {
        if (!isControlVisible()) {
            show();
        } else {
            hide();
        }
    }

    private boolean isControlVisible() {
        return mProgressSeekBar.getVisibility() == VISIBLE;
    }

    private void hideAfterTimeout() {
        removeCallbacks(hideAction);
        postDelayed(hideAction, showTimeoutMs);
    }

    public void scrollXDistance(float xDis, boolean isCancel) {
        int width = getWidth();
        if (width <= 0) {
            return;
        }
        if (isCancel) {
            scrubbing = false;
            mCenterTimeTv.setVisibility(GONE);
            seekToTimeBarPosition(touchSeekProMS);
            return;
        }
        xDis = (-xDis) / 3;
        long duration = mPlayer.getDuration();
        if (!scrubbing) {
            scrubbing = true;
            mCenterTimeTv.setVisibility(VISIBLE);
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
        String timeStr = Util.getStringForTime(mFormatBuilder, mFormatter, touchSeekProMS);
        String durationStr = Util.getStringForTime(mFormatBuilder, mFormatter, duration);
        mCenterTimeTv.setText(timeStr + " / " + durationStr);
    }

    public void scrollYDistance(float yDis, boolean isCancel) {
        int height = getHeight();
        if (height <= 0) {
            return;
        }
        if (isCancel) {
            mValueLay.setVisibility(GONE);
            return;
        }
        //音量
        mValueLay.setVisibility(VISIBLE);
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
    }

    private AudioManager getAudioManager() {
        return (AudioManager) getContext().getSystemService(Service.AUDIO_SERVICE);
    }

    private final class ComponentListener implements Player.Listener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, @Player.State int playbackState) {
            updateBuffering();
            updatePlayPauseButton();
            updateProgress();
            if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
                show();
            }
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            updateProgress();
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
            updateNavigation();
            updateTimeline();
        }

        @Override
        public void onTimelineChanged(Timeline timeline, @Player.TimelineChangeReason int reason) {
            updateNavigation();
            updateTimeline();
        }
    }

    public void playOrPause() {
        int playbackState = mPlayer.getPlaybackState();
        if (playbackState == Player.STATE_BUFFERING) {
            return;
        }
        if (playbackState == Player.STATE_ENDED) {
            seekTo(mPlayer.getCurrentWindowIndex(), C.TIME_UNSET);
        } else if (playbackState == Player.STATE_IDLE) {
            ExoPlaybackException playbackError = mPlayer.getPlayerError();
            if (playbackError != null) {
                mPlayer.prepare();
            }
        } else {
            mPlayer.setPlayWhenReady(!mPlayer.isPlaying());
        }
    }
}
