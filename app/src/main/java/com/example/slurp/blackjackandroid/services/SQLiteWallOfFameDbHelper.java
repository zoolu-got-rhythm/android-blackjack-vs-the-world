package com.example.slurp.blackjackandroid.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;

public class SQLiteWallOfFameDbHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WallOfFame.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WallOfFameDbContract.WallOfFameEntry.TABLE_NAME + " (" +
                    WallOfFameDbContract.WallOfFameEntry._ID + " INTEGER PRIMARY KEY," +
                    WallOfFameDbContract.WallOfFameEntry.COLUMN_NAME_NICKNAME+ " TEXT," +
                    WallOfFameDbContract.WallOfFameEntry.COLUMN_NAME_IMAGE_PATH + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WallOfFameDbContract.WallOfFameEntry.TABLE_NAME;

    public SQLiteWallOfFameDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(this.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
