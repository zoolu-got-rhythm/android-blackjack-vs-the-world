package com.example.slurp.blackjackandroid.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.controller.Controller;
import com.example.slurp.blackjackandroid.model.blackjack.Game;
import com.example.slurp.blackjackandroid.model.blackjack.Hand;
import com.example.slurp.blackjackandroid.model.blackjack.Player;
import com.example.slurp.blackjackandroid.utils.StopWatch;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Observer {

    private Game model;
    private Controller controller;
    private Button betButton, drawCardButton, stickButton;
    private ObjectAnimator betButtonAnimator, drawCardButtonAnimator, stickButtonAnimator;
    private BoardCanvasView boardCanvasViewComputer, boardCanvasViewPlayer;
    final String playerName = "player", computerName = "house";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.model = new Game(new Player(playerName, 10), new Player(computerName, 100000));
        this.model.init(); // starts game timer
        this.model.setGameListener(new Game.GameListener() {
            @Override
            public void onGameWin() {
                // congratulate user
                // store time to online score board

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SoundPlayerSingleton.getInstance()
                                .play(getResources().openRawResourceFd(R.raw.trumpet_fanfare));
                    }
                }, 800);



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!isFinishing()){


                            final EditText nameInput = new EditText(getApplicationContext());

// Set the default text to a link of the Queen
                            nameInput.setHint("nickname");

                            new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("submit your score online")
                                        .setMessage("input your name")
                                        .setView(nameInput)
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.submit_score_ok,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // whatever...
                                                        // go back to menu activity
                                                        Toast.makeText(getApplicationContext(),
                                                                "your time is: " +
                                                                        model.getPlayersTime() + "," +
                                                                        "thx for trying the demo: " +
                                                                        nameInput.getText(),
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                        .show();

                        }
                    }

                }, 500);

            }
        });

        this.controller = new Controller(this.model);

        this.model.addObserver(this);


//        betButton = findViewById(R.id.betBtn);
//        betButtonAnimator = this.blinkAnimationEffect(betButton);
//        betButton.setBackgroundColor(Color.DKGRAY);
//        betButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                controller.setBetPlaced(3);
//                controller.bet();
//            }
//        });

        drawCardButton = findViewById(R.id.cardBtn);
        drawCardButtonAnimator = this.blinkAnimationEffect(drawCardButton);
//        drawCardButton.setBackgroundColor(Color.DKGRAY);
        drawCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.playerDrawCard();
            }
        });

        stickButton = findViewById(R.id.stayBtn);
        stickButtonAnimator = this.blinkAnimationEffect(stickButton);
//        stickButton.setBackgroundColor(Color.DKGRAY);
        stickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.stay();
            }
        });

        final LinearLayout bettingLayoutParent = findViewById(R.id.bettingContainer);
        CustomBettingView customBettingView = new CustomBettingView(getApplicationContext(), playerName);
        this.model.addObserver(customBettingView);

        customBettingView.setOnBettingListener(new CustomBettingView.OnBettingListener() {
            @Override
            public void onBetMade(View v, int chipsValuePlaced) {
                controller.setBetPlaced(chipsValuePlaced);
                controller.bet();
            }

            @Override
            public void onReadyToPlay(View v) {
                controller.checkBetsArePlacedAndStartGame();
            }
        });

        bettingLayoutParent.addView(customBettingView);

        final LinearLayout hintsLayoutParent = findViewById(R.id.hints_container);
        HintView hintView = new HintView(getApplicationContext());
        this.model.addObserver(hintView);
        hintsLayoutParent.addView(hintView);






        final LinearLayout cardsLayoutParent = findViewById(R.id.cards_view_parent);
        ViewTreeObserver vtoCardsLayoutParent = cardsLayoutParent.getViewTreeObserver();

        vtoCardsLayoutParent.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //remove listener to ensure only one call is made.
                cardsLayoutParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int h = cardsLayoutParent.getHeight();
                int w = cardsLayoutParent.getWidth();

                boardCanvasViewComputer = new BoardCanvasView(model, getApplicationContext(), computerName, w, h / 2);
                boardCanvasViewPlayer = new BoardCanvasView(model, getApplicationContext(), playerName, w, h / 2);

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

                ChipsCanvasView chipsCanvasView = new ChipsCanvasView(getApplicationContext(),
                        model,
                        playerName,
                        widthOfChipsLayout,
                        heightOfChipsLayout);

                model.addObserver(chipsCanvasView);
                chipsLayoutParent.addView(chipsCanvasView);

            }
        });


