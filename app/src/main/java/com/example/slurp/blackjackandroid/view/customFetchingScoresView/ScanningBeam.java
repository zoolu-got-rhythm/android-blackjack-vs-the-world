package com.example.slurp.blackjackandroid.view.customFetchingScoresView;

import android.graphics.Point;

public class ScanningBeam extends SimulatedTapVisual {

    public static final int MAX_RADIUS = 50;

    public ScanningBeam(Point point) {
        super(point);
        super.setMaxRadius(MAX_RADIUS);
        super.setVelocity(1);
    }
}
