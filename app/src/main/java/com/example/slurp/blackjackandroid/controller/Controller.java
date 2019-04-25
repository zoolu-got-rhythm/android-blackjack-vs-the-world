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

    private void getWinnerAndResetGameIfOver(){ // this should only reset game and do nothing else
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


        // game over condition: reset game
//        if(playerChipsValue < 3 ){
//                this.model.resetGame(true, 3);
//                return;
//        }

        this.model.resetGame(false, 0); // chips value is irrelevant if resetChips bool is false
    }

    public void playerDrawCard(){
        if(model.isGameOver())
            return;
        model.twist(model.getCurrentPlayer());
    }

    // controls how the computer reacts to the player when he sticks
    public void stay(){
        if(model.isGameOver())
            return;
        model.stick(model.getCurrentPlayer());
    }

    private void placeBet(int betAmount) {
        if(model.isGameOver())
            this.getWinnerAndResetGameIfOver();

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
                }
            }
        }
    }



    public void checkBetsArePlacedAndStartGame(){
        if(model.checkIfPlayerHasPlacedBetAndIsReadyToStartGame())
            model.dealCards(2, true);
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
