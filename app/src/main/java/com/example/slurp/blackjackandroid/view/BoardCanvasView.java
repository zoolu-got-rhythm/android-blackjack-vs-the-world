package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.model.blackjack.Game;
import com.example.slurp.blackjackandroid.model.blackjack.Player;
import com.example.slurp.blackjackandroid.model.playingcards.PlayingCard;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Observable;
import java.util.Observer;

public class BoardCanvasView extends View implements Observer {
    private Game model;
    private android.os.Handler mHandler;
    private Paint mPaint;
    private int width, height;
    private String playerName;
    final int cardTableGreen1 = ContextCompat.getColor(getContext(), R.color.darkGreen);
    final int cardTableGreen2 = ContextCompat.getColor(getContext(), R.color.darkGreen2);

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

        final float DEVICE_DENSITY_SCALE = getResources().getDisplayMetrics().density; // dpi  0.75, 1.0, 1.5, 2.0

        final int offsetFromTop = Math.round(30f * DEVICE_DENSITY_SCALE);

        Paint mBackgroundTilePaint = new Paint();
        int squareSizeX = this.width;
        int squareSizeY = this.height - offsetFromTop;
        squareSizeX = squareSizeX / 20;
        squareSizeY = squareSizeY / 20;

        for(int i = 0; i <= 20; i++){
            for(int j = 0; j <= 20; j++){
                if(j % 2 == 0){
                    mBackgroundTilePaint.setColor(i % 2 == 0 ? this.cardTableGreen1: this.cardTableGreen2);
                }else{
                    mBackgroundTilePaint.setColor(i % 2 == 0 ? this.cardTableGreen2 : this.cardTableGreen1);
                }
                canvas.drawRect(new Rect(i * squareSizeX,
                                j * (squareSizeY) + (offsetFromTop), (i * squareSizeX) + squareSizeX,
                                (j * squareSizeY) + squareSizeY + (offsetFromTop)),
                        mBackgroundTilePaint);
            }
        }

        Paint playerNamePaint = new Paint();
        playerNamePaint.setColor(Color.BLACK);
        canvas.drawRect(new Rect(0, 0, this.width, offsetFromTop), playerNamePaint);

        Paint playerTextPaint = new Paint();

        playerTextPaint.setColor(this.playerName.toLowerCase().equals("player") ? Color.GREEN : Color.MAGENTA);

        int headerTextSize = Math.round(12f * DEVICE_DENSITY_SCALE);
        playerTextPaint.setTextSize(headerTextSize);

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

        canvas.drawText(playerNameAndHandValue, 5 * DEVICE_DENSITY_SCALE, (offsetFromTop / 2) + (headerTextSize / 2), playerTextPaint);


        // values dp to px for different handsets/devices
        int cardWidth = Math.round(85f * DEVICE_DENSITY_SCALE);
        int cardHeight = Math.round(100f * DEVICE_DENSITY_SCALE);
        int xOffset = Math.round(20f * DEVICE_DENSITY_SCALE);
        int cardMargin = Math.round(20f * DEVICE_DENSITY_SCALE);
        int cardBorderSize = Math.round(8f * DEVICE_DENSITY_SCALE);

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
            cardShadowPaint.setColor(player.getName().equals("house") ? Color.MAGENTA : Color.GREEN);
            canvas.drawRoundRect(
                    new RectF(xOffset - cardBorderSize, (offsetFromTop + cardMargin) - cardBorderSize, xOffset + cardWidth + cardBorderSize, (offsetFromTop + cardMargin) + cardHeight + cardBorderSize),
                    cardBorderSize,
                    cardBorderSize,
                    cardShadowPaint);

            Paint cardBackgroundPaint = new Paint();
            cardBackgroundPaint.setColor(Color.WHITE);
            canvas.drawRoundRect(
                    new RectF(xOffset, (offsetFromTop + cardMargin), xOffset + cardWidth, (offsetFromTop + cardMargin) + cardHeight),
                    cardBorderSize,
                    cardBorderSize,
                    cardBackgroundPaint);

            canvas.drawBitmap(bitmap, null,
                    new Rect(xOffset, (offsetFromTop + cardMargin), xOffset + cardWidth, (offsetFromTop + cardMargin) + cardHeight), null);

            xOffset += Math.round(35f * DEVICE_DENSITY_SCALE);
            cardIndex++;
        }






        super.onDraw(canvas);
    }

    @Override
    public void update(Observable observable, Object o) {

        this.playSounds((Game) observable);

        this.model = (Game) observable;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread
                invalidate();
            }
        });
    }

    private void playSounds(Game newModelState){
        // check prev and new model state to see if card placement sound should be played
        if(this.model.getCurrentPlayer().getName().equals(this.playerName) &&
                this.model.getCurrentPlayer().getHand().getCards().size() >= 1){

            Log.d(this.getClass().getName(), "should play sound now");
//            new Thread(
//                    new PlaySoundThread(getContext(),
//                            R.raw.card_placement,
//                            0.8f)
//            ).run();
            SoundPlayerSingleton.getInstance()
                    .play(getResources().openRawResourceFd(R.raw.card_placement));

        }
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
