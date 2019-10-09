package com.dbb.chap1;

import java.util.function.IntToLongFunction;
import java.util.function.ToIntFunction;

public class Learning05 {

    public static void main(String[] args) throws Exception {
        System.out.println(intToLong(1, i -> i + 1));


        System.out.println(toInt("111", (s) -> s.length()));
    }

    public static long intToLong(int i, IntToLongFunction itl) throws Exception {
        return itl.applyAsLong(i);
    }


    public static int toInt(String t, ToIntFunction<String> itl) throws Exception {
        return itl.applyAsInt(t);
    }

}
