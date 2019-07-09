package com.example.slurp.blackjackandroid.view.BubbleSpeechView;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.view.CustomPoint;
import com.example.slurp.blackjackandroid.view.ResponsiveSizes;
import com.example.slurp.blackjackandroid.view.ResponsiveSizesFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DefaultSpeechBubbleView extends View{

    private Handler mHandler;
    private ArrayList<CustomPoint> currentPlot, wigglePlot;
    private String textToDisplay;
    private float randomTextWiggleOffset;
    private Timer wiggleTimer;
    private ResponsiveSizes responsiveSizes;
    final float DEVICE_DENSITY_SCALE = getResources().getDisplayMetrics().density; // dpi  0.75, 1.0, 1.5, 2.0
    private int specifiedSpacesToPlotOnLeftSide = 1;
    private DrawMode drawAsLinesOrDots = DrawMode.LINES;
    private Float mSpeechBubbleTextSizeDp;
    private Float mStartingXPosForBubblePlotDp;
    private Float mStartingYPosForBubblePlotDp;
    private Float mSpeechBubbleMarginDp;


    private int mDefaultSpacesBetweenPoints = 8;

    enum DrawMode {
        LINES,
        DOTS,
        NO_BORDER
    };

    public DefaultSpeechBubbleView(Context context) {
        super(context);
        this.mHandler = new Handler();


        this.responsiveSizes = ResponsiveSizesFactory.getInstance()
                .createResponsiveSizes(DEVICE_DENSITY_SCALE);

        this.mSpeechBubbleTextSizeDp = this.responsiveSizes.getSpeechBubbleTextSize();
        this.mStartingXPosForBubblePlotDp = this.responsiveSizes.getStartingXPosForSpeechBubblePlot();
        this.mStartingYPosForBubblePlotDp = this.responsiveSizes.getStartingYPosForSpeechBubblePlot();
        this.mSpeechBubbleMarginDp = this.responsiveSizes.getSpeechBubbleMargin();


        LinearLayout.LayoutParams hintTextViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        hintTextViewParams.weight = 1;
        this.setLayoutParams(hintTextViewParams);
        this.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onDraw(final android.graphics.Canvas canvas) {
        if(this.drawAsLinesOrDots == DrawMode.LINES)
            drawAsLines(canvas);

        if(this.drawAsLinesOrDots == DrawMode.DOTS)
            drawAsDots(canvas);

        drawFont(canvas);
    }

    public void setmDefaultSpacesBetweenPoints(int mDefaultSpacesBetweenPoints) {
        this.mDefaultSpacesBetweenPoints = mDefaultSpacesBetweenPoints;
    }

    public void setmStartingXPosForBubblePlotDp(Float mStartingXPosForBubblePlotDp) {
        this.mStartingXPosForBubblePlotDp = mStartingXPosForBubblePlotDp;
    }

    public void setmStartingYPosForBubblePlotDp(Float mStartingYPosForBubblePlotDp) {
        this.mStartingYPosForBubblePlotDp = mStartingYPosForBubblePlotDp;
    }

    public void setmSpeechBubbleMarginDp(Float mSpeechBubbleMarginDp) {
        this.mSpeechBubbleMarginDp = mSpeechBubbleMarginDp;
    }

    public void setmSpeechBubbleTextSizeDp(Float mSpeechBubbleTextSizeDp) {
        this.mSpeechBubbleTextSizeDp = mSpeechBubbleTextSizeDp;
    }

    public void setPlotToDrawFullBubbleWithNoTriangle() {
        this.specifiedSpacesToPlotOnLeftSide = 0;
    }

    public void setDrawAsLinesOrDots(DrawMode drawAsLinesOrDots) {
        this.drawAsLinesOrDots = drawAsLinesOrDots;
    }

    private void drawAsLines(Canvas canvas){
        if(currentPlot != null){
            final Paint speechBoxOutline = new Paint();
            speechBoxOutline.setStrokeWidth(
                    this.responsiveSizes.getSpeechBubbleLineThickness() *
                    DEVICE_DENSITY_SCALE);
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

        int marginAndBorderRadius = Math.round(
                this.mSpeechBubbleMarginDp * DEVICE_DENSITY_SCALE
        );

        // put in constructor
        AssetManager am = getContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am,  "fonts/Dokdo-Regular.ttf");
        if(textToDisplay != null){
            Paint paint = new Paint();
            paint.setTypeface(custom_font);
            paint.setTextSize(this.mSpeechBubbleTextSizeDp * DEVICE_DENSITY_SCALE); // change to class member
            paint.setColor(Color.MAGENTA);
            canvas.drawText(this.textToDisplay,
                    ((this.mStartingXPosForBubblePlotDp - 3) * DEVICE_DENSITY_SCALE) -
                            (marginAndBorderRadius * 0.125f) +
                            this.randomTextWiggleOffset,
                    (this.mStartingYPosForBubblePlotDp *
                            DEVICE_DENSITY_SCALE) +
                            ((this.mSpeechBubbleTextSizeDp * DEVICE_DENSITY_SCALE) / 2) +
                            (marginAndBorderRadius * 0.875f) +
                            this.randomTextWiggleOffset,
                    paint);
        }
    }

    private void drawAsDots(Canvas canvas){

        int transPink = ContextCompat.getColor(getContext(), R.color.transparentPink);
        int transPurple = ContextCompat.getColor(getContext(), R.color.transparentPurple);

        int dotSize = 6;
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

    public void drawDialogueBox(String textToDisplay, boolean drawRepeatedly){

        this.textToDisplay = textToDisplay;

        AssetManager am = getContext().getAssets();
        Typeface customFont = Typeface.createFromAsset(am,  "fonts/Dokdo-Regular.ttf");

        HashMap<String, Integer> widthAndHeightOfText =
                this.inferTextSizeOfFontAndMap(customFont,
                        mSpeechBubbleTextSizeDp * DEVICE_DENSITY_SCALE,
                        textToDisplay);

        int marginAndBorderRadius = Math.round(
                this.mSpeechBubbleMarginDp * DEVICE_DENSITY_SCALE
        );

        float xDp = this.mStartingXPosForBubblePlotDp * DEVICE_DENSITY_SCALE;
        float yDp = this.mStartingYPosForBubblePlotDp * DEVICE_DENSITY_SCALE;
//        float speechBoxHeightDp = widthAndHeightOfText.get("height");

        int speechTriangleWidthDp = (Math.round(
                responsiveSizes.getSpeechBubbleTriangleWidth() * DEVICE_DENSITY_SCALE)
        );


        this.currentPlot = new SpeechBubblePlotManager().plotSpeechBubble(
                new CustomPoint(xDp, yDp),
                widthAndHeightOfText.get("width") + (marginAndBorderRadius * 1.75f),
                widthAndHeightOfText.get("height") + (marginAndBorderRadius * 1.75f),
                marginAndBorderRadius,
                mDefaultSpacesBetweenPoints, // 8
                speechTriangleWidthDp, // this param is ignores if 'specifiedSpacesToPlotOnLeftSide' argument is 0
                this.specifiedSpacesToPlotOnLeftSide // if this param is set to 0 the speech triangle isn't drawn and is ignored
        );

        if(this.wiggleTimer == null){

            if(drawRepeatedly){
                this.wiggleTimer = new Timer();
                this.wiggleTimer.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {

                        copyPrevPlotsAndDeviateThemByRange(4);

                        // not sure if this wiggleplot != null check is needed
                        if(wigglePlot != null) // makes sure wiggleplot has been assigned before trying to call methods on it when drawing
                            mHandler.postAtFrontOfQueue(new Runnable() {
                                @Override
                                public void run() {
                                    postInvalidate();

                                }
                            });
                    }
                }, 0, 150);
            }else{
                copyPrevPlotsAndDeviateThemByRange(4);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        postInvalidate();
                    }
                });
            }
        }
    }

    private void copyPrevPlotsAndDeviateThemByRange(int range){
        wigglePlot = new SpeechBubblePlotManager().copyPlotArrAndWiggleByRange(currentPlot, range);
        randomTextWiggleOffset = new SpeechBubblePlotManager().generateRandomNegOrPosNumberInRangeX(range);
    }



    public void stopDrawDialogueBox(){
        if(this.wiggleTimer != null){
            this.wiggleTimer.cancel();
            this.wiggleTimer = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }
}
