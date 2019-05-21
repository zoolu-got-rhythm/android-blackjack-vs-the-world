package com.example.slurp.blackjackandroid.view;

// concrete product
public class HdpiSizes implements ResponsiveSizes {
    @Override
    public float getCardWidthForThisDevice() {
        return 85f;
    }

    @Override
    public float getCardHeightForThisDevice() {
        return 100f;
    }

    @Override
    public float getStartingXPosForSpeechBubblePlot() {
        return 32;
    }

    @Override
    public float getStartingYPosForSpeechBubblePlot() {
        return 13;
    }

    @Override
    public float getSpeechBubbleTextSize() {
        return 19;
    }

    @Override
    public float getSpeechBubbleTriangleWidth() {
        return 12;
    }

    @Override
    public float getSpeechBubbleMargin() {
        return 15;
    }

    @Override
    public float getSpeechBubbleLineThickness() {
        return 8;
    }
}
