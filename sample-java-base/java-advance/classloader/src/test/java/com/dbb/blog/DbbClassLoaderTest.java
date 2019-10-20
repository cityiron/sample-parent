package com.dbb.blog;

import java.lang.reflect.Constructor;

import static org.junit.Assert.*;

/**
 * @author tc
 * @date 2019-10-20
 */
public class DbbClassLoaderTest {

    public static void main(String[] args) throws Exception {
        DbbClassLoader dbbClassLoader = new DbbClassLoader();
        Class<?> dogClass = dbbClassLoader.findClass("com.dbb.blog.DogMaoMao");

        Constructor<?> constructor = dogClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object o = constructor.newInstance();
        DogMaoMao dogMaoMao = (DogMaoMao)constructor.newInstance();
        System.out.println(o instanceof DogMaoMao);
    }

}