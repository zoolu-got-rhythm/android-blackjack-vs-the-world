package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.example.slurp.blackjackandroid.R;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SpeechScrollerView extends View{

    private Handler mHandler;
    private ArrayList<CustomPoint> currentPlot, wigglePlot;
    private String textToDisplay;
    private float randomTextWiggleOffset;
    private Timer wiggleTimer;


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
        drawFont(canvas);
    }

    private void drawAsLines(Canvas canvas){
        if(currentPlot != null){
            final Paint speechBoxOutline = new Paint();
            speechBoxOutline.setStrokeWidth(11f);
            speechBoxOutline.setColor(Color.MAGENTA);


            for(int i = 1; i < this.wigglePlot.size(); i++){

                canvas.drawLine(this.wigglePlot.get(i - 1).getX(),
                        this.wigglePlot.get(i - 1).getY(),
                        this.wigglePlot.get(i).getX(),
                        this.wigglePlot.get(i).getY()
                        , speechBoxOutline);
            }

            canvas.drawLine(this.wigglePlot.get(this.wigglePlot.size() - 1).getX(),
                    this.wigglePlot.get(this.wigglePlot.size() - 1).getY(),
                    this.wigglePlot.get(0).getX(),
                    this.wigglePlot.get(0).getY()
                    , speechBoxOutline);
        }
    }

    private void drawFont(Canvas canvas){

        // put in constructor
        AssetManager am = getContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am,  "fonts/Dokdo-Regular.ttf");
        if(textToDisplay != null){
            Paint paint = new Paint();
            paint.setTypeface(custom_font);
            paint.setTextSize(70);// change to class member
            paint.setColor(Color.MAGENTA);
            canvas.drawText(this.textToDisplay, (60 - 8) + this.randomTextWiggleOffset,
                    85 + this.randomTextWiggleOffset,
                    paint);
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

    // uses Paint object and Rect to get innerWidth and innerHeight of text/typeface based on size
    private HashMap<String, Integer> inferTextSizeOfFontAndMap(Typeface typeface,
                                                               float textSize, String text){
        Paint mTextPaint = new Paint();
        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextSize(textSize);

        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);

        HashMap<String, Integer> mapOfTextWidthAndHeight = new HashMap<>();
        mapOfTextWidthAndHeight.put("width", bounds.width());
        mapOfTextWidthAndHeight.put("height", bounds.height());

        return mapOfTextWidthAndHeight;
    }

    public void drawDialogueBox(String textToDisplay){

        this.textToDisplay = textToDisplay;

        AssetManager am = getContext().getAssets();
        Typeface customFont = Typeface.createFromAsset(am,  "fonts/Dokdo-Regular.ttf");

        HashMap<String, Integer> widthAndHeightOfText =
                this.inferTextSizeOfFontAndMap(customFont, 70, textToDisplay);

        int marginAndBorderRadius = 25;

        this.currentPlot = new SpeechBubblePlotManager().plotSpeechBubble(
                new CustomPoint(60, 20),
                widthAndHeightOfText.get("width") + (marginAndBorderRadius * 2),
                100,
                marginAndBorderRadius,
                8,
                22,
                5
        );

        if(this.wiggleTimer == null){
            this.wiggleTimer = new Timer();
            this.wiggleTimer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {

                    wigglePlot = new SpeechBubblePlotManager().copyPlotArrAndWiggleByRange(currentPlot, 4);
                    randomTextWiggleOffset = new SpeechBubblePlotManager().generateRandomNegOrPosNumberInRangeX(4);

                    mHandler.postAtFrontOfQueue(new Runnable() {
                        @Override
                        public void run() {
                            postInvalidate();

                        }
                    });
                }
            }, 0, 150);
        }


    }
}
