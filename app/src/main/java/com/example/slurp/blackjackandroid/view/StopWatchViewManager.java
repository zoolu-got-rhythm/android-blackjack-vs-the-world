package com.example.slurp.blackjackandroid.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.model.blackjack.Game;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class StopWatchViewManager implements Observer{
    private Handler mHandler;
    final private int INTERVAL_IN_MS = 25;

    public StopWatchViewManager(Activity activity, final Game model){
        super();


        this.mHandler = new Handler();

        final TextView currentTimeTextView = activity.findViewById(R.id.currentTime);

        TextView bestTimeTextView = (TextView) activity.findViewById(R.id.bestTime);
        bestTimeTextView.setText("unknown");


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        currentTimeTextView.setText(model.getPlayersTime());;
                    }
                });
            }

        }, 0, this.INTERVAL_IN_MS);
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
