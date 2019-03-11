package com.example.slurp.blackjackandroid.view;


import com.example.slurp.blackjackandroid.model.playingcards.Rank;
import com.example.slurp.blackjackandroid.model.playingcards.Suit;

// write tests for this class
public class EnumToCardPath {
    public static String imgPathFromRankAndSuitEnums(Rank rank, Suit suit){
        StringBuilder imgPath = new StringBuilder();

        if((int) rank.ordinal() != 0 && rank.ordinal() < 10) {
            imgPath.append(digitToWord((int) rank.ordinal() + 1));
            imgPath.append("_of_");
        }else if(rank.ordinal() == 0){
            imgPath.append("ace_of_");
        }else{
            switch (rank){
                case JACK:
                    imgPath.append("jack_of_");
                    break;
                case QUEEN:
                    imgPath.append("queen_of_");
                    break;
                case KING:
                    imgPath.append("king_of_");
                    break;

            }
        }

        switch (suit){
            case CLUBS:
                imgPath.append("clubs.png");
                break;
            case SPADES:
                imgPath.append("spades.png");
                break;
            case HEARTS:
                imgPath.append("hearts.png");
                break;
            case DIAMONDS:
                imgPath.append("diamonds.png");
                break;
        }

        return imgPath.toString();
    }

    private static String digitToWord(int n){
        String digitAsWord = null;
        switch (n){
            case 2:
                digitAsWord = "two";
                break;
            case 3:
                digitAsWord = "three";
                break;
            case 4:
                digitAsWord = "four";
                break;
            case 5:
                digitAsWord = "five";
                break;
            case 6:
                digitAsWord = "six";
                break;
            case 7:
                digitAsWord = "seven";
                break;
            case 8:
                digitAsWord = "eight";
                break;
            case 9:
                digitAsWord = "nine";
                break;
            case 10:
                digitAsWord = "ten";
                break;
        }

        return digitAsWord;
    }
}

