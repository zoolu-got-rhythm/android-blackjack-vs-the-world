package com.example.slurp.blackjackandroid.services;

import android.provider.BaseColumns;

public final class WallOfFameDbContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private WallOfFameDbContract() {}

    /* Inner class that defines the table contents */
    public static class WallOfFameEntry implements BaseColumns {
        public static final String TABLE_NAME = "wallOfFameScores";
        public static final String COLUMN_NAME_NICKNAME = "title";
        public static final String COLUMN_NAME_IMAGE_PATH = "subtitle";
    }


}
