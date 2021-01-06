package com.djh.nettypro01.zerocopy;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class OldIOServer {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(7001);

        while (true) {

            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            byte[] bytes = new byte[4096];

            try {
                while (true) {
                    int read = dataInputStream.read(bytes, 0, bytes.length);
                    if (-1 == read) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
