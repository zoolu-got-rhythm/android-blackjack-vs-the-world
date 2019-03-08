package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.slurp.blackjackandroid.model.blackjack.Game;

import java.util.Observable;
import java.util.Observer;

public class BoardCanvasView extends View implements Observer {
    private Game currentGameState;
    private android.os.Handler mHandler;
    private Paint mPaint;
    private int width, height;
    private String playerName;

    public BoardCanvasView(Context context, String playerName, int width, int height) {
        super(context);
        this.mHandler = new android.os.Handler();

        this.playerName = playerName;
        this.width = width;
        this.height = height;



        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.width,
                this.height);

        this.setLayoutParams(layoutParams);
//        this.setBackgroundColor(Color.BLACK);

//        this.mPaint = new Paint();
//        this.mPaint.setColor(Color.GREEN);

//        this.mPaint.setStrokeWidth(16f);
//        this.mPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        // draw
//            canvas.drawCircle(tap.getPoint().x, tap.getPoint().y, tap.getCurrentRadius(), this.mPaint);

        final int offsetFromTop = 90;

        Paint mBackgroundTilePaint = new Paint();
        int squareSizeX = this.width;
        int squareSizeY = this.height - offsetFromTop + 2;
        squareSizeX = squareSizeX / 20;
        squareSizeY = squareSizeY / 20;

        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                if(j % 2 == 0){
                    mBackgroundTilePaint.setColor(i % 2 == 0 ? Color.YELLOW: Color.GREEN);
                }else{
                    mBackgroundTilePaint.setColor(i % 2 == 0 ? Color.GREEN : Color.YELLOW);
                }
                canvas.drawRect(new Rect(i * squareSizeX,
                                j * (squareSizeY) + (offsetFromTop + 2), (i * squareSizeX) + squareSizeX,
                                (j * squareSizeY) + squareSizeY + (offsetFromTop + 2)),
                        mBackgroundTilePaint);
            }
        }

        Paint playerNamePaint = new Paint();
        playerNamePaint.setColor(Color.BLACK);
        canvas.drawRect(new Rect(0, 0, this.width - 2, offsetFromTop), playerNamePaint);

        Paint playerTextPaint = new Paint();

        playerTextPaint.setColor(this.playerName.toLowerCase().equals("player") ? Color.GREEN : Color.MAGENTA);
        playerTextPaint.setTextSize(40);
        canvas.drawText(this.playerName, 40, (offsetFromTop / 2) + 11.25f, playerTextPaint);





//        Paint redPaint = new Paint();
//        redPaint.setColor(Color.RED);
//        Point apple = this.currentGameState.getCurrentApple() != null ?
//                this.currentGameState.getCurrentApple().getPos() : null;


//        if(apple != null)
//            canvas.drawRect(new Rect(apple.x * squareSize,
//                    apple.y * squareSize, (apple.x * squareSize) + squareSize - 2,
//                    (apple.y * squareSize) + squareSize - 2), redPaint);
//
//
//        for (Point p : this.currentGameState.getPlayerSnake().getHeadAndBody()) {
//
//            if(this.currentGameState.getPlayerSnake().getHeadAndBody().indexOf(p) == 0){
//                Paint snakeHeadPaint = new Paint();
//                snakeHeadPaint.setColor(getResources().getColor(R.color.purple));
//                canvas.drawRect(new Rect(p.x * squareSize,
//                                p.y * squareSize, (p.x * squareSize) + squareSize - 2,
//                                (p.y * squareSize) + squareSize -2),
//                        snakeHeadPaint);
//
//            }else{
//                canvas.drawRect(new Rect(p.x * squareSize,
//                                p.y * squareSize, (p.x * squareSize) + squareSize - 2,
//                                (p.y * squareSize) + squareSize - 2),
//                        this.mPaint);
//            }
//        }
//
//        if(currentGameState.getGameOver()) {
//
//            String gameOver = "GAME OVER";
//            String tapScreenToPlayAgain = "tap screen to play again";
//
//            Paint gameOverPaint = new Paint();
//            gameOverPaint.setTextSize(100f);
//            gameOverPaint.setColor(Color.RED);
//            gameOverPaint.setStyle(Paint.Style.FILL);
//
//            Paint tapScreenPaint = new Paint();
//            tapScreenPaint.setTextSize(50f);
//            tapScreenPaint.setColor(Color.GREEN);
//            tapScreenPaint.setStyle(Paint.Style.FILL);
//
//            Rect resultGameOver = new Rect();
//            Rect resultTapScreenToPlayAgain = new Rect();
//
//
//            gameOverPaint.getTextBounds(gameOver, 0, gameOver.length(), resultGameOver);
//
//            tapScreenPaint.getTextBounds(tapScreenToPlayAgain, 0, tapScreenToPlayAgain.length(),
//                    resultTapScreenToPlayAgain);
//
//
//            canvas.drawText(gameOver, (width / 2) - resultGameOver.width() / 2,
//                    (height / 2) + resultGameOver.height() / 2, gameOverPaint);
//
//            canvas.drawText(tapScreenToPlayAgain, (width / 2) - resultTapScreenToPlayAgain.width() / 2,
//                    ((height / 2) + resultTapScreenToPlayAgain.height() / 2)
//                            + resultGameOver.height(), tapScreenPaint);
//
//
//        }


        super.onDraw(canvas);
    }

    @Override
    public void update(Observable observable, Object o) {
        this.currentGameState = (Game) observable;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread
                invalidate();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
