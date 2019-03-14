package com.example.slurp.blackjackandroid.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() throws Exception {
        this.stopWatch = new StopWatch();
    }

    @Test
    public void getTimedElapsed() {
        final long TIME_DELAY= 2510;
        final long SLACK_TIME = 50; // 50ms

        this.stopWatch.start();
        try {
            Thread.sleep(2540);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            this.stopWatch.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long timeElapsed = 0;
        try {
            timeElapsed = this.stopWatch.getTimedElapsedInMs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(timeElapsed >= TIME_DELAY && timeElapsed < TIME_DELAY + SLACK_TIME);
    }
}