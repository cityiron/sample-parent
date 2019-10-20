package com.dbb.blog;

import java.util.UUID;

/**
 * @author tc
 * @date 2019-10-20
 */
public abstract class Dog {

    protected static final int age = 18;

    protected static String name = UUID.randomUUID().toString();

    static {
        System.out.println("I'm Dog");
    }

    public static void main(String[] args) {
        System.out.println(Dog.age);
    }
}
