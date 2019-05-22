package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.model.blackjack.Game;
import com.example.slurp.blackjackandroid.model.blackjack.Player;

import java.util.Observable;
import java.util.Observer;

public class MenuHintView extends LinearLayout {
    private Game model;
    private TextView hintTextView;
    private SpeechScrollerView speechScrollerView;

    public MenuHintView(Context context) {
        super(context);
        this.setOrientation(HORIZONTAL);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);


        // am i using the right type of LayoutParams class currently using LinearLayout.LayoutParams
        ImageView faceView = new ImageView(this.getContext());
        faceView.setBackgroundColor(Color.MAGENTA);
        LinearLayout.LayoutParams faceViewParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        faceViewParams.weight = 3;

        faceView.setLayoutParams(faceViewParams);
        faceView.setPadding(12, 12, 12, 12);
        faceView.setImageResource(R.drawable.tips_man);

        this.addView(faceView);
        this.speechScrollerView = new SpeechScrollerView(getContext());
        this.addView(this.speechScrollerView);

        String title = "blackjack vs the world";

        this.speechScrollerView.drawDialogueBox(title);
    }
}