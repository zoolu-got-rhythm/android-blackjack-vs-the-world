package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.controller.Controller;
import com.example.slurp.blackjackandroid.model.blackjack.Game;
import com.example.slurp.blackjackandroid.model.blackjack.Player;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private Game model;
    private Controller controller;
    private Button betButton, drawCardButton, stickButton;
    private BoardCanvasView boardCanvasViewComputer, boardCanvasViewPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String playerName = "player", computerName = "house";

        this.model = new Game(new Player(playerName, 10), new Player(computerName, 100000));
        this.controller = new Controller(this.model);

        this.model.addObserver(this);


        betButton = findViewById(R.id.betBtn);
        betButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.setBetPlaced(3);
                controller.bet();
            }
        });

        drawCardButton = findViewById(R.id.cardBtn);
        drawCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.playerDrawCard();
            }
        });

        stickButton = findViewById(R.id.stayBtn);
        stickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.stay();
            }
        });




        final LinearLayout cardsLayoutParent = findViewById(R.id.cards_view_parent);
        ViewTreeObserver vtoCardsLayoutParent = cardsLayoutParent.getViewTreeObserver();

        vtoCardsLayoutParent.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //remove listener to ensure only one call is made.
                cardsLayoutParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int h = cardsLayoutParent.getHeight();
                int w = cardsLayoutParent.getWidth();

                boardCanvasViewComputer = new BoardCanvasView(model, getApplicationContext(), playerName, w, h / 2);
                boardCanvasViewPlayer = new BoardCanvasView(model, getApplicationContext(), computerName, w, h / 2);


                cardsLayoutParent.addView(boardCanvasViewComputer);
                cardsLayoutParent.addView(boardCanvasViewPlayer);
            }

        });

        final LinearLayout chipsLayoutParent = findViewById(R.id.chips_parent_view);
        ViewTreeObserver vtoChipsLayoutParent = chipsLayoutParent.getViewTreeObserver();

        vtoChipsLayoutParent.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                chipsLayoutParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int widthOfChipsLayout = chipsLayoutParent.getWidth();
                int heightOfChipsLayout = chipsLayoutParent.getHeight();

                ChipsCanvasView chipsCanvasView = new ChipsCanvasView(getApplicationContext(), widthOfChipsLayout, heightOfChipsLayout);
                chipsLayoutParent.addView(chipsCanvasView);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.model.notifyView();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void update(Observable observable, Object o) {
       manageBetButtonState();
       
    }

    private void manageBetButtonState(){
        if(model.getPlacedBets().size() != 2){
            this.betButton.setTextColor(Color.GREEN);
        }else{
            this.betButton.setTextColor(Color.RED);
        }
    }

}
