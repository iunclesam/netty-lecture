package com.shengsiyuan.netty.firstexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject>{

    /**
     *  handlerAdded
     *  channelRegistered
     *  channelActive
     *  class io.netty.handler.codec.http.DefaultHttpRequest
     *  /127.0.0.1:51043
     *  执行channelRead0
     *  请求方法名：GET
     *  class io.netty.handler.codec.http.LastHttpContent$1
     *  /127.0.0.1:51043
     *  channelInactive
     *  channelUnregistered
     *  handlerRemoved
     *
     *  区别
     *  1) curl "http://127.0.0.1:8899"
     *  2) 浏览器访问 http://127.0.0.1:8899
     *     a.除了请求以上地址，浏览器还会请求 http://127.0.0.1:8899/favicon.ico，查看开发者控制台
     *     b.控制台打印，需要等若干秒钟，才会看到以下打印输出，或者可以主动调用ctx.channel().close()立即关闭连接
     *       实际上需要判断HTTP协议版本和keep-alive请求头
     *       channelUnregistered
     *       handlerRemoved
     */

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        System.out.println(msg.getClass());
        System.out.println(ctx.channel().remoteAddress());
        Thread.sleep(8 * 1000);

        if (msg instanceof HttpRequest) {

            System.out.println("执行" + getCurrentMethodName());

            HttpRequest httpRequest = (HttpRequest) msg;
            System.out.println("请求方法名：" + httpRequest.method().name());

            URI uri = new URI(httpRequest.uri());

            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求favicon.ico");
                return;
            }

            ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            ctx.writeAndFlush(response);

            ctx.channel().close();
        }

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println(methodName);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println(methodName);
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println(methodName);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println(methodName);
        super.channelInactive(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println(methodName);
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println(methodName);
        super.handlerRemoved(ctx);
    }

    public static String getCurrentMethodName() {
        // return Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
