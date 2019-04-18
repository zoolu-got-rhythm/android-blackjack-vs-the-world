package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.example.slurp.blackjackandroid.R;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SpeechScrollerView extends View{

    private Handler mHandler;
    private ArrayList<CustomPoint> currentPlot;


    public SpeechScrollerView(Context context) {
        super(context);

        this.mHandler = new Handler();

        LinearLayout.LayoutParams hintTextViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        hintTextViewParams.weight = 1;
        this.setLayoutParams(hintTextViewParams);
        this.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onDraw(final android.graphics.Canvas canvas) {
        drawAsLines(canvas);
    }

    private void drawAsLines(Canvas canvas){
        if(currentPlot != null){
            final Paint speechBoxOutline = new Paint();
            speechBoxOutline.setStrokeWidth(11f);
            speechBoxOutline.setColor(Color.MAGENTA);


            for(int i = 1; i < this.currentPlot.size(); i++){

                canvas.drawLine(this.currentPlot.get(i - 1).getX(),
                        this.currentPlot.get(i - 1).getY(),
                        this.currentPlot.get(i).getX(),
                        this.currentPlot.get(i).getY()
                        , speechBoxOutline);
            }

            canvas.drawLine(this.currentPlot.get(this.currentPlot.size() - 1).getX(),
                    this.currentPlot.get(this.currentPlot.size() - 1).getY(),
                    this.currentPlot.get(0).getX(),
                    this.currentPlot.get(0).getY()
                    , speechBoxOutline);
        }
    }

    private void drawAsDots(Canvas canvas){

        int transPink = ContextCompat.getColor(getContext(), R.color.transparentPink);
        int transPurple = ContextCompat.getColor(getContext(), R.color.transparentPurple);


        int dotSize = 4;
        if(currentPlot != null){
            final Paint speechBoxOutline = new Paint();

            for(int i = 0; i < this.currentPlot.size(); i++){
                speechBoxOutline.setColor(i % 2 == 0 ? transPink : transPurple);

                canvas.drawRect(this.currentPlot.get(i).getX() - dotSize,
                        this.currentPlot.get(i).getY() - dotSize,
                        this.currentPlot.get(i).getX() + dotSize,
                        this.currentPlot.get(i).getY() + dotSize
                        , speechBoxOutline);
            }
        }
    }

    public void drawDialogueBox(){

        this.currentPlot = new SpeechBubblePlotManager().plotSpeechBubble(
                new CustomPoint(60, 20),
                300,
                100,
                25,
                8,
                22,
                3
        );

//        new Timer().scheduleAtFixedRate(new TimerTask() {

//            @Override
//            public void run() {
                mHandler.postAtFrontOfQueue(new Runnable() {
                    @Override
                    public void run() {
                        postInvalidate();

                    }
                });
//            }
//        }, 0, 100);
    }
}
