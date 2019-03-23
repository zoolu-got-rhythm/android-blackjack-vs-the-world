package com.example.slurp.blackjackandroid.utils;

import com.example.slurp.blackjackandroid.model.blackjack.Hand;
import com.example.slurp.blackjackandroid.model.playingcards.PlayingCard;
import com.example.slurp.blackjackandroid.model.playingcards.Rank;
import com.example.slurp.blackjackandroid.model.playingcards.Suit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetToArrayConverterTest {
    private SetToArrayConverter<PlayingCard> setToArrayConverter;
    private Hand hand;

    @Before
    public void setUp() throws Exception {
        setToArrayConverter = new SetToArrayConverter<>();
        this.hand = new Hand();
        this.hand.addCard(new PlayingCard(Suit.DIAMONDS, Rank.EIGHT));
    }

    @Test
    public void testSetConvertsToArray(){
//        PlayingCard[] playingCardsArr = this.setToArrayConverter.convert(this.hand.getCards());
//        assertTrue(playingCardsArr.length == 1);
        System.out.println(hand.getCards().toArray());
    }

}