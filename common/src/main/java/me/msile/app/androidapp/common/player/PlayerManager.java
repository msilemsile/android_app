package me.msile.app.androidapp.common.player;

import android.content.Context;
import android.content.Intent;

import me.msile.app.androidapp.common.player.exoplayer.ExoPlayerActivity;
import me.msile.app.androidapp.common.player.mediaplayer.MediaPlayerActivity;

public class PlayerManager {

    public static void goToMediaPlay(Context context, String videoUrl) {
        Intent intent = new Intent(context, MediaPlayerActivity.class);
        intent.putExtra(MediaPlayerActivity.INTENT_VIDEO_URI, videoUrl);
        context.startActivity(intent);
    }

    public static void goToExoPlay(Context context, String videoUrl) {
        Intent intent = new Intent(context, ExoPlayerActivity.class);
        intent.putExtra(ExoPlayerActivity.INTENT_VIDEO_URI, videoUrl);
        context.startActivity(intent);
    }

}
