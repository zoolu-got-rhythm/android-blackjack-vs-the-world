package com.example.slurp.blackjackandroid.utils;

import java.util.Date;

public class StopWatch {
    private Date startTime;
    private Date stopTime;

    public StopWatch(){}

    public long getTimedElapsedInMs(){
        if(this.stopTime == null){
            return new Date().getTime() - this.startTime.getTime();
        }else{
            return this.stopTime.getTime() - this.startTime.getTime();
        }
    }

    public void start(){
        this.startTime = new Date();
    }

    public void stop(){
        this.stopTime = new Date();
    }

    @Override
    public String toString() {
        StringBuilder formattedTime = new StringBuilder();
        String minutes = Integer.toString((int) Math.floor((getTimedElapsedInMs() / 60000)));
        if(minutes.length() == 1)
            minutes = "0" + minutes;
        String seconds = Integer.toString((int) (getTimedElapsedInMs() / 1000) % 60);
        // 100/1 of a second
        if(seconds.length() == 1)
            seconds = "0" + seconds;
        String oneHundreth = Integer.toString((int) Math.floor((((double) getTimedElapsedInMs() / 1000 % 1) * 100)));
        if(oneHundreth.length() == 1)
            oneHundreth = "0" + oneHundreth;
        formattedTime.append(minutes);
        formattedTime.append(":");
        formattedTime.append(seconds);
        formattedTime.append(":");
        formattedTime.append(oneHundreth);
        return formattedTime.toString();
    }
}
