package com.example.slurp.blackjackandroid.model.blackjack;

import com.example.slurp.blackjackandroid.model.blackjack.Chips;

/**
 * Created by Carl on 16/02/2016.
 */
public class Player {

    private String name;
    private Hand hand;
    private boolean stillInTheGame;
    private Chips chips;

    public Player (String name, int balance){
        this.name = name;
        hand=new Hand();
        stillInTheGame =true;
        this.chips = new Chips(balance);
    }

    public void newHand(){
        this.hand = new Hand();
    }


    public Hand getHand(){
        return hand;
    }

    public void stick(){

        stillInTheGame =false;
    }



    public boolean getStillInTheGame(){
        return stillInTheGame;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hand=" + hand +
                '}';
    }

    public Chips getChips() {
        return chips;
    }
}