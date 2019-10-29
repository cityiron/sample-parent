package com.dbb.netty.bytebuf;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * target
 * <p>
 * {@link io.netty.buffer.ByteBuf} {@link java.nio.ByteBuffer}
 * 1. ByteBuf 功能说明
 * 2. ByteBuf 源码分析
 * 3. ByteBuf 相关辅助类功能说明
 */
public class ByteBufDemo {

    public static void main(String[] args) throws Exception {
        // NIO编程 主要使用 ByteBuffer，七种基本类型（Boolean除外）都有自己的缓冲区实现

        // 缺点
        // 1. 长度固定，一旦分配完成，它的容量不能动态扩展和收缩，当需要编码当 POJO 对象大于 ByteBuffer当容量时，会发生索引越界异常
        // 2. 只有一个标识位置当指针 position, 读写的时候需要手工调用 flip() 和 rewind() 方法
        // 3. API 功能有限，一些高级和实用的特性它不支持
        byteBuffer();
    }

    /**
     * 注意，读取的内容是 position 到 limit 的内容
     *
     * @throws UnsupportedEncodingException
     */
    public static void byteBuffer() throws UnsupportedEncodingException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        System.out.println("初始化");

        print(byteBuffer);

        byteBuffer.put("DogBaoBao".getBytes("UTF-8"));
        System.out.println("设置值");

        print(byteBuffer);

        byteBuffer.flip();
        System.out.println("flip操作");

        print(byteBuffer);

        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        System.out.println("获取");

        print(byteBuffer);

        System.out.println(new String(bytes));

        // Exception in thread "main" java.nio.BufferOverflowException
        //	at java.base/java.nio.HeapByteBuffer.put(HeapByteBuffer.java:229)
        //	at java.base/java.nio.ByteBuffer.put(ByteBuffer.java:899)
        //	at com.dbb.netty.bytebuf.ByteBufDemo.byteBuffer(ByteBufDemo.java:51)
        //	at com.dbb.netty.bytebuf.ByteBufDemo.main(ByteBufDemo.java:23)
        // 因为 limit-position = 0 会出错

        byteBuffer.clear();
        System.out.println("清理");

        print(byteBuffer);

        byteBuffer.put("DogBaoBao".getBytes("UTF-8"));

        print(byteBuffer);

        byte[] bytes2 = new byte[9];
        // position = position + length
        byteBuffer.get(bytes2);


        System.out.println(new String(bytes2));
    }

    private static void print(ByteBuffer byteBuffer) {
        System.out.println(String.format("偏移量：%s, 容量：%s, 有限大小：%s, 位置：%s", byteBuffer.arrayOffset()
                , byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position()));
    }

}
