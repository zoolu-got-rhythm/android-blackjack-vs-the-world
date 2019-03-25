package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.model.blackjack.Game;
import com.example.slurp.blackjackandroid.model.blackjack.Player;
import com.example.slurp.blackjackandroid.model.playingcards.PlayingCard;

import java.lang.reflect.Field;
import java.util.Observable;
import java.util.Observer;

public class BoardCanvasView extends View implements Observer {
    private Game model;
    private android.os.Handler mHandler;
    private Paint mPaint;
    private int width, height;
    private String playerName;

    public BoardCanvasView(Game model, Context context, String playerName, int width, int height) {
        super(context);
        this.model = model;
        this.model.addObserver(this);

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

        Player player = null;
        try {
            player = this.model.getPlayerByName(this.playerName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String playerNameAndHandValue = this.playerName;

        if(model.isGameOver()){

            if(model.getWinner() == null){
                playerNameAndHandValue += " DRAW. ";
            }else if(model.getWinner().getName().equals(this.playerName)){
                playerNameAndHandValue += " is WINNER. ";
            }else{
                playerNameAndHandValue += " is loser. ";
            }
        }

        playerNameAndHandValue += " HAND = "+
                (player.getName().equals("house") &&
                player.getHand().getCards().size() <= 2 &&
                !this.model.isGameOver() ?
                        "unknown" : player.getHand().getBestValue().toString().toLowerCase());

        canvas.drawText(playerNameAndHandValue, 40, (offsetFromTop / 2) + 11.25f, playerTextPaint);



        int cardWidth = 300;
        int cardHeight = 350;
        int xOffset = 20;

        int cardIndex = 0;

        for(PlayingCard card : player.getHand().getCards()){

            // find cardImageResourceId at runtime
            String imagePathForCard = EnumToCardPath.imgPathFromRankAndSuitEnums(card.rank, card.suit);
            if(player.getName().equals("house") &&
                    player.getHand().getCards().size() == 2 &&
                    cardIndex == 1 && !this.model.isGameOver()){
                imagePathForCard = "back.png";
            }
            imagePathForCard = imagePathForCard.substring(0, imagePathForCard.length() - 4); // remove .png extention
            int cardImageResourceId = getResId(imagePathForCard, R.drawable.class);
            Log.d("card resource path", imagePathForCard);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), cardImageResourceId);

            Paint cardShadowPaint = new Paint();
            cardShadowPaint.setColor(Color.DKGRAY);
            canvas.drawRoundRect(
                    new RectF(xOffset - 10, (offsetFromTop + 100) + 10, xOffset + cardWidth - 10, (offsetFromTop + 100) + cardHeight + 10),
                    15,
                    15,
                    cardShadowPaint);

            Paint cardBackgroundPaint = new Paint();
            cardBackgroundPaint.setColor(Color.WHITE);
            canvas.drawRoundRect(
                    new RectF(xOffset, (offsetFromTop + 100), xOffset + cardWidth, (offsetFromTop + 100) + cardHeight),
                    15,
                    15,
                    cardBackgroundPaint);

            canvas.drawBitmap(bitmap, null,
                    new Rect(xOffset, (offsetFromTop + 100), xOffset + cardWidth, (offsetFromTop + 100) + cardHeight), null);

            xOffset += 100;
            cardIndex++;
        }






        super.onDraw(canvas);
    }

    @Override
    public void update(Observable observable, Object o) {
        this.model = (Game) observable;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread
                invalidate();
            }
        });
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
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
