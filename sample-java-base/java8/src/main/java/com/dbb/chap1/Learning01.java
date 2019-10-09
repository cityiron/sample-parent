package com.dbb.chap1;

public class Learning01 {

    public static void main(String[] args) throws Exception {
        String oneline = FileProcess.processFile(
                "/Users/tiecheng/Documents/workspace/backend/advancement-parent/java8-sample/src/main/resources/demo.text",
                bufferedReader -> bufferedReader.readLine());

        String twoline = FileProcess.processFile(
                "/Users/tiecheng/Documents/workspace/backend/advancement-parent/java8-sample/src/main/resources/demo.text",
                bufferedReader -> bufferedReader.readLine() + bufferedReader.readLine());

        System.out.println(oneline);

        System.out.println(twoline);
    }

}
