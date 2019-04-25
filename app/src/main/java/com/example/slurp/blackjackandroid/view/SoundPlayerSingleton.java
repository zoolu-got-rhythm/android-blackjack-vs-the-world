package com.example.slurp.blackjackandroid.view;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.example.slurp.blackjackandroid.R;

import java.io.IOException;

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

    public void play(AssetFileDescriptor afd){
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
