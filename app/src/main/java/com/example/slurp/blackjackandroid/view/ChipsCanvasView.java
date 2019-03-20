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

public class ChipsCanvasView extends View implements Observer, ViewComponent {
    private int width, height;
    private Game model;
    private String playerName;
    private Handler mHandler;
    private int prevChipsBalance;

    public ChipsCanvasView(Context context, Game model, String playerName, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
        this.model = model;
        this.playerName = playerName;

        try {
            prevChipsBalance =
                    this.model.getPlayerByName(this.playerName).getChips().getCurrentBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        Paint mBackgroundTilePaint = new Paint();
        int squareSizeX = this.width;
        int squareSizeY = 90;
        squareSizeX = squareSizeX / 9;
        squareSizeY = squareSizeY / 3;

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 3; j++){
                if(j % 2 == 0){
                    mBackgroundTilePaint.setColor(i % 2 == 0 ? Color.BLACK: Color.WHITE);
                }else{
                    mBackgroundTilePaint.setColor(i % 2 == 0 ? Color.WHITE : Color.BLACK);
                }
                canvas.drawRect(new Rect(i * squareSizeX,
                        j * (squareSizeY), (i * squareSizeX) + squareSizeX,
                        (j * squareSizeY) + squareSizeY), mBackgroundTilePaint);
            }
        }

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

        int nToSubtractFromRanXoffsetToCreateNegativeAndPositiveNumber = (chipWidth / 4) / 2;

        for(int i = 0; i <= player.getChips().getCurrentBalance(); i++){
            int randomXOffset = (int) Math.round(Math.random() * chipWidth / 4) -
                    nToSubtractFromRanXoffsetToCreateNegativeAndPositiveNumber;
                canvas.drawRect(new Rect((chipWidth / 2) + randomXOffset,
                                this.height - (i * (this.height / 30)),
                        this.width - (chipWidth / 2) + randomXOffset,
                        this.height - (i * (this.height / 30)) + (this.height / 30) - 1), chipPaint);
        }

    }

    @Override
    public void update(Observable observable, Object o) {

        this.model = (Game) observable; // is this needed? shouldn't be?

        if(this.shouldComponentUpdate(this.model)){
            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // runOnUiThread
                    invalidate();
                }
            });
        }
    }

    @Override
    public boolean shouldComponentUpdate(Game newModleState) {

        int newPlayerStateChipsBalance = 0;
        try {
            newPlayerStateChipsBalance =
                    newModleState.getPlayerByName(this.playerName).getChips().getCurrentBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean prevAndNewStateIsDifferent = false;
        if(this.prevChipsBalance != newPlayerStateChipsBalance){
            prevAndNewStateIsDifferent = true;
        }

        // update prevChipsBalanceState as ref to compare against for next call
        this.prevChipsBalance = newPlayerStateChipsBalance;

        return prevAndNewStateIsDifferent;
    }
}
