package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.method.TimeKeyListener;
import android.view.Gravity;
import android.widget.TextView;

import com.example.slurp.blackjackandroid.model.blackjack.Game;

import java.util.Timer;
import java.util.TimerTask;

public class StopWatchView extends android.support.v7.widget.AppCompatTextView {
    private android.os.Handler mHandler;
    private Game model;
    final private int INTERVAL_IN_MS = 25;
    public StopWatchView(Context context, final Game model, int width, int height) {
        super(context);
        this.mHandler = new Handler();
        this.model = model;
        this.setWidth(width);
        this.setHeight(height);
        this.setTextSize(18f);
        this.setBackgroundColor(Color.DKGRAY);
        this.setTextColor(Color.CYAN);
        this.setGravity(Gravity.CENTER);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setText(model.getPlayersTime());;
                    }
                });
            }

        }, 0, this.INTERVAL_IN_MS);
    }
}
