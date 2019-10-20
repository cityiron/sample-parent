package com.dbb.blog;

/**
 * @author tc
 * @date 2019-10-20
 */
public class DogMaoMao {

    private static DogMaoMao singleton = new DogMaoMao();

    public static int c1;

    public static int c2 = 10;

    private DogMaoMao() {
        c1++;
        c2++;
    }

    public static DogMaoMao getInstance(){
        return singleton;
    }

    @Override
    public String toString(){
        //return "c1 : " + c1 + " -- c2 : " + c2 + " -- age : " + Dog.age;
        return "";
    }

}
