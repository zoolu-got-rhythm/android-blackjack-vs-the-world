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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getTimedElapsed() {
        this.stopWatch.start();
        try {
            Thread.sleep(2540);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.stopWatch.stop();
        System.out.println(this.stopWatch.getTimedElapsed());
        System.out.println(this.stopWatch.toString());
    }
}