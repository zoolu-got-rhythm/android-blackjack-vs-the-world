package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class SpeechScrollerView extends View{
    public SpeechScrollerView(Context context) {
        super(context);

        LinearLayout.LayoutParams hintTextViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        hintTextViewParams.weight = 1;
        this.setLayoutParams(hintTextViewParams);
    }
}
