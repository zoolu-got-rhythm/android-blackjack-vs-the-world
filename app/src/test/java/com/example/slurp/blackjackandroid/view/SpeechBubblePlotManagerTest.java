package com.example.slurp.blackjackandroid.view;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SpeechBubblePlotManagerTest {

    private SpeechBubblePlotManager speechBubblePlotManager;

    @Before
    public void setUp() throws Exception {
        this.speechBubblePlotManager = new SpeechBubblePlotManager();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void plotSpeechBubble() {
    }

    @Test
    public void plotAngleCurvCoords() {
        ArrayList<CustomPoint> curve = this.speechBubblePlotManager.plotAngleCurvCoords(
                new CustomPoint(50, 50),
                20,
                4,
                SpeechBubblePlotManager.CURVE_ANGLE_ENUM.TOP_RIGHT
        );

        for(CustomPoint customPoint : curve) {
            System.out.println(customPoint.getX());
            System.out.println(customPoint.getY());
        }
    }

    @Test
    public void degreesToRadiansFunction(){
        System.out.println(this.speechBubblePlotManager.degreesToRadians(90));
    }
}