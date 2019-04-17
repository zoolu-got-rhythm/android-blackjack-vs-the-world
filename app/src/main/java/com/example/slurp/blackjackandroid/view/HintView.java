package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.model.blackjack.Game;
import com.example.slurp.blackjackandroid.model.blackjack.Player;

import java.util.Observable;
import java.util.Observer;

public class HintView extends LinearLayout implements Observer{
    private Game model;
    private TextView hintTextView;

    public HintView(Context context) {
        super(context);
        this.setOrientation(HORIZONTAL);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);


        // am i using the right type of LayoutParams class currently using LinearLayout.LayoutParams
        ImageView faceView = new ImageView(this.getContext());
        faceView.setBackgroundColor(Color.DKGRAY);
        LinearLayout.LayoutParams faceViewParams = new LinearLayout.LayoutParams(1200, LayoutParams.MATCH_PARENT);
        faceViewParams.weight = 5;
        faceView.setLayoutParams(faceViewParams);
        faceView.setImageResource(R.drawable.tips_man);
//        Bitmap bitmapOfTipsMan = BitmapFactory.decodeResource(getResources(), R.drawable.tips_man);


//        this.hintTextView = new TextView(this.getContext());
//        this.hintTextView.setText("can you beat me?");
//        LinearLayout.LayoutParams hintTextViewParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        hintTextViewParams.weight = 1;
//        this.hintTextView.setLayoutParams(hintTextViewParams);
//        this.hintTextView.setTextSize(22f);
//        this.hintTextView.setPadding(50, 0,0,0);
//        this.hintTextView.setBackgroundColor(Color.BLACK);
//        this.hintTextView.setTextColor(Color.MAGENTA);
//        this.hintTextView.setGravity(Gravity.CENTER_VERTICAL);

        this.addView(faceView);
//        this.addView(this.hintTextView);
        SpeechScrollerView speechScrollerView = new SpeechScrollerView(getContext());
        this.addView(speechScrollerView);
        speechScrollerView.drawDialogueBox();
    }


    @Override
    public void update(Observable observable, Object o) {
        this.model = (Game) observable;

//        String hint;
//
//        // implement should update
//
//        Player winner = this.model.getWinner();
//        if(winner != null) {
//            if (this.model.isGameOver() && winner.getName().equals("house")) {
//                hint = "Hah! unlucky...";
//                this.hintTextView.setText(hint);
//            }
//
//            if (this.model.isGameOver() && winner.getName().equals("player")) {
//                hint = "heh' good job!";
//                this.hintTextView.setText(hint);
//            }
//        }else{
//            if(this.model.isGameOver()){
//                hint = "tie! interesting...";
//                this.hintTextView.setText(hint);
//            }else{
//                hint = "time is ticking...";
//                this.hintTextView.setText(hint);
//            }
//        }


    }
}