//        final LinearLayout timerLayoutParent = findViewById(R.id.timer_parent_view);
//        ViewTreeObserver vtotimerLayoutParent = timerLayoutParent.getViewTreeObserver();
//
//        vtotimerLayoutParent.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                timerLayoutParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int widthOfTimerLayoutContainer = timerLayoutParent.getWidth();
//                int heightOfTimerLayoutContainer = timerLayoutParent.getHeight();
//
//                StopWatchView stopWatchView = new StopWatchView(
//                        getApplicationContext(),
//                        model,
//                        widthOfTimerLayoutContainer,
//                        heightOfTimerLayoutContainer
//                );
//                timerLayoutParent.addView(stopWatchView);
//            }
//        });
        new StopWatchViewManager(this, this.model);



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
//       manageBetButtonState();
       manageDrawCardButtonState();
       manageStickButtonState();
    }

//    private void manageBetButtonState(){
//        if(model.getPlacedBets().size() != 2 || model.isGameOver()){
////            this.betButton.setTextColor(Color.GREEN);
//            this.betButton.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
//            betButtonAnimator.start();
//        }else{
//            this.betButton.setTextColor(Color.RED);
//            this.betButton.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            betButtonAnimator.pause();
//        }
//    }

    // these manage anim btn's methods can be refactored
    private void manageDrawCardButtonState(){
        boolean areInitialCardsDealt = false;
        if(model.getPlayersInDeal().size() == 2)
            areInitialCardsDealt = model.getPlayersInDeal().get(0).getHand().getCards().size() >= 2 &&
                    model.getPlayersInDeal().get(1).getHand().getCards().size() >= 2;

        boolean isPlayersTurn = model.getCurrentPlayer().getName().equals(playerName);

        if(isPlayersTurn &&
                model.getPlacedBets().size() == 2 &&
                areInitialCardsDealt &&
                !model.isGameOver()){
//            this.betButton.setTextColor(Color.GREEN);
            this.drawCardButton.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
            drawCardButtonAnimator.start();
        }else if(isPlayersTurn &&
                model.getPlacedBets().size() == 2 &&
                !areInitialCardsDealt &&
                !model.isGameOver()){
            this.drawCardButton.setTextColor(Color.YELLOW);
            this.drawCardButton.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            drawCardButtonAnimator.pause();
        }else{
            this.drawCardButton.setTextColor(Color.RED);
            this.drawCardButton.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            drawCardButtonAnimator.pause();
        }
    }

    private void manageStickButtonState(){
        boolean areInitialCardsDealt = false;
        if(model.getPlayersInDeal().size() == 2)
            areInitialCardsDealt = model.getPlayersInDeal().get(0).getHand().getCards().size() >= 2 &&
                    model.getPlayersInDeal().get(1).getHand().getCards().size() >= 2;

        boolean isPlayersTurn = model.getCurrentPlayer().getName().equals(playerName);

        if(isPlayersTurn &&
                model.getPlacedBets().size() == 2 &&
                areInitialCardsDealt &&
                !model.isGameOver()){
//            this.betButton.setTextColor(Color.GREEN);
            this.stickButton.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
            stickButtonAnimator.start();
        }else if(isPlayersTurn &&
                model.getPlacedBets().size() == 2 &&
                !areInitialCardsDealt &&
                !model.isGameOver()){
            this.stickButton.setTextColor(Color.YELLOW);
            this.stickButton.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            stickButtonAnimator.pause();
        }else{
            this.stickButton.setTextColor(Color.RED);
            this.stickButton.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            stickButtonAnimator.pause();
        }
    }

    private ObjectAnimator blinkAnimationEffect(Button btn){
        ObjectAnimator anim = ObjectAnimator.ofInt(btn, "textColor", Color.DKGRAY,
                Color.GREEN, Color.DKGRAY);
        anim.setDuration(700);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        return anim;
    }

}
