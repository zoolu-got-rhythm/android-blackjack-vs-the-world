package com.example.slurp.blackjackandroid.view;

// concrete product
public class HdpiSizes implements ResponsiveSizes {
    @Override
    public float getCardWidthForThisDevice() {
        return 85f;
    }

    @Override
    public float getCardHeightForThisDevice() {
        return 100f;
    }
}
