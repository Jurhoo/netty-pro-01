package com.djh.nettypro01.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7002));

        String fileName = "J:\\rp.rar";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();

        fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("总字节数： " + fileChannel.size() + " , 耗时： " + (System.currentTimeMillis() - startTime));

    }
}
