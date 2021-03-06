package com.example.slurp.blackjackandroid.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.slurp.blackjackandroid.model.blackjack.Game;
import com.example.slurp.blackjackandroid.model.blackjack.Player;
import com.example.slurp.blackjackandroid.model.playingcards.PlayingCard;

import java.lang.reflect.Field;
import java.util.Observable;
import java.util.Observer;

public class CustomBettingView extends LinearLayout implements Observer{

    private CustomBettingView.OnBettingListener bettingListener;
    private Game model;
    private String playerName;
    private AnimationManager betButtonAnimatorManager, playButtonAnimatorManager, chipsOkButtonAnimatorManager;

    public CustomBettingView(Context context, String playerName) {
        super(context);
        this.playerName = playerName;
        this.setOrientation(HORIZONTAL);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        this.createStartButton();

    }

    public void setOnBettingListener(OnBettingListener bettingListener) {
        this.bettingListener = bettingListener;
    }

    @Override
    public void update(Observable observable, Object o) {
        this.model = (Game) observable;

        if(this.model.isGameOver())
            this.createStartButton();
    }


    private void createStartButton(){
        removeAllViewsInLayout();

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
                createPlaceBetButton();
            }
        });
        this.addView(startButton);

        this.betButtonAnimatorManager = new AnimationManager(getContext(), startButton);
        this.betButtonAnimatorManager.start();

    }

    private void setNumberPickerTextColor(NumberPicker numberPicker, int color) {

        try {
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
        } catch (NoSuchFieldException e) {
            Log.w("setNumberPickerTextColor", e);
        } catch (IllegalAccessException e) {
            Log.w("setNumberPickerTextColor", e);
        } catch (IllegalArgumentException e) {
            Log.w("setNumberPickerTextColor", e);
        }

        // this below block of code makes sure the current selected number is also coloured
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText)
                ((EditText) child).setTextColor(color);
        }
    }

    private void createPlaceBetButton(){
        removeAllViewsInLayout();

        Player user = null;
        try {
            user = this.model.getPlayerByName(this.playerName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final NumberPicker numberPicker = new NumberPicker(this.getContext());

        // sets colour of numberPicker text
        this.setNumberPickerTextColor(numberPicker, Color.GREEN);

        numberPicker.setMinValue(3);
        if(user.getChips().getCurrentBalance() < 3){
            numberPicker.setMaxValue(3);
        }else{
            numberPicker.setMaxValue(user.getChips().getCurrentBalance());
        }
        numberPicker.setScaleX(2f);
        numberPicker.setScaleY(1.5f);
        numberPicker.setValue(3);

        LinearLayout.LayoutParams layoutParamsNumberPicker = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParamsNumberPicker.weight = 1;
        numberPicker.setLayoutParams(layoutParamsNumberPicker);

        this.addView(numberPicker);

//        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
//            @Override
//            public void onScrollStateChange(NumberPicker numberPicker, int i) {
//                // sets colour of numberPicker text
//                final int count = numberPicker.getChildCount();
//                for(int j = 0; j < count; j++){
//                    View child = numberPicker.getChildAt(j);
//                    if(child instanceof EditText)
//                        ((EditText)child).setTextColor(Color.GREEN);
//                }
//                numberPicker.invalidate();
//            }
//        });


        final Button okButton = new Button(this.getContext());

        LinearLayout.LayoutParams layoutParamsOkButton = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParamsOkButton.weight = 1;
        okButton.setLayoutParams(layoutParamsOkButton);

        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                okButton.setOnClickListener(null); // will this remove listener safely?
                // controller set chips
                if(bettingListener != null) // does getRootViewReturn my CustomBettingView class ref?
                    bettingListener.onBetMade(getRootView(), numberPicker.getValue());
                createPlayButton();
            }
        });

        okButton.setText("ok");
        this.addView(okButton);

//        this.chipsOkButtonAnimator = AnimatorHelper.blinkAnimationEffect(okButton);
//        this.chipsOkButtonAnimator.start();

        this.chipsOkButtonAnimatorManager = new AnimationManager(getContext(), okButton);
        this.chipsOkButtonAnimatorManager.start();
    }

    private void createPlayButton(){
        removeAllViewsInLayout();

        final Button playButton = new Button(this.getContext());

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        p.weight = 1;
        playButton.setLayoutParams(p);

        playButton.setText("play");
        // add event
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton.setOnClickListener(null); // will this remove listener safely?
                if (bettingListener != null){
                    bettingListener.onReadyToPlay(getRootView());
                    createGameInProgressTextView();
                }

            }
        });
        this.addView(playButton);

        this.playButtonAnimatorManager = new AnimationManager(getContext(), playButton);
        this.playButtonAnimatorManager.start();
    }

    private void createGameInProgressTextView(){
        removeAllViewsInLayout();
        TextView gameInProgTextView = new TextView(this.getContext());

        LinearLayout.LayoutParams gameInProgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        gameInProgParams.weight = 1;
        gameInProgTextView.setLayoutParams(gameInProgParams);


        try {
            gameInProgTextView.setText("bet is x" + Integer.toString(model.getPlacedBets().get(
                    model.getPlayerByName(this.playerName)
            )));

        } catch (Exception e) {
            e.printStackTrace();
        }

        gameInProgTextView.setTextSize(18f);
        gameInProgTextView.setBackgroundColor(Color.BLACK);
        gameInProgTextView.setTextColor(Color.YELLOW);
        gameInProgTextView.setGravity(Gravity.CENTER);
        this.addView(gameInProgTextView);
    }

    interface OnBettingListener{
        void onBetMade(View bettingView, int chipsValuePlaced);
        void onReadyToPlay(View bettingView);
    }
}
