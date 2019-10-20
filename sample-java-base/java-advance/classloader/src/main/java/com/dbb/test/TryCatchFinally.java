package com.dbb.test;

/**
 * @author tc
 * @date 2019-10-15
 */
public class TryCatchFinally {

    /**
     * 内层 finally
     * 外层 异常
     * 外层 finally
     *
     * 如果是外层异常先执行，那么细想一下，递归会怎么样
     * @param args
     */
    public static void main(String[] args) {

        try {

            try {

                int i = 1 / 0;

            //} catch (Exception e) {
            //    System.out.println("内层 异常");
            //    throw e;
            } finally {
                System.out.println("内层 finally");
            }

        } catch (Exception e) {
            System.out.println("外层 异常");
        } finally {
            System.out.println("外层 finally");
        }

    }

}
