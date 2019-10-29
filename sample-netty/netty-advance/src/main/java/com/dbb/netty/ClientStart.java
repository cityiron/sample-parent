package com.dbb.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientStart {

    private int port;
    private String ip;
    private ChannelInitializer channelInitializer;

    public ClientStart(int port, String ip, ChannelInitializer channelInitializer) {
        this.port = port;
        this.ip = ip;
        this.channelInitializer = channelInitializer;
    }

    public ClientStart(int port, ChannelInitializer channelInitializer) {
        this.port = port;
        this.ip = "127.0.0.1";
        this.channelInitializer = channelInitializer;
    }

    public void run() {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 客户端连接门面类
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workGroup) // I/O读写的线程组
                    .channel(NioSocketChannel.class) // 指定客户端使用的channel接口，对于TCP客户端，默认使用NioSocketChannel
                    .option(ChannelOption.TCP_NODELAY, true); // 激活或禁止TCP_NODELAY套接字选项，它决定是否使用Nagle算法。如果延时敏感性的应用，建议关闭Nagle算法

            if (channelInitializer == null) {
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                    }
                });
            } else {
                bootstrap.handler(channelInitializer);
            }

            ChannelFuture future = bootstrap.connect(ip, port).sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

}
