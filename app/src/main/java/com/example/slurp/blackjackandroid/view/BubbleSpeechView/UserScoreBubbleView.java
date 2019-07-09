package com.example.slurp.blackjackandroid.view.BubbleSpeechView;

import android.content.Context;

public class UserScoreBubbleView extends DefaultSpeechBubbleView {
    public UserScoreBubbleView(Context context) {
        super(context);
        this.setPlotToDrawFullBubbleWithNoTriangle();
        this.setDrawAsLinesOrDots(DrawMode.DOTS);
        this.setmSpeechBubbleTextSizeDp(16f);
        this.setmSpeechBubbleMarginDp(12f);
        this.setmStartingXPosForBubblePlotDp(13f);
        this.setmStartingYPosForBubblePlotDp(8f);

        // override default spaces between points
        this.setmDefaultSpacesBetweenPoints(20);
    }
}
