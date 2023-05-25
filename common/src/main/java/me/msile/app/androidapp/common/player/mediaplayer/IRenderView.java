package me.msile.app.androidapp.common.player.mediaplayer;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

public interface IRenderView {
    int AR_ASPECT_FIT_PARENT = 0; // without clip
    int AR_ASPECT_FILL_PARENT = 1; // may clip
    int AR_ASPECT_WRAP_CONTENT = 2;
    int AR_MATCH_PARENT = 3;
    int AR_16_9_FIT_PARENT = 4;
    int AR_4_3_FIT_PARENT = 5;

    View getView();

    boolean shouldWaitForResize();

    void setVideoSize(int videoWidth, int videoHeight);

    void setAspectRatio(int aspectRatio);

    void addRenderCallback(IRenderCallback callback);

    void removeRenderCallback(IRenderCallback callback);

    interface ISurfaceHolder {
        void bindToMediaPlayer(MediaPlayer mp);

        IRenderView getRenderView();

        SurfaceHolder getSurfaceHolder();

        Surface openSurface();

        SurfaceTexture getSurfaceTexture();
    }

    interface IRenderCallback {
        /**
         * @param holder
         * @param width  could be 0
         * @param height could be 0
         */
        void onSurfaceCreated(ISurfaceHolder holder, int width, int height);

        /**
         * @param holder
         * @param format could be 0
         * @param width
         * @param height
         */
        void onSurfaceChanged(ISurfaceHolder holder, int format, int width, int height);

        void onSurfaceDestroyed(ISurfaceHolder holder);
    }
}
