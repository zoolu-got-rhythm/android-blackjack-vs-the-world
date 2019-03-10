package com.example.slurp.blackjackandroid.model.blackjack;


import com.example.slurp.blackjackandroid.model.playingcards.Deck;
import com.example.slurp.blackjackandroid.model.playingcards.PlayingCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

// note: could use aspect oriented programming/aop here to trigger observer.update after every method call
public class Game extends Observable{

    private ArrayList<Player> players;
    private ArrayList<Player> playersInGame;
    private ArrayList<Player> playersInDeal;
    private Player currentPlayer;
    private Player winner;
    private boolean isGameOver;
    private Deck theDeck;
    private HashMap<Player, Integer> placedBets;
    private final int chipsNeededToWin = 30;



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
    }

    public void resetGame(Boolean resetChips, int chipsValue){ // re-name to new game with player chips in tact
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

        isGameOver = false;

        this.notifyView();
    }



    public void placeChipsToEnterGame(Player player, int chipsAmount) throws Exception {
        if(chipsAmount < 3)
            throw new Exception("minimum buy in is 3 chips to play"); // make custom exception instead
        placedBets.put(player, chipsAmount);
//        this.notifyView();
    }

    public void dealCards(int numberOfCardsInInitialShuffle, boolean shuffleFirst){

        PlayingCard aCard;

        if (shuffleFirst){
            theDeck.shuffle(100);
        }
        for (int cards=1;cards<=numberOfCardsInInitialShuffle;cards++){
            for (Player aPlayer : playersInGame) {
                aCard = theDeck.deal();
                System.out.println(aCard.rank);
                aPlayer.getHand().addCard(aCard);
            }
        }

        this.notifyView();
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
        }
        else{
            currentPlayer = playersInDeal.get(currentPlayerIndex+1);
        }
    }

    public void stick(Player aPlayer){
        if(!this.getPlacedBets().isEmpty()) {
            playersInDeal.remove(aPlayer);
            aPlayer.stick();
        }
    }


    public void twist(Player aPlayer){
        if(!this.getPlacedBets().isEmpty()) {
            PlayingCard nextCard = theDeck.deal();
            aPlayer.getHand().addCard(nextCard);
            if (aPlayer.getHand().getBestValue() == HandValue.BUST) {
                playersInGame.remove(aPlayer);
                playersInDeal.remove(aPlayer);
                int chipsValue = placedBets.get(aPlayer);
                aPlayer.getChips().removeChips(chipsValue);
            }
            this.notifyView();
        }
    }

    public boolean isGameOver(){
        return ((playersInGame.size()==1) || (playersInDeal.isEmpty()));
    }

    public int getNumberOfPlayersInGame() {
        return playersInGame.size();
    }

    public int getNumberOfPlayersInDeal() {
        return playersInDeal.size();
    }

    public Player getWinner(){
        Player theWinner;

        //if everyone else has gone bust, the winner is the last player standing
        if (playersInGame.size()==1){
            theWinner=playersInGame.get(0);
            int chipsValue = placedBets.get(theWinner);
            theWinner.getChips().addChips(chipsValue);
            this.winner = theWinner;
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
            theWinner = null;
            this.winner = null;
            return theWinner;
        }

        //Otherwise, it is the player with the best hand-value
        theWinner = playersInGame.get(0);
        for (Player aPlayer : playersInGame) {
            if (aPlayer.getHand().getBestValue().ordinal() > theWinner.getHand().getBestValue().ordinal()) {
                theWinner = aPlayer;
            }
        }

        // give chips to winner, remove from loosers
        for(int j = 0; j < playersInGame.size(); j++){
            if(playersInGame.get(j) == theWinner){
                int chipsValue = placedBets.get(theWinner);
                playersInGame.get(j).getChips().addChips(chipsValue);
            }else{
                int chipsValue = placedBets.get(playersInGame.get(j));
                playersInGame.get(j).getChips().removeChips(chipsValue);
            }
        }

        this.winner=theWinner;
        return theWinner;
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
}