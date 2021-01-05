package com.djh.nettypro01.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
* @Description: 用一个Buffer，利用通道来实现文件复制
* @Author: DongJiaHao
* @Date: 2021/1/4
*/
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {

        //文件输入流、文件输出流
        FileInputStream fileInputStream = new FileInputStream("J:\\file01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("J:\\file02.txt");

        //获取通道
        FileChannel fileChannel01 = fileInputStream.getChannel();
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        //分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(14);

        //从输入通道循环读取数据
        while (true) {

            //清空buffer，position置为0
            buffer.clear();

            //从输入通道中读取数据到缓冲区
            int read = fileChannel01.read(buffer);

            if (read == -1) {
                break;
            }

            //读写切换
            buffer.flip();

            //将缓冲区里的数据写入到输出流
            fileChannel02.write(buffer);
        }

        //关闭流
        fileOutputStream.close();
        fileInputStream.close();
    }
}
