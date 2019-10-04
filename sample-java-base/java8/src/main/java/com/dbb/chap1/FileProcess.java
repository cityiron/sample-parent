package com.dbb.chap1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FileProcess {

    public static String processFile(String path, BufferedReaderProcessor brp) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return brp.process(br);
        }
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) throws Exception {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }

        return result;
    }

    public static <T> void forEach(List<T> list, Consumer<T> consumer) throws Exception {
        for (T t : list) {
            consumer.accept(t);
        }
    }

    public static <T, R> List<R> list(List<T> list, Function<T, R> function) throws Exception {
        List<R> result = new ArrayList<>();
        for (T t : list) {
            result.add(function.apply(t));
        }

        return result;
    }


}
