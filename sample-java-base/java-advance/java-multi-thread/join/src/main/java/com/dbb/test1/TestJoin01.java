package com.dbb.test1;

import java.util.concurrent.TimeUnit;

/**
 * 第 0 次执行 start_
 * 我想当 threadTest 对象执行完毕后我再执行
 * 但是 sleep多久呢？
 * 答案是不能确认
 * 1930
 * 第 0 次执行 end_
 * 第 1 次执行 start_
 * 我想当 threadTest 对象执行完毕后我再执行
 * 但是 sleep多久呢？
 * 1269
 * 答案是不能确认
 * 第 1 次执行 end_
 * 第 2 次执行 start_
 * 我想当 threadTest 对象执行完毕后我再执行
 * 3932
 * 但是 sleep多久呢？
 * 答案是不能确认
 * 第 2 次执行 end_
 * 第 3 次执行 start_
 * 我想当 threadTest 对象执行完毕后我再执行
 * 但是 sleep多久呢？
 * 答案是不能确认
 * 1611
 * 第 3 次执行 end_
 * 第 4 次执行 start_
 * 我想当 threadTest 对象执行完毕后我再执行
 * 但是 sleep多久呢？
 * 9584
 * 答案是不能确认
 * 第 4 次执行 end_
 * 第 5 次执行 start_
 * 我想当 threadTest 对象执行完毕后我再执行
 * 但是 sleep多久呢？
 * 5134
 * 答案是不能确认
 * 第 5 次执行 end_
 * 第 6 次执行 start_
 * 6342
 * 我想当 threadTest 对象执行完毕后我再执行
 * 但是 sleep多久呢？
 * 答案是不能确认
 * 第 6 次执行 end_
 * 第 7 次执行 start_
 * 我想当 threadTest 对象执行完毕后我再执行
 * 206
 * 但是 sleep多久呢？
 * 答案是不能确认
 * 第 7 次执行 end_
 * 第 8 次执行 start_
 * 我想当 threadTest 对象执行完毕后我再执行
 * 但是 sleep多久呢？
 * 答案是不能确认
 * 3686
 * 第 8 次执行 end_
 * 第 9 次执行 start_
 * 我想当 threadTest 对象执行完毕后我再执行
 * 6764
 * 但是 sleep多久呢？
 * 答案是不能确认
 * 第 9 次执行 end_
 * @author tc
 * @date 2019-09-28
 */
public class TestJoin01 {

    public static void main(String[] args) {
        TestJoin01 testJoin01 = new TestJoin01();
        for (int i = 0; i < 10; i++) {
            System.out.println(String.format("第 %s 次执行 start_", i));
            testJoin01.run();
            System.out.println(String.format("第 %s 次执行 end_", i));
        }
    }

    public void run() {
        MyThread threadTest = new MyThread();
        threadTest.start();
        System.out.println("我想当 threadTest 对象执行完毕后我再执行");
        System.out.println("但是 sleep多久呢？");
        System.out.println("答案是不能确认");
    }

    class MyThread extends Thread {

        @Override
        public void run() {
            try {
                int secondValue = (int)(Math.random() * 10000);
                System.out.println(secondValue);
                TimeUnit.MILLISECONDS.sleep(secondValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
