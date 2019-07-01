package com.example.slurp.blackjackandroid.view.customFetchingScoresView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RippleWhenTouchesCanvas extends View {
    private List<SimulatedTapVisual> tapsOnScreenBuffer;
    private Paint mPaint;
    private Boolean isRendering;

    public RippleWhenTouchesCanvas(Context context, int width, int height) {
        super(context);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width,
                height);

        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(Color.DKGRAY);
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.GREEN);
        this.mPaint.setStrokeWidth(16f);
        this.mPaint.setStyle(Paint.Style.STROKE);


        this.tapsOnScreenBuffer = new CopyOnWriteArrayList<>(); // read up on how this works more
        this.isRendering = false;
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        // draw
        for(SimulatedTapVisual tap : this.tapsOnScreenBuffer){
            canvas.drawCircle(tap.getPoint().x, tap.getPoint().y, tap.getCurrentRadius(), this.mPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.render();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isRendering = false;
    }

    // make private?
    public void render(){
        this.isRendering = true;
        new Thread(){
            public void run() {
                while(isRendering) {
//                    Log.d("rendering", "yup");
                    try {
                        checkAndClearBuffer();
                        postInvalidate();
                        Thread.sleep(1000 / 60); // 60 fps
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void checkAndClearBuffer(){
        for(SimulatedTapVisual tap : this.tapsOnScreenBuffer){
            if(tap.getHasFinishedAnimating())
                this.tapsOnScreenBuffer.remove(0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public synchronized void addTap(SimulatedTapVisual tap){
        this.tapsOnScreenBuffer.add(tap);
        tap.animate();
    }
}
