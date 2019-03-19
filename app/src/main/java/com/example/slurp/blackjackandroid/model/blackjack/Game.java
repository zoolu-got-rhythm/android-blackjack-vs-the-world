package com.example.slurp.blackjackandroid.model.blackjack;


import android.support.annotation.Nullable;
import android.util.Log;

import com.example.slurp.blackjackandroid.model.playingcards.Deck;
import com.example.slurp.blackjackandroid.model.playingcards.PlayingCard;
import com.example.slurp.blackjackandroid.utils.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

// note: could use aspect oriented programming/aop here to trigger observer.update after every method call
public class Game extends Observable{


    // mode = delay or no-delay, sync or a-sync
    private StopWatch gameTimer;
    private ArrayList<Player> players;
    private ArrayList<Player> playersInGame;
    private ArrayList<Player> playersInDeal;
    private Player currentPlayer;
    private Player winner;
    private boolean isGameOver;
    private Deck theDeck;
    private HashMap<Player, Integer> placedBets;
    private final int chipsNeededToWin = 30;
    private boolean roundIsOver = false;

    public Game (Player playerNameA, Player playerNameB, Player... otherPlayerNames){
        super();
        players = new ArrayList<Player>();
        players.add(playerNameA);
        players.add(playerNameB);
        if (otherPlayerNames.length>0){
            for (Player other : otherPlayerNames){
                players.add(other);
            }
        }
        playersInGame = new ArrayList<Player>();
        playersInGame.addAll(players);
        playersInDeal = new ArrayList<Player>();
        playersInDeal.addAll(players);
//        isGameOver=false;
        theDeck=new Deck();
        currentPlayer = players.get(0);
        placedBets = new HashMap<>();
//        isGameOver = false;
    }

    public void startGameTimer() {
        this.gameTimer = new StopWatch();
        this.gameTimer.start();
    }

