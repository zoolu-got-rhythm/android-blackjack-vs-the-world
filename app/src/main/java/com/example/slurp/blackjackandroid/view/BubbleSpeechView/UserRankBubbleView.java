package com.example.slurp.blackjackandroid.view.BubbleSpeechView;

import android.content.Context;

public class UserRankBubbleView extends DefaultSpeechBubbleView {
    public UserRankBubbleView(Context context) {
        super(context);
        this.setPlotToDrawFullBubbleWithNoTriangle();
        this.setDrawAsLinesOrDots(DrawMode.DOTS);
        this.setmSpeechBubbleTextSizeDp(1f);
    }
}
