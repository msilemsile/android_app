package me.msile.app.androidapp.common.player.exoplayer;

import static com.google.android.exoplayer2.Player.COMMAND_SET_VIDEO_SURFACE;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.video.VideoSize;

import me.msile.app.androidapp.common.R;

public class ExoPlayerView extends FrameLayout {

    private static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    private static final int SURFACE_TYPE_TEXTURE_VIEW = 2;

    private View surfaceView;
    private AspectRatioFrameLayout contentFrame;

    private ExoPlayer player;

    private ComponentListener componentListener;
    private int textureViewRotation;

    private PlayViewListener playViewListener;

    private int mResizeMode;

    public ExoPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public ExoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int surfaceType = SURFACE_TYPE_SURFACE_VIEW;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AppExoPlayerView, 0, 0);
            try {
                surfaceType = a.getInt(R.styleable.AppExoPlayerView_app_exo_surface_type, surfaceType);
                mResizeMode = a.getInt(R.styleable.AppExoPlayerView_app_exo_resize_mode, AspectRatioFrameLayout.RESIZE_MODE_FIT);
            } finally {
                a.recycle();
            }
        }

        componentListener = new ComponentListener();

        inflate(context, R.layout.merge_exo_player_view, this);
        contentFrame = findViewById(R.id.fl_content_frame);
        contentFrame.setResizeMode(mResizeMode);

        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (surfaceType == SURFACE_TYPE_TEXTURE_VIEW) {
            surfaceView = new TextureView(context);
        } else {
            surfaceView = new SurfaceView(context);
        }
        surfaceView.setLayoutParams(params);
        contentFrame.addView(surfaceView, 0);
    }

    public void setResizeMode(int mResizeMode) {
        this.mResizeMode = mResizeMode;
        if (contentFrame != null) {
            contentFrame.setResizeMode(mResizeMode);
        }
    }

    public void setPlayViewListener(PlayViewListener playViewListener) {
        this.playViewListener = playViewListener;
    }

    public void setPlayer(ExoPlayer player) {
        if (this.player == player) {
            return;
        }
        @Nullable Player oldPlayer = this.player;
        if (oldPlayer != null) {
            oldPlayer.removeListener(componentListener);
            if (oldPlayer.isCommandAvailable(COMMAND_SET_VIDEO_SURFACE)) {
                if (surfaceView instanceof TextureView) {
                    oldPlayer.clearVideoTextureView((TextureView) surfaceView);
                } else if (surfaceView instanceof SurfaceView) {
                    oldPlayer.clearVideoSurfaceView((SurfaceView) surfaceView);
                }
            }
        }
        this.player = player;
        if (player != null) {
            if (player.isCommandAvailable(COMMAND_SET_VIDEO_SURFACE)) {
                if (surfaceView instanceof TextureView) {
                    player.setVideoTextureView((TextureView) surfaceView);
                } else if (surfaceView instanceof SurfaceView) {
                    player.setVideoSurfaceView((SurfaceView) surfaceView);
                }
                updateAspectRatio();
            }
            player.addListener(componentListener);
        }
    }

    private void updateAspectRatio() {
        VideoSize videoSize = player != null ? player.getVideoSize() : VideoSize.UNKNOWN;
        int width = videoSize.width;
        int height = videoSize.height;
        int unappliedRotationDegrees = videoSize.unappliedRotationDegrees;
        float videoAspectRatio =
                (height == 0 || width == 0) ? 0 : (width * videoSize.pixelWidthHeightRatio) / height;

        if (surfaceView instanceof TextureView) {
            // Try to apply rotation transformation when our surface is a TextureView.
            if (videoAspectRatio > 0
                    && (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270)) {
                // We will apply a rotation 90/270 degree to the output texture of the TextureView.
                // In this case, the output video's width and height will be swapped.
                videoAspectRatio = 1 / videoAspectRatio;
            }
            if (textureViewRotation != 0) {
                surfaceView.removeOnLayoutChangeListener(componentListener);
            }
            textureViewRotation = unappliedRotationDegrees;
            if (textureViewRotation != 0) {
                // The texture view's dimensions might be changed after layout step.
                // So add an OnLayoutChangeListener to apply rotation after layout step.
                surfaceView.addOnLayoutChangeListener(componentListener);
            }
            applyTextureViewRotation((TextureView) surfaceView, textureViewRotation);
        }

        onContentAspectRatioChanged(
                contentFrame, videoAspectRatio);
    }


    /**
     * Called when there's a change in the desired aspect ratio of the content frame. The default
     * implementation sets the aspect ratio of the content frame to the specified value.
     *
     * @param contentFrame The content frame, or {@code null}.
     * @param aspectRatio  The aspect ratio to apply.
     */
    protected void onContentAspectRatioChanged(
            @Nullable AspectRatioFrameLayout contentFrame, float aspectRatio) {
        if (contentFrame != null) {
            contentFrame.setAspectRatio(aspectRatio);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (surfaceView instanceof SurfaceView) {
            // Work around https://github.com/google/ExoPlayer/issues/3160.
            surfaceView.setVisibility(visibility);
        }
    }

    private static void applyTextureViewRotation(TextureView textureView, int textureViewRotation) {
        Matrix transformMatrix = new Matrix();
        float textureViewWidth = textureView.getWidth();
        float textureViewHeight = textureView.getHeight();
        if (textureViewWidth != 0 && textureViewHeight != 0 && textureViewRotation != 0) {
            float pivotX = textureViewWidth / 2;
            float pivotY = textureViewHeight / 2;
            transformMatrix.postRotate(textureViewRotation, pivotX, pivotY);

            // After rotation, scale the rotated texture to fit the TextureView size.
            RectF originalTextureRect = new RectF(0, 0, textureViewWidth, textureViewHeight);
            RectF rotatedTextureRect = new RectF();
            transformMatrix.mapRect(rotatedTextureRect, originalTextureRect);
            transformMatrix.postScale(
                    textureViewWidth / rotatedTextureRect.width(),
                    textureViewHeight / rotatedTextureRect.height(),
                    pivotX,
                    pivotY);
        }
        textureView.setTransform(transformMatrix);
    }

    private final class ComponentListener
            implements Player.Listener,
            OnLayoutChangeListener {

        // Player.Listener implementation

        @Override
        public void onVideoSizeChanged(@NonNull VideoSize videoSize) {
            updateAspectRatio();
            if (playViewListener != null) {
                playViewListener.onVideoSizeChanged(videoSize);
            }
        }

        @Override
        public void onRenderedFirstFrame() {
            if (playViewListener != null) {
                playViewListener.onRenderedFirstFrame();
            }
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            if (playViewListener != null) {
                playViewListener.onPlayerError();
            }
        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
            if (playViewListener != null) {
                playViewListener.onPlayerStateChanged(playbackState);
            }
        }

        // OnLayoutChangeListener implementation

        @Override
        public void onLayoutChange(
                View view,
                int left,
                int top,
                int right,
                int bottom,
                int oldLeft,
                int oldTop,
                int oldRight,
                int oldBottom) {
            applyTextureViewRotation((TextureView) view, textureViewRotation);
        }
    }


    public interface PlayViewListener {
        default void onVideoSizeChanged(VideoSize videoSize) {
        }

        default void onRenderedFirstFrame() {
        }

        default void onPlayerError() {
        }

        default void onPlayerStateChanged(int playerState) {
        }
    }


}
