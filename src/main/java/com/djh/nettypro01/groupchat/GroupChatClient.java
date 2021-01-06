package com.djh.nettypro01.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    //定义相关属性
    private static final String HOST = "127.0.0.1"; //服务器IP
    private static final int PORT = 6667; //服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    /**
     * 构造器
     * 完成初始化工作
     * */
    public GroupChatClient() {

        try {
            //取得选择器
            selector = Selector.open();
            //连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            //设置非阻塞模式
            socketChannel.configureBlocking(false);
            //注册 socketChannel 到 selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            //得到userName
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送信息
     * */
    public void sendInfo(String info) {

        info = userName + " 说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取服务器端回复的消息
     * */
    public void readInfo() {

        try {

            int readChannels = selector.select();
            if (readChannels > 0 ) { //又可以用的通道

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {

                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) { //处理读
                        //得到相关通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        //得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        String info = new String(buffer.array());
                        System.out.println(info.trim());
                    }

                    //移除当前selectionKey
                    keyIterator.remove();
                }
            } else {
                System.out.println("没有可用的通道...");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //启动客户端
        GroupChatClient chatClient = new GroupChatClient();

        //启动一个线程，每隔三秒，从服务器读取发送的数据
        new Thread(() -> {
            while (true) {
                chatClient.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }

    }

}
