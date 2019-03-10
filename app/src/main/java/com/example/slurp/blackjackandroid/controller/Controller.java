package com.example.slurp.blackjackandroid.controller;

import com.example.slurp.blackjackandroid.model.blackjack.EnumToValueMapper;
import com.example.slurp.blackjackandroid.model.blackjack.Game;
import com.example.slurp.blackjackandroid.model.blackjack.NoPlayersInGameException;
import com.example.slurp.blackjackandroid.model.blackjack.Player;

// sole responsibility is to control the model (game class) this also helps with testability
public class Controller{
    private Game model;
    private int betPlaced;

    public Controller(Game model){
        this.model = model;
        this.betPlaced = 0;
    }

    private void getWinnerAndRestGameIfOver(){
        // move to view
        Player winner = this.model.getWinner(); // this also deals with assigning chips to winner,
        // should refactor this

        // draw condition
        if(winner == null){
            this.model.resetGame(false, 0);
            return;
        }

        int playerChipsValue = 0;
        try{
            playerChipsValue = model.getPlayerByName("player").getChips().getCurrentBalance();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        // this should be in view
        if(playerChipsValue >= model.getChipsNeededToWin()) {
            // do something when win
        }

        // game over condition: reset game
        if(playerChipsValue < 3 ){
                this.model.resetGame(true, 3);
        }
    }

    public void playerDrawCard(){
        System.out.println("execute");
        model.twist(model.getCurrentPlayer());
        if(model.isGameOver())
            this.getWinnerAndRestGameIfOver();
    }

    // controls how the computer reacts to the player when he sticks
    public void stay(){
        model.stick(model.getCurrentPlayer());
        try {
            model.nextPlayer();
        } catch (NoPlayersInGameException e) {
            e.printStackTrace();
        }

        int computerValue = EnumToValueMapper.getHandIntValueFromHandValueEnum(
                model.getCurrentPlayer().getHand().getBestValue());

        int playerValue = 0;

        try {
            playerValue = EnumToValueMapper.getHandIntValueFromHandValueEnum(
                    model.getPlayerByName("player").getHand().getBestValue());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        while (computerValue < playerValue
                && computerValue != 0 && computerValue != 1 && computerValue != 23){
            model.twist(model.getCurrentPlayer());
            computerValue = EnumToValueMapper.getHandIntValueFromHandValueEnum(
                    model.getCurrentPlayer().getHand().getBestValue());

            System.out.println("looping");
        }




        this.getWinnerAndRestGameIfOver();
    }

    private void placeBet(int betAmount) {
        int playerChipsAmount = 0;
        try {
            playerChipsAmount = model.getPlayerByName("player").getChips().getCurrentBalance();
        } catch (Exception e1) {

        }

        if (playerChipsAmount >= 3 && betAmount <= playerChipsAmount) {
            if (betAmount >= 3) {
                if (model.getPlacedBets().isEmpty()) {
                    try {
                        model.placeChipsToEnterGame(model.getPlayerByName("player"), betAmount);
                        model.placeChipsToEnterGame(model.getPlayerByName("house"), 3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    model.dealCards(2, true);
                }
            }
        }
    }


    // what's this for?
    public void bet(){
        System.out.println("bet placed");
        this.placeBet(this.getBetPlaced());
        this.setBetPlaced(0);
    }

    public int getBetPlaced() {
        return betPlaced;
    }

    public void setBetPlaced(int betPlaced) {
        this.betPlaced = betPlaced;
    }

    // check if game is over
}
