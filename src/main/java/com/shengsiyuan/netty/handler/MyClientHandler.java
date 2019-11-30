package com.shengsiyuan.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

public class MyClientHandler extends SimpleChannelInboundHandler<Long>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("client output：" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       /*
        ctx.writeAndFlush(123456L);
        ctx.writeAndFlush(1L);
        ctx.writeAndFlush(2L);
        ctx.writeAndFlush(3L);
        ctx.writeAndFlush(4L);
        */

        // ctx.writeAndFlush("helloworld");
        ctx.writeAndFlush(Unpooled.copiedBuffer("helloworld", Charset.forName("utf-8")));
    }

    // 打印异常信息，关闭连接
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
