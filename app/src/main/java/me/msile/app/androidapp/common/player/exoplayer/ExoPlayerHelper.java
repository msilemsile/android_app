package me.msile.app.androidapp.common.player.exoplayer;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import me.msile.app.androidapp.common.core.ActivityWeakRefHolder;

public class ExoPlayerHelper extends ActivityWeakRefHolder {

    private ExoPlayer mPlayer;
    private boolean needListenLifecycle;

    public ExoPlayerHelper(@NonNull Activity activity) {
        super(activity);
        mPlayer = new ExoPlayer.Builder(activity).build();
    }

    public ExoPlayer getPlayer() {
        return mPlayer;
    }

    public void setNeedListenLifecycle(boolean needListenLifecycle) {
        this.needListenLifecycle = needListenLifecycle;
    }

    public void play(Uri videoUri) {
        runUIThreadWithCheck(new Runnable() {
            @Override
            public void run() {
                if (mPlayer != null && videoUri != null) {
                    MediaItem mediaItem = new MediaItem.Builder()
                            .setUri(videoUri)
                            .build();
                    mPlayer.setMediaItem(mediaItem);
                    mPlayer.prepare();
                    mPlayer.play();
                }
            }
        });
    }

    public void enableVoice() {
        if (mPlayer != null) {
            mPlayer.setVolume(1f);
        }
    }

    public void disableVoice() {
        if (mPlayer != null) {
            mPlayer.setVolume(0f);
        }
    }

    public boolean isEnableVoice() {
        if (mPlayer != null) {
            return mPlayer.getVolume() != 0;
        }
        return false;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_PAUSE) {
            if (needListenLifecycle) {
                if (mPlayer != null) {
                    mPlayer.pause();
                }
            }
        } else if (event == Lifecycle.Event.ON_RESUME) {
            if (needListenLifecycle) {
                if (mPlayer != null) {
                    mPlayer.play();
                }
            }
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            clear();
        }
    }

    @Override
    public void onClear() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
