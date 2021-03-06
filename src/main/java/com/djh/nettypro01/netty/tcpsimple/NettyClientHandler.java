package com.djh.nettypro01.netty.tcpsimple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("client " + ctx);

        //模拟粘包测试
        for(int i = 0; i < 1000; i++) {
            //固定长度拆包器测试
//            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: miao~", CharsetUtil.UTF_8));
            //行拆包器测试
//            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: miao~\n", CharsetUtil.UTF_8));
            //分隔符拆包器测试
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: miao~$", CharsetUtil.UTF_8));
        }
    }

    //当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器回复的消息是：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址：" + ctx.channel().remoteAddress());
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
