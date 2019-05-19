package com.example.slurp.blackjackandroid.view;

public class XhdipSizes implements ResponsiveSizes{
    @Override
    public float getCardWidthForThisDevice() {
        return 130f;
    }

    @Override
    public float getCardHeightForThisDevice() {
        return 150f;
    }

    @Override
    public float getStartingXPosForSpeechBubblePlot() {
        return 32;
    }

    @Override
    public float getStartingYPosForSpeechBubblePlot() {
        return 22;
    }

    @Override
    public float getSpeechBubbleTextSize() {
        return 30;
    }

    @Override
    public float getSpeechBubbleTriangleWidth() {
        return 12;
    }

    @Override
    public float getSpeechBubbleMargin() {
        return 15;
    }
}
