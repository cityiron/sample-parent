package com.dbb.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.io.IOException;

public class Client20191004 {

    public static void main(String[] args) throws IOException {
        new ClientStart(8222, new ChannelHandler()).run();
    }

    static class ChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline p = socketChannel.pipeline();
            p.addLast(new LineBasedFrameDecoder(1024)); //很重要哦
            p.addLast(new StringDecoder()); //很重要哦
            p.addLast(new ClientHandler());
        }
    }

    static class ClientHandler extends ChannelInboundHandlerAdapter {

        private byte[] req;

        /**
         * Creates a client-side handler.
         */
        public ClientHandler() {
            req = ("DogBaoBao, 博客地址：blog.funnycode.cn。" + System.getProperty("line.separator")).getBytes();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ByteBuf message;
            for (int i = 0; i < 100; i++) {
                message = Unpooled.buffer(req.length);
                message.writeBytes(req);
                ctx.writeAndFlush(message);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String body = (String) msg;
            System.out.println(body);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

}
