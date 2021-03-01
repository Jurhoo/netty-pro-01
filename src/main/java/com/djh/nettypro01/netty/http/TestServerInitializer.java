package com.djh.nettypro01.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个 netty 提供的 httpServerCodec codec => [coder - decoder]
        //HttpServerCodec 说明
        //1、HttpServerCodec 是 netty 提供的 http 的编解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //2、增加一个自定义的 handler
        pipeline.addLast("MyTestServerHandler", new TestServerHandler());
    }

}
