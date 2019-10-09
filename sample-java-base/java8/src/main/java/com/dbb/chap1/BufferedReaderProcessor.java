package com.dbb.chap1;

import java.io.BufferedReader;

@FunctionalInterface
public interface BufferedReaderProcessor {

    String process(BufferedReader bufferedReader) throws Exception;

}
