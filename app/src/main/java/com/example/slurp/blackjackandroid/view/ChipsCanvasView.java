package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

public class ChipsCanvasView extends View implements Observer {
    private int width, height;

    public ChipsCanvasView(Context context, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.width,
                this.height);

        this.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        Paint backgroundColour = new Paint();
        backgroundColour.setColor(Color.DKGRAY);
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
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
