package com.example.slurp.blackjackandroid.utils;

public class IntegerToWord {
    public static String convert(int n){
        if(n > 3 || n < 0)
            throw new IllegalArgumentException("parsed in int must be between 0-3");
        String word = "this word shouldn't display";

        switch (n){
            case 0 :
                word = "zero";
                break;
            case 1 :
                word = "one";
                break;
            case 2 :
                word = "two";
                break;
            case 3 :
                word = "three";
                break;
        }

        return word;
    };
}