    public void stopGameTimer(){
        try {
            this.gameTimer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseGameTimer(){
        this.gameTimer.pause();
    }

    public void resumeGameTimer(){
        this.gameTimer.resume();
    }

    @Nullable
    public String getPlayersTime (){
        String formattedTime = null;
        try {
            formattedTime = this.gameTimer.getFormattedTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public void setRoundIsOver(boolean roundIsOver) {
        this.roundIsOver = roundIsOver;
    }

    public void resetGame(Boolean resetChips, int chipsValue){ // re-name to new game with player chips in tact
        roundIsOver = false;
        theDeck=new Deck();
        currentPlayer = players.get(0);
        placedBets = new HashMap<>();
        for(Player p : this.players){
            p.newHand();
            if(playersInGame.indexOf(p) == -1){
                playersInGame.add(p);
            }

            if(playersInDeal.indexOf(p) == -1){
                playersInDeal.add(p);
            }
        }
        Player player;

        if(resetChips)
            try{
                player = this.getPlayerByName("player");
                player.getChips().setCurrentBalance(chipsValue);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

//        isGameOver = false;

        this.notifyView();
    }



    public void placeChipsToEnterGame(Player player, int chipsAmount) throws Exception {
        if(chipsAmount < 3)
            throw new Exception("minimum buy in is 3 chips to play"); // make custom exception instead
        placedBets.put(player, chipsAmount);
//        this.notifyView();
    }

    public void dealCards(final int numberOfCardsInInitialShuffle, boolean shuffleFirst){

//        final PlayingCard aCard;

        final android.os.Handler handler = new android.os.Handler();

        if (shuffleFirst){
            theDeck.shuffle(100);
        }
//
        final Timer t = new Timer();

        t.scheduleAtFixedRate(new TimerTask() {
            int i = 0;
            @Override
            public void run() {

                final Player aPlayer = playersInGame.get(i % 2 == 0 ? 0 : 1);
                PlayingCard aCard = theDeck.deal();
                System.out.println(aCard.rank);
                aPlayer.getHand().addCard(aCard);
                i++;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyView();
                    }
                });

                if(i == numberOfCardsInInitialShuffle * 2)
                    t.cancel();

            }
        }, 0, 450);


    }

    // check blackjack

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Player getPlayerByName(String playerName) throws Exception { // change to no player found custom exception
        for(Player player : this.players){
            if(player.getName().equals(playerName))
                return player;
        }
        throw new Exception("no player with that name found");
    }

    public void nextPlayer() throws NoPlayersInGameException{

        int currentPlayerIndex=playersInDeal.indexOf(currentPlayer);
        int numberOfPlayersInGame=playersInDeal.size();

        if (numberOfPlayersInGame==0){
            throw new NoPlayersInGameException();
        }

        if (currentPlayerIndex==numberOfPlayersInGame-1){
            currentPlayer = playersInDeal.get(0);
        } else{
            currentPlayer = playersInDeal.get(currentPlayerIndex+1);
        }
    }

    public void stick(Player aPlayer){
        if(!this.getPlacedBets().isEmpty()) {
            playersInDeal.remove(aPlayer);
            aPlayer.stick();


            try {
                this.nextPlayer();
            } catch (NoPlayersInGameException e) {
                e.printStackTrace();
            }

            int computerValue = EnumToValueMapper.getHandIntValueFromHandValueEnum(
                    this.getCurrentPlayer().getHand().getBestValue());

            int playerValue = 0;

            try {
                playerValue = EnumToValueMapper.getHandIntValueFromHandValueEnum(
                        this.getPlayerByName("player").getHand().getBestValue());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            while (computerValue < playerValue
                    && computerValue != 0 && computerValue != 1 && computerValue != 23){
                this.twist(this.getCurrentPlayer());
                computerValue = EnumToValueMapper.getHandIntValueFromHandValueEnum(
                        this.getCurrentPlayer().getHand().getBestValue());

                System.out.println("looping");
            }





            Player losingPlayer = null;

            // if not draw remove loser from game
            for(Player p : this.getPlayersInGame()){
                if(this.getWinner() != null && !this.getWinner().getName().equals(p.getName())){
                    losingPlayer = p;
                }
            }

            // if not draw
            if(losingPlayer != null){
                this.getPlayersInGame().remove(losingPlayer);
                this.getPlayersInDeal().remove(losingPlayer);
//                int chipsValue = placedBets.get(aPlayer);
//                aPlayer.getChips().removeChips(chipsValue);
                    this.addChipsToWinnerRemoveFromLoosers(this.getWinner());
            }

            this.setRoundIsOver(true);

            this.notifyView();
        }
    }


    public void twist(Player aPlayer){
        if(!this.getPlacedBets().isEmpty()) {
            PlayingCard nextCard = theDeck.deal();
            aPlayer.getHand().addCard(nextCard);
            if (aPlayer.getHand().getBestValue() == HandValue.BUST) {
                playersInGame.remove(aPlayer);
                playersInDeal.remove(aPlayer);
//                int chipsValue = placedBets.get(aPlayer);
//                aPlayer.getChips().removeChips(chipsValue);
                this.addChipsToWinnerRemoveFromLoosers(this.getWinner());
                setRoundIsOver(true);
            }
            this.notifyView();
        }
    }

    public boolean isGameOver(){
        return ((playersInGame.size()==1) || (playersInDeal.isEmpty()) || roundIsOver);
    }

    public int getNumberOfPlayersInGame() {
        return playersInGame.size();
    }

    public int getNumberOfPlayersInDeal() {
        return playersInDeal.size();
    }

    public Player getWinner(){
        Player theWinner = null;

        //if everyone else has gone bust, the winner is the last player standing
        if (playersInGame.size()==1){
            theWinner=playersInGame.get(0);
            return theWinner;
        }

        //draw logic: return null if draw and don't remove any chips
        Boolean isDraw = false;
        for(int i = 1; i < playersInGame.size(); i++){
            if(playersInGame.get(i - 1).getHand().getBestValue().ordinal() ==
                    playersInGame.get(i).getHand().getBestValue().ordinal()){
                isDraw = true;
            }else{
                isDraw = false;
                break;
            }
        }

        if(isDraw){
            return theWinner;
        }

        //Otherwise, it is the player with the best hand-value
        theWinner = playersInGame.get(0);
        for (Player aPlayer : playersInGame) {
            if (aPlayer.getHand().getBestValue().ordinal() > theWinner.getHand().getBestValue().ordinal()) {
                theWinner = aPlayer;
            }
        }

        return theWinner;
    }

    public void addChipsToWinnerRemoveFromLoosers(Player theWinner){
        // give chips to winner, remove from loosers
        for(int j = 0; j < players.size(); j++){
            if(players.get(j) == theWinner){
                int chipsValue = placedBets.get(theWinner);
                players.get(j).getChips().addChips(chipsValue);
                Log.d("chips val", Integer.toString(chipsValue));
            }else{
                int chipsValue = placedBets.get(players.get(j));
                players.get(j).getChips().removeChips(chipsValue);
            }
        }
    }

    public void notifyView(){
        System.out.println("notifying view");
        super.setChanged();
        super.notifyObservers(this);
        super.clearChanged();
    }


    @Override
    public void addObserver(Observer o){ // can denote this method i think and just directly/externally call super method
            super.addObserver(o);
    }

    @Override
    public String toString() {
        return "Game{" +
                "players=" + players +
                ", winner=" + winner +
                '}';
    }

    public HashMap<Player, Integer> getPlacedBets() {
        return placedBets;
    }

    public int getChipsNeededToWin() {
        return chipsNeededToWin;
    }

    public ArrayList<Player> getPlayersInGame() {
        return playersInGame;
    }

    public ArrayList<Player> getPlayersInDeal() {
        return playersInDeal;
    }
}