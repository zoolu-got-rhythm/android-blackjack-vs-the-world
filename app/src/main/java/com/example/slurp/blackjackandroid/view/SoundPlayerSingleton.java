package com.example.slurp.blackjackandroid.view;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.example.slurp.blackjackandroid.R;

import java.io.IOException;

// this will become a potential shared resource, should add locks on it?
public class SoundPlayerSingleton {

    private AssetFileDescriptor afd;
    private MediaPlayer mediaPlayer;
    private static SoundPlayerSingleton soundPlayerSingletonInstance;

    private SoundPlayerSingleton(){
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setVolume(0.8f, 0.8f);
        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    public static SoundPlayerSingleton getInstance(){
        if(soundPlayerSingletonInstance == null)
            soundPlayerSingletonInstance = new SoundPlayerSingleton();
        return soundPlayerSingletonInstance;
    }

    // some thread contention (with threads calling this method at sametime) may have been causing this method to cause crash,
    // due to this singleton class state being mutated by different threads simultaneously
    public synchronized void play(AssetFileDescriptor afd){
//        if(this.mediaPlayer.isPlaying())
//            this.mediaPlayer.release();
        this.mediaPlayer.reset();
        try {
            this.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
