package com.example.slurp.blackjackandroid.view.customFetchingScoresView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

public class SearchForDevicesTextView extends AppCompatTextView {
    final Handler handler = new Handler();
    private String textToDisplay, textToDisplayWhenScanStopped;
    private String textScrollCurrentState;
    private int i;
    private Timer myTimer;

    public SearchForDevicesTextView(Context context, String textToDisplay, String textToDisplayWhenScanStopped,
                                    int widthOffSet) {
        super(context);
        this.i = 1;
        this.textToDisplay = textToDisplay;
        this.textToDisplayWhenScanStopped = textToDisplayWhenScanStopped;
        this.setTextColor(Color.GREEN);
        this.setBackgroundColor(Color.DKGRAY);
        this.setTextSize(20f);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width - widthOffSet,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);

        this.setPadding(20, 25, 0, 0);
    }

    public void setTextToDisplay(){

    }

    public void startScan(){
        this.i = 1;
        this.textScrollCurrentState = this.textToDisplay.substring(0, this.i);
        this.setText(this.textScrollCurrentState);
        if(this.myTimer != null)
            this.myTimer.cancel();
        this.myTimer = new Timer();
        this.myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
            }
        }, 0, 1000 / 20); // 20fps
    }

    public void stopScan(){
        this.myTimer.cancel();
        handler.post(new Runnable() {
            @Override
            public void run() {
                setText(textToDisplayWhenScanStopped);
                invalidate();
            }
        });
        this.i = 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void UpdateGUI() {
        if (this.i > this.textToDisplay.length()){
            this.myTimer.cancel();
            return;
        }
        this.textScrollCurrentState = this.textToDisplay.substring(0, this.i);
        handler.post(myRunnable);
//        runO
        this.i++;
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            setText(textScrollCurrentState);
            invalidate();
        }
    };
}
