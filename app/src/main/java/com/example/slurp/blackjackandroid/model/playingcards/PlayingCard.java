package com.example.slurp.blackjackandroid.model.playingcards;

/**
 * Created by Carl on 16/02/2016.
 */
public class PlayingCard  {

    public Rank rank;
    public Suit suit;

    public PlayingCard(Suit suit, Rank rank) {
        this.suit=suit;
        this.rank=rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayingCard that = (PlayingCard) o;

        if (rank != that.rank) return false;
        return suit == that.suit;

    }

    @Override
    public int hashCode() {
        int result = rank != null ? rank.hashCode() : 0;
        result = 31 * result + (suit != null ? suit.hashCode() : 0);
        return result;
    }

    int position(){

        return ((this.suit.ordinal())
                *(Rank.KING.ordinal()+1)
                +this.rank.ordinal());
    }


    @Override
    public String toString() {
        return rank +
                " of " + suit;
    }
}
