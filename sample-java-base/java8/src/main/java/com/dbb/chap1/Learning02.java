package com.dbb.chap1;

import java.util.Arrays;
import java.util.List;

public class Learning02 {

    public static void main(String[] args) throws Exception {
        List<String> list = Arrays.asList("1", "2", "3", "4", "5");

        List<String> filter = FileProcess.filter(list, s -> s.equals("2"));

        System.out.println(filter);
    }

}
