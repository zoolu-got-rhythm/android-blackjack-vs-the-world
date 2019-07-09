package com.example.slurp.blackjackandroid.utils;

public class DummyUserName {
    public static String generate(int length){
        String alphabet = "abcdefghijklmnopqrstuvxyz";
        StringBuilder dummyNameStringBuilder = new StringBuilder();
        for(int i = 0; i <= length; i++){
            dummyNameStringBuilder.append(
                    alphabet.charAt((int) Math.floor(Math.random() * alphabet.length()))
            );
        }

        return dummyNameStringBuilder.toString()
                .substring(0, (int) Math.ceil(Math.random() * length));
    }
}
