package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.model.blackjack.Game;

public class MainActivity extends AppCompatActivity {

    Game model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;

        final LinearLayout cardsLayoutParent = findViewById(R.id.cards_view_parent);


        ViewTreeObserver vtoCardsLayoutParent = cardsLayoutParent.getViewTreeObserver();
        vtoCardsLayoutParent.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //remove listener to ensure only one call is made.
                cardsLayoutParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int h = cardsLayoutParent.getHeight();
                int w = cardsLayoutParent.getWidth();

                BoardCanvasView boardCanvasViewComputer = new BoardCanvasView(getApplicationContext(), "computer", w, h / 2);
                BoardCanvasView boardCanvasViewPlayer = new BoardCanvasView(getApplicationContext(), "player", w, h / 2);
                cardsLayoutParent.addView(boardCanvasViewComputer);
                cardsLayoutParent.addView(boardCanvasViewPlayer);



                //make use of height and width
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




//        Log.d("action bar height", Integer.toString(getActionBarHeight(getApplicationContext())));
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

//    public static int getActionBarHeight(Context context) {
//        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
//        TypedArray a = context.obtainStyledAttributes(new TypedValue().data,  textSizeAttr);
//        int height = a.getDimensionPixelSize(0, 0);
//        a.recycle();
//        return height;
//    }
}
