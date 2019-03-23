package com.example.slurp.blackjackandroid.utils;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;

// this doesn't currently work properly, new Object cast to array throws runtime exception
public class SetToArrayConverter<T>{
    public T[] convert(Set<T> set){

        T[] arr = (T[]) new Object[set.size()];

        int i = 0;
        for(T t : set){
            arr[i] = t;
            i++;
        }

        return arr;
    }
}
