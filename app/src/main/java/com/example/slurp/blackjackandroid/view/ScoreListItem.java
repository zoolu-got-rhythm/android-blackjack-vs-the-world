package com.example.slurp.blackjackandroid.view;

public class ScoreListItem {
    private int imageRes, userRankNumber;
    private String userName;

    public ScoreListItem(int imageRes, int userRankNumber, String userName) {
        this.imageRes = imageRes;
        this.userRankNumber = userRankNumber;
        this.userName = userName;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getUserRankNumber() {
        return userRankNumber;
    }

    public String getUserName() {
        return userName;
    }
}
