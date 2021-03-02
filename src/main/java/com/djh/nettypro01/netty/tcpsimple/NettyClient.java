package com.djh.nettypro01.netty.tcpsimple;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class NettyClient {

    public static void main(String[] args) throws Exception {

        //客户端需要一个事件循环组···
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();

            //设置相关参数
            bootstrap.group(eventExecutors) //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道的实现类（反射）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new FixedLengthFrameDecoder(20)); //固定长度拆包器
//                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024)); //行拆包器
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("$".getBytes()))); //指定分隔符拆包器
                            ch.pipeline().addLast(new NettyClientHandler()); //加入自己的处理器
                        }
                    });

            System.out.println("客户端 ok...");

            //启动客户端去连接服务器端
            //关于 ChannelFuture 要分析，涉及到 netty 的异步模型
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 6668).sync();

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }

    }

}
