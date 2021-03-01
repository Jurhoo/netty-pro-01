package com.djh.nettypro01.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;


/*
* 说明：
* 1、SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 的子类
* 2、HttpObject 客户端和服务端相互通讯的数据被封装成 HTTPObject
* */
public class TestServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //当有读取事件的时候 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断 msg 是不是 HttpRequest 请求、
        if (msg instanceof HttpRequest) {

            System.out.println("pipeline hashcode " + ctx.pipeline().hashCode() + " TestServerHandler hashcode " + this.hashCode());

            System.out.println("msg 类型=" + msg.getClass());
            System.out.println("客户端地址" + ctx.channel().remoteAddress());

            //获取到
            HttpRequest httpRequest = (HttpRequest) msg;
            //获取 uri, 过滤特定资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico，不做响应");
                return;
            }

            //回复信息给浏览器[HTTP 协议]
            ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);

            //构造一个 http 的响应，即HTTPResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好的 response 返回
            ctx.writeAndFlush(response);
        }
    }


}
