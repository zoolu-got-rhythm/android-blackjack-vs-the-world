package com.example.slurp.blackjackandroid.view.customFetchingScoresView;

import android.graphics.Point;

public class ScanningBeam extends SimulatedTapVisual {

    public ScanningBeam(Point point) {
        super(point);
        super.setMaxRadius(50);
        super.setVelocity(1);
    }
}
