package com.djh.nettypro01.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws Exception {

        //创建 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到 Selector 对象
        Selector selector = Selector.open();

        //绑定6666端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置非阻塞
        serverSocketChannel.configureBlocking(false);

        //把 ServerSocketChannel 注册到 Selector，关心时间为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true) {

            //等待一秒，如果没有事件发生，返回
            if (selector.select(1000) == 0) { //没有事件发生
                System.out.println("服务器等待了一秒，无连接");
                continue;
            }

            //如果返回的大于0，我们就获取到相关的 selectionKey 集合
            //1、如果返回大于0，表示已经获取到关注的事件了
            //2、selector.selectedKeys() 返回关注事件的集合
            //  通过 selectionKeys 获取反向通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历 Set<SelectionKey> , 使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                //获取到 SelectionKey
                SelectionKey key = keyIterator.next();
                //根据这个 key 对应的通道发生的事件做相应的处理
                if (key.isAcceptable()) { //如果是 OP_ACCEPT，说明有新的客户端来连接
                    //给该客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功 生成了一个socketChannel" + socketChannel.hashCode());
                    //将当前的 socketChannel 也注册到 selector，关注事件为 OP_READ，同时给 socketChannel 关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if (key.isReadable()) { //发生 OP_READ
                    //通过 key 反向获取到 channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("from client " + new String(buffer.array()));

                }

                //手动从集合中移除当前的 selectionKey，防止重复操作
                keyIterator.remove();

            }

        }


    }
}
