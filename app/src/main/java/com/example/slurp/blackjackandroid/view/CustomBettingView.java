package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Observable;
import java.util.Observer;

public class CustomBettingView extends LinearLayout implements Observer{

    public CustomBettingView(Context context) {
        super(context);
        this.setOrientation(HORIZONTAL);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);

    }

    @Override
    public void update(Observable observable, Object o) {

    }


    private void createStartButton(){
        final Button startButton = new Button(this.getContext());

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        p.weight = 1;
        startButton.setLayoutParams(p);

        startButton.setText("bet");
        // add event
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setOnClickListener(null); // will this remove listener safely?
                removeView(startButton);
                createPlaceBetButton();
            }
        });
        this.addView(startButton);

    }

    private void createPlaceBetButton(){
        final Button okButton = new Button(this.getContext());

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        p.weight = 2;
        okButton.setLayoutParams(p);

        okButton.setText("ok");
    }

    private void createPlayButton(){

    }

    private void removePreviousChild(){

    }
}
