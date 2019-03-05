package uk.ac.cf.GUI;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import uk.ac.cf.blackjack.EnumToValueMapper;
import uk.ac.cf.blackjack.Game;
import uk.ac.cf.blackjack.NoPlayersInGameException;
import uk.ac.cf.blackjack.Player;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class Controller implements ActionListener{
    private Game model;
    private CardView cardView;
    private int betPlaced;

    Controller(Game model){
        this.model = model;
        this.cardView = cardView;
        this.betPlaced = 0;
    }

    private void getWinnerAndRestGameIfOver(){
        System.out.println("is game over");
        System.out.println("GAME IS OVER");


        // found bug
        Player winner = this.model.getWinner();
        JOptionPane.showMessageDialog(null,
                winner != null ?
                        winner.getName() + " has won the round" : "draw");

        int playerChipsValue = 0;
        try{
            playerChipsValue = model.getPlayerByName("player").getChips().getCurrentBalance();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        if(playerChipsValue >= 35) {
            JOptionPane.showMessageDialog(null,
                    " you've beat the computer, well done!");
            System.exit(0);
        }

        if(playerChipsValue < 3 ){
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog (null,
                    "you don't have enough chips to bet, " +
                            "do you want to play again?","not enough chips",dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
                this.model.resetGame(true, 3);
                return;
            }else{
                System.exit(0);
            }
            // yes or no option
            // this
        }

        this.model.resetGame(false, 0);
    }

    public void playerDrawCard(){
        System.out.println("execute");

        model.twist(model.getCurrentPlayer());
        if(model.isGameOver())
            this.getWinnerAndRestGameIfOver();
    }

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

    public void placeBet(int betAmount){
        int playerChipsAmount = 0;
        try{
            playerChipsAmount = model.getPlayerByName("player").getChips().getCurrentBalance();
        }catch(Exception e1){

        }

        if (playerChipsAmount >= 3 && betAmount <= playerChipsAmount) {
            if(betAmount >= 3) {
                if(model.getPlacedBets().isEmpty()) {
                    try {
                        model.placeChipsToEnterGame(model.getPlayerByName("player"), betAmount);
                        model.placeChipsToEnterGame(model.getPlayerByName("house"), 3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    model.dealCards(2, true);
                }else{
                    // current game in play
                }
            }else{
                // minimum buy in is 3
            }
            // check for blackjack
        } else{
//            JOptionPane.showMessageDialog(this.cardView,
//            this.getBetPlaced(),
//            "not enough chips",
//            JOptionPane.YES_NO_OPTION);

            // you don't have enough chips
        }
    }

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
