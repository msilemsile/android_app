package me.msile.app.androidapp.common.player.mediaplayer;

import android.widget.MediaController;

public interface IMediaController {

    void setMediaPlayer(MediaController.MediaPlayerControl player);

    void setEnabled(boolean enabled);

    void onPreparing();

    void onPrepared();

    void onComplete();

    void onError();

    void show(int timeout);

    void show();

    void hide();

    boolean isShowing();

}
