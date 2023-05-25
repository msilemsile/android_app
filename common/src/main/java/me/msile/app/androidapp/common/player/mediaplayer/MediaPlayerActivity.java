package me.msile.app.androidapp.common.player.mediaplayer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import me.msile.app.androidapp.common.R;
import me.msile.app.androidapp.common.ui.activity.ImmerseActivity;
import me.msile.app.androidapp.common.utils.StatusBarUtils;

/**
 * 简单视频播放
 */

public class MediaPlayerActivity extends ImmerseActivity {

    public static final String INTENT_VIDEO_URI = "video_uri";

    private MediaPlayerVideoView videoView;
    private MediaPlayerControllerLayout controllerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.black));
        StatusBarUtils.setSystemBarMode(this, false);
        setContentView(R.layout.activity_media_player);
        initViews();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String mVideoUri = intent.getStringExtra(INTENT_VIDEO_URI);
            initializePlayer(mVideoUri);
        }
    }

    private void initViews() {
        videoView = findViewById(R.id.player_video_view);
        controllerLayout = findViewById(R.id.player_controller);
        videoView.setMediaController(controllerLayout.getMediaController());
        View titleView = findViewById(R.id.ll_title);
        MediaPlayerControllerLayout.PlayerControlListener playerControlListener = new MediaPlayerControllerLayout.PlayerControlListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                if (visibility == VISIBLE) {
                    titleView.setVisibility(VISIBLE);
                } else {
                    if (controllerLayout.isFullScreen()) {
                        titleView.setVisibility(GONE);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (isInPictureInPictureMode()) {
                            titleView.setVisibility(GONE);
                        }
                    }
                }
            }

            @Override
            public void onLongClick() {
            }

            @Override
            public void onPrevious() {
            }

            @Override
            public void onNext() {

            }
        };
        controllerLayout.setPlayerControlListener(playerControlListener);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playerControlListener.onNext();
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializePlayer(String videoUri) {
        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!isInPictureInPictureMode()) {
                controllerLayout.pause();
            }
        } else {
            controllerLayout.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        controllerLayout.resume();
    }

    @Override
    public void onBackPressed() {
        if (controllerLayout.isFullScreen()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}
