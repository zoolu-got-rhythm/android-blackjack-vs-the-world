package com.example.slurp.blackjackandroid.view.BubbleSpeechView;

import android.content.Context;

// here i went with extention of speechScrollerView class oppose to modification -
// like SOLID priniciples suggest
public class FullyRoundedBubbleView extends DefaultSpeechBubbleView {
    public FullyRoundedBubbleView(Context context) {
        super(context);
        this.setPlotToDrawFullBubbleWithNoTriangle();
        this.setDrawAsLinesOrDots(DrawMode.DOTS);
    }
}
