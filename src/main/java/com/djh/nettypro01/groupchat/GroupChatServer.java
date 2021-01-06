package com.djh.nettypro01.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    //定义相关属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    /**
     * 构造器
     * 初始化属性
     * */
    public GroupChatServer() {

        try {

            //获取选择器
            selector = Selector.open();
            //获取通道
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将 listenChannel 注册到 selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 监听
     * */
    private void listen() {

        try {

            //循环处理
            while (true) {
                int count = selector.select();
                if (count > 0) { //有事件要处理

                    //遍历得到selectionKey
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        //取出selectionKey
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将 sc 注册到 selector
                            sc.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress() + " 上线了 ");

                        }
                        if (key.isReadable()) { //通道发生读事件
                            //处理读
                            readData(key);
                        }

                        //手动从集合中移除当前的 selectionKey，防止重复操作
                        iterator.remove();

                    }

                } else {
                    System.out.println("等待......");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }

    /**
     * 读数据
     * */
    private void readData(SelectionKey key) {

        //定义一个 SocketChannel
        SocketChannel channel = null;
        try {
            //通过key反向获取channel
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("from 客户端：" + msg);

                //向其他客户端转发消息
                sendInfoOtherClients(msg, channel);
            }

        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了..");
                //取消注册
                key.cancel();
                channel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其他客户端（通道）
     * */
    private void sendInfoOtherClients(String msg, SocketChannel self) throws IOException {

        System.out.println("服务器转发消息中...");
        Set<SelectionKey> keys = selector.keys();

        //遍历所有注册到 selector 上的 socketChannel，并排除自己
        for (SelectionKey key : keys) {

            //通过 key 取到对应的 channel
            Channel targetChannel = key.channel();

            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将 msg 存到 buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将 buffer 里的数据写入到 dest 通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {

        //创建一个服务器对象
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }

}
