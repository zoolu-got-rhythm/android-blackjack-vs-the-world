package com.example.slurp.blackjackandroid.view;

public class XxhdpiSizes implements ResponsiveSizes{
    @Override
    public float getCardWidthForThisDevice() {
        return 145f;
    }

    @Override
    public float getCardHeightForThisDevice() {
        return 165f;
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
        return 70;
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
