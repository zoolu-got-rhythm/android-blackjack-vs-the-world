package com.example.slurp.blackjackandroid.utils;

import java.util.ArrayList;
import java.util.Date;

public class StopWatch {
    private Date startTime;
    private Date stopTime;
    private ArrayList<Date> pauseTimes, resumeTimes;
    private Boolean isPaused = false;

    public StopWatch(){
        this.pauseTimes = new ArrayList<>();
        this.resumeTimes = new ArrayList<>();
    }

    public long getTimedElapsedInMs() throws Exception {
        long elapsedTime;
        if(this.stopTime == null){
            elapsedTime = new Date().getTime() - this.startTime.getTime();
        }else{
            elapsedTime = this.stopTime.getTime() - this.startTime.getTime();
        }

        long deductedTime = 0;

        // can throw exception
        deductedTime = this.getDeductedTime();

        return elapsedTime - deductedTime;
    }

    private long getDeductedTime() throws Exception {
        if(this.resumeTimes.size() != this.pauseTimes.size())
            throw new Exception("must resume stopWatch first before can calculate deduced time");
        long deductedTime = 0;
        for(int i = 0; i < this.resumeTimes.size(); i++)
            deductedTime = this.resumeTimes.get(i).getTime() - this.pauseTimes.get(i).getTime();
        return deductedTime;
    }

    public void start(){
        this.startTime = new Date();
    }

    public void stop() throws Exception {
        if(this.isPaused)
            throw new Exception("pause must be resumed before stopping stopWatch");
        this.stopTime = new Date();
    }

    public void pause(){
        if(!this.isPaused){
            this.isPaused = true;
            this.pauseTimes.add(new Date());
        }
    }

    public void resume(){
        if(this.isPaused){
            this.isPaused = false;
            this.resumeTimes.add(new Date());
        }
    }

    public String getFormattedTime() throws Exception {
        StringBuilder formattedTime = new StringBuilder();


        long timeElapsedInMs = 0;
        // can throw exception: look into error/exception bubbling handling and stack traces
        timeElapsedInMs = this.getTimedElapsedInMs();

        String minutes = Integer.toString((int) Math.floor((timeElapsedInMs / 60000)));
        if(minutes.length() == 1)
            minutes = "0" + minutes;
        String seconds = Integer.toString((int) (timeElapsedInMs / 1000) % 60);
        // 100/1 of a second
        if(seconds.length() == 1)
            seconds = "0" + seconds;
        String oneHundreth = Integer.toString((int) Math.floor((((double) timeElapsedInMs / 1000 % 1) * 100)));
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
