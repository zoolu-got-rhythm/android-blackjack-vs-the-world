package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer implements Runnable {

    // looking into playing the sound ascyncrhonously if it doesn't already because it's in this thread,
    // then test the performance
    private MediaPlayer mPlayer;

    public SoundPlayer(final Context context, int resId, float volume){
        this.mPlayer = MediaPlayer.create(
                context, resId);

        this.mPlayer.setVolume(volume, volume);

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override

            public void onPrepared(MediaPlayer mediaPlayer) {
//                Toast.makeText(context, "media player setup",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
                mPlayer = null; // mark null for gb collection?
            }
        });
    }

    @Override
    public void run() {
        this.mPlayer.start();
    }
}