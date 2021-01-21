package com.djh.nettypro01.zerocopy;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

public class OldIOClient {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("127.0.0.1", 7001);

        File file = new File("J:\\rp.rar");
        InputStream inputStream = new FileInputStream(file);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] bytes = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();

        while ((readCount = inputStream.read(bytes)) >= 0) {
            total += readCount;
            dataOutputStream.write(bytes);
        }

        System.out.println("发送总字节数： " + total + "，耗时：" + (System.currentTimeMillis() - startTime));

    }
}
