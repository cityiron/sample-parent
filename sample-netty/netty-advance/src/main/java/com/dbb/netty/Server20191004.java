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

public class Server20191004 {

    public static void main(String[] args) throws IOException {
        new ServerStart(8222, new ChannelHandler()).run();
    }

    static class ChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline p = socketChannel.pipeline();
            p.addLast(new LineBasedFrameDecoder(1024));//很重要哦
            p.addLast(new StringDecoder());//很重要哦
            p.addLast(new ServerHandler());
        }
    }

    static class ServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String body = (String) msg;
            System.out.println(body);
            String req = "感谢关注，希望在这里找到你想要的。" + System.getProperty("line.separator");
            ByteBuf resp = Unpooled.copiedBuffer(req.getBytes());
            ctx.writeAndFlush(resp);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // Close the connection when an exception is raised.
            cause.printStackTrace();
            ctx.close();
        }

    }

}
