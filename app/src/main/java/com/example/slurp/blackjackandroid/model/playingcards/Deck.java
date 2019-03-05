package com.example.slurp.blackjackandroid.model.playingcards;

import java.util.ArrayList;

/**
 * Created by Carl on 16/02/2016.
 */
public class Deck  {

    private ArrayList<PlayingCard> cards;

    public Deck(){

        cards = new ArrayList<PlayingCard>();
        for (Suit suit:Suit.values()){
            for (Rank rank : Rank.values()){
                cards.add(new PlayingCard(suit,rank));
            }
        }
    }

    public int size(){

        return cards.size();
    }

    public PlayingCard getCardAtIndex(int n){

        return cards.get(n);
    }

    public PlayingCard deal(){
        PlayingCard topCard = cards.get(0);
        cards.remove(topCard);
        return topCard;
    }

    public void shuffle(int numberOfSwaps) {
        //shuffle deck by swapping random pairs of cards n times

        for (int swap=0; swap<numberOfSwaps; swap++){
            this.swap((int)Math.round(Math.random()*51),(int)Math.round(Math.random()*51));
        }
    }

    private void swap(int aPlace, int anotherPlace){
        PlayingCard temp = cards.get(aPlace);
        cards.set(aPlace, cards.get(anotherPlace));
        cards.set(anotherPlace, temp);
    }
}
