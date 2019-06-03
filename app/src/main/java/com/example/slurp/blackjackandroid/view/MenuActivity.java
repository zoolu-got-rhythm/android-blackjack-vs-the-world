package com.example.slurp.blackjackandroid.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.utils.IntegerToWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mScoresListRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ScoreListAdapter mScoreListAdapter;
    private List<ScoreListItem> scoreListItems;
    private AnimationManager mPlayButtonAnimationManager;
    private boolean isCountingDownFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        final LinearLayout hintsLayoutParent = findViewById(R.id.title_container);
        final MenuHintView menuHintView = new MenuHintView(getApplicationContext());
        hintsLayoutParent.addView(menuHintView);

        final Button playButton = findViewById(R.id.playButton);
        this.mPlayButtonAnimationManager = new AnimationManager(this, playButton);
        this.mPlayButtonAnimationManager.start();


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isCountingDownFlag){
                    isCountingDownFlag = true;

                    final Timer mCountDownTimer = new Timer();

                    mCountDownTimer.scheduleAtFixedRate(new TimerTask() {
                        private int countDown = 3;
                        @Override
                        public void run() {
                            if(countDown == 0){
                                mCountDownTimer.cancel();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }else{
                                menuHintView.getSpeechScrollerView().drawDialogueBox(
                                        IntegerToWord.convert(countDown).toUpperCase());
                                countDown--;
                            }
                        }
                    }, 0, 1000);
                }
            }
        });

        this.scoreListItems = new ArrayList<>();
        this.scoreListItems.add(new ScoreListItem(R.drawable.me_placeholder_img, 5, "Bobby23"));
        this.scoreListItems.add(new ScoreListItem(R.drawable.me_placeholder_two, 2, "chrisManDoo"));
        this.scoreListItems.add(new ScoreListItem(R.drawable.me_placeholder_img, 22, "slime"));

        this.mScoresListRecyclerView = findViewById(R.id.score_recycler_view);
        this.mScoresListRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(this);
        this.mScoreListAdapter = new ScoreListAdapter(this, this.scoreListItems);

        this.mScoresListRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mScoresListRecyclerView.setAdapter(this.mScoreListAdapter);


    }
}
