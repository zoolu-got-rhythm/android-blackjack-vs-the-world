package com.example.slurp.blackjackandroid.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

// make this singleton?
public class WallOfFameDbCrudOperations {
    // Gets the data repository in write mode

    // put on a lock on this class so read and creates can't be done at same time on db?
    private Semaphore mSemaphoreLock = new Semaphore(1);

    public interface QueryExecuctionListener{
        void onGetRowsDone(Cursor cursor);
    }

    private static WallOfFameDbCrudOperations instance = null;

    private WallOfFameDbCrudOperations() { }

    public static WallOfFameDbCrudOperations getInstance(){
        if(instance == null)
            instance = new WallOfFameDbCrudOperations();
        return instance;
    }

    public void createRow(final SQLiteWallOfFameDbHelper dbHelper, final String nickName, final String imageName){
        // put a lock on here?
        try {
            this.mSemaphoreLock.tryAcquire(2l, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                // getting readable or writable database should be called on a background thread

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(WallOfFameDbContract.WallOfFameEntry.COLUMN_NAME_NICKNAME, nickName);
                values.put(WallOfFameDbContract.WallOfFameEntry.COLUMN_NAME_IMAGE_PATH, imageName);

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(WallOfFameDbContract.WallOfFameEntry.TABLE_NAME, null, values);
                mSemaphoreLock.release();
            }
        }).start();
    }

    // haven't tested this method yet
    public int getRowsCount(final SQLiteWallOfFameDbHelper dbHelper){
        try {
            this.mSemaphoreLock.tryAcquire(2l, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor= db.rawQuery(
                "SELECT COUNT (*) FROM " + WallOfFameDbContract.WallOfFameEntry.TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        this.mSemaphoreLock.release();
        return count;
    }

    public void getAllRows(final SQLiteWallOfFameDbHelper dbHelper, final QueryExecuctionListener queryExecuctionListener) {

        try {
            this.mSemaphoreLock.tryAcquire(2l, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
                String[] projection = {
                        BaseColumns._ID,
                        WallOfFameDbContract.WallOfFameEntry.COLUMN_NAME_NICKNAME,
                        WallOfFameDbContract.WallOfFameEntry.COLUMN_NAME_IMAGE_PATH
                };

// Filter results WHERE "title" = 'My Title'
//        String selection = WallOfFameDbContract.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
                String sortOrder =
                        BaseColumns._ID + " ASC"; // not sure if this is needed: happens by default i think

                Cursor cursor = db.query(
                        WallOfFameDbContract.WallOfFameEntry.TABLE_NAME,   // The table to query
                        projection,             // The array of columns to return (pass null to get all)
                        null,              // The columns for the WHERE clause
                        null,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        sortOrder               // The sort order
                );

                queryExecuctionListener.onGetRowsDone(cursor);
                mSemaphoreLock.release();
            }
        }).start();


    }

}
