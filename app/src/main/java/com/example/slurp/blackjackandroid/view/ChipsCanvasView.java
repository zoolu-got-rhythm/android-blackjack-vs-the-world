package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.example.slurp.blackjackandroid.model.blackjack.Game;
import com.example.slurp.blackjackandroid.model.blackjack.Player;

import java.util.Observable;
import java.util.Observer;

public class ChipsCanvasView extends View implements Observer {
    private int width, height;
    private Game model;
    private String playerName;
    private Handler mHandler;

    public ChipsCanvasView(Context context, Game model, String playerName, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
        this.model = model;
        this.playerName = playerName;
        this.mHandler = new Handler();

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.width,
                this.height);

        this.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        Paint backgroundColour = new Paint();
        backgroundColour.setColor(Color.GRAY);
        canvas.drawRect(new Rect(0, 0, this.width, this.height), backgroundColour);

        Paint chipPaint = new Paint();
        chipPaint.setColor(Color.YELLOW);
        int chipWidth = this.width / 2;
        int chipHeight = this.height;  // amount to win

        Player player = null;
        try {
            player = this.model.getPlayerByName(this.playerName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i <= player.getChips().getCurrentBalance(); i++){
                canvas.drawRect(new Rect(chipWidth / 2,
                                this.height - (i * (this.height / 30)),
                        this.width - (chipWidth / 2),
                        this.height - (i * (this.height / 30)) + (this.height / 30) - 1), chipPaint);
        }

    }

    @Override
    public void update(Observable observable, Object o) {

        this.model = (Game) observable;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread
                invalidate();
            }
        });
    }
}
