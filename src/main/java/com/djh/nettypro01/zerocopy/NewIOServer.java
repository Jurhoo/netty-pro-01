package com.djh.nettypro01.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {

    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7002);
        serverSocket.bind(inetSocketAddress);

        ByteBuffer buffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            int read = 0;

            while (-1 != read) {
                read = socketChannel.read(buffer);
                buffer.rewind();
            }
        }

    }
}
