package com.example.slurp.blackjackandroid.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.slurp.blackjackandroid.R;
import com.example.slurp.blackjackandroid.services.SQLiteWallOfFameDbHelper;
import com.example.slurp.blackjackandroid.services.WallOfFameDbContract;
import com.example.slurp.blackjackandroid.services.WallOfFameDbCrudOperations;
import com.example.slurp.blackjackandroid.utils.IntegerToWord;
import com.example.slurp.blackjackandroid.view.customFetchingScoresView.ScanningForDevicesCompoundView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
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
    private final String MENU_TITLE = "blackjack vs the world";
    private SQLiteWallOfFameDbHelper mDbHelper;
    private final static String TAG = "menuActivity";
    private ScanningForDevicesCompoundView scanningForDevicesCompoundView;
    private final static int FETCH_DATA_INTERVAL_IN_MS = 1000 * 10;
    private final static int FETCH_INDICATOR_DURATION_IN_MS = 4000;
    private int mPrevCursorCount = 0;

    private class FetchScoresFromApiThread extends Thread{
        private Boolean running = true;

        @Override
        public void run() {
            while(running){
                try {
                    scanningForDevicesCompoundView.startScan();
                    // fetch score data
                    Thread.sleep(FETCH_INDICATOR_DURATION_IN_MS);
                    scanningForDevicesCompoundView.stopScan();
                    Thread.sleep(FETCH_DATA_INTERVAL_IN_MS -
                            FETCH_INDICATOR_DURATION_IN_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopThread(){
            this.running = false;
        }
    }

    private FetchScoresFromApiThread fetchScoresFromApiThread = new FetchScoresFromApiThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Log.d(TAG, "IS CREATED");


        // add a way too photo taking activity from menu, perhaps my holding down on -
        // playButton, also add feature toggle for this in dev and set to false in master.

        final LinearLayout hintsLayoutParent = findViewById(R.id.title_container);
        final MenuHintView menuHintView = new MenuHintView(getApplicationContext(), MENU_TITLE);
        hintsLayoutParent.addView(menuHintView);

        this.scanningForDevicesCompoundView =
                new ScanningForDevicesCompoundView(this, new ScanningForDevicesCompoundView.ScanningForDevicesCompoundViewListener() {
                    @Override
                    public void onMountedToViewTree() {
                        fetchScoresFromApiThread.start();
                    }
                });

        LinearLayout wallOfFameFetchContainer = findViewById(R.id.wallOfFameFetchContainer);
        wallOfFameFetchContainer.addView(this.scanningForDevicesCompoundView);

        final Button playButton = findViewById(R.id.playButton);
        this.mPlayButtonAnimationManager = new AnimationManager(this, playButton);
        this.mPlayButtonAnimationManager.start();

//        for(int i = 0; i < getFilesDir().list().length; i++){
//            String fileName = getFilesDir().list()[i];
//            Boolean didDeleteFile = new File(getFilesDir(), fileName).delete();
//            Log.d(TAG, "internal file was delete: " + Boolean.toString(didDeleteFile));
//        }

        this.mDbHelper =  new SQLiteWallOfFameDbHelper(this);


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
                                finish();
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

        playButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent submitUserTimeActivityIntent = new Intent(getApplicationContext(),
                        SubmitUserTimeActivity.class);
                startActivity(submitUserTimeActivityIntent);
                finish();

                return false;
            }
        });

        this.scoreListItems = new ArrayList<>();

//        this.scoreListItems.add(
//                new ScoreListItem(R.drawable.me_placeholder_img, 5, "Bobby23")
//        );

        this.mScoresListRecyclerView = findViewById(R.id.score_recycler_view);
        this.mScoresListRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(this);
        this.mScoreListAdapter = new ScoreListAdapter(this, this.scoreListItems);

        this.mScoresListRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mScoresListRecyclerView.setAdapter(this.mScoreListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "IS RESUMED");

        // load score data from sql lite or sharedPrefs into list

        // this is rough code for checking whether need to re-query database for new user scores
//        if(cursor.getCount() == mPrevCursorCount){
//            cursor.close();
//            mDbHelper.close();
//            return;
//        }
//
//        mPrevCursorCount = cursor.getCount();

        WallOfFameDbCrudOperations.getInstance().getAllRows(this.mDbHelper, new WallOfFameDbCrudOperations.QueryExecuctionListener() {
            @Override
            public void onGetRowsDone(Cursor cursor) {


                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // if there's no new data to update since last check, just skip looping through each database row,
                // seems like it can be expensive to pull data out of rows via cursor




                while(cursor.moveToNext()) {
                    long itemId = cursor.getLong(
                            cursor.getColumnIndexOrThrow(WallOfFameDbContract.WallOfFameEntry._ID));


                    String imageNameId = cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    WallOfFameDbContract.WallOfFameEntry.COLUMN_NAME_IMAGE_PATH)
                    );

//                    openFileInput(imageNameId)


                    Log.d(TAG, getFilesDir().getAbsolutePath());

//                    File fileContaintingImageBytes = new File(getFilesDir().getAbsolutePath(), imageNameId);
//                    Bitmap userPictureBitmap = null;
//                    try {
//                        userPictureBitmap = BitmapFactory.decodeStream(new FileInputStream(
//                                fileContaintingImageBytes
//                        ));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    String imageFilePath = getFilesDir() + "/" + imageNameId + ".jpg";

//                    Bitmap userPictureBitmap = BitmapFactory.decodeFile(imageFilePath);

//                    try {
////                        FileInputStream fileInputStream = openFileInput(imageNameId);
////                    } catch (FileNotFoundException e) {
////                        e.printStackTrace();
////                    }

                    Bitmap userPictureBitmap = loadImageBitmap(imageFilePath);

                    String userNickName = cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    WallOfFameDbContract.WallOfFameEntry.COLUMN_NAME_NICKNAME)
                    );

                    scoreListItems.add(new ScoreListItem(userPictureBitmap, (int) itemId, userNickName));
                }
                cursor.close();
                mDbHelper.close();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mScoreListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "IS PAUSED");

    }

    public Bitmap loadImageBitmap(String name){
        Bitmap bitmap = null;
        try{
            Thread.sleep(500);
            bitmap = BitmapFactory.decodeFile(name);
            Log.d(TAG, "snould have loaded bitmap");
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "IS DESTROYED");

        this.scanningForDevicesCompoundView.stopScan();
        fetchScoresFromApiThread.stopThread();
        // not sure if i need to do this
        this.mScoresListRecyclerView = null;
        this.scoreListItems = null;
        this.mScoreListAdapter = null;
    }
}
