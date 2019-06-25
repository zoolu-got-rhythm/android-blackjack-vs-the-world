package com.example.slurp.blackjackandroid.view;

import android.graphics.Bitmap;

public class ScoreListItem {
    private int userRankNumber;
    private Bitmap imageBitmap;
    private String userName;

    public ScoreListItem(Bitmap imageBitmap, int userRankNumber, String userName) {
        this.imageBitmap = imageBitmap;
        this.userRankNumber = userRankNumber;
        this.userName = userName;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public int getUserRankNumber() {
        return userRankNumber;
    }

    public String getUserName() {
        return userName;
    }
}
