package com.dbb.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerStart {

    private int port;
    private String ip;
    private ChannelInitializer channelInitializer;

    public ServerStart(int port, ChannelInitializer channelInitializer) {
        this.port = port;
        this.channelInitializer = channelInitializer;
    }

    public ServerStart(int port, String ip, ChannelInitializer channelInitializer) {
        this.port = port;
        this.ip = ip;
        this.channelInitializer = channelInitializer;
    }

    public void run() {
        run(this.ip, this.port);
    }

    public void run(int port) {
        run(this.ip, port);
    }

    public void run(String ip, int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {

            // 服务端连接门面类
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 1024);

            if (channelInitializer == null) {
                bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                    }
                });
            } else {
                bootstrap.childHandler(channelInitializer);
            }

            ChannelFuture future;
            if (ip != null) {
                future = bootstrap.bind(ip, port).sync();
            } else {
                future = bootstrap.bind(port).sync();
            }

            future.channel().closeFuture().sync();
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
