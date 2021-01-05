package com.djh.nettypro01.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
* @Description: 通道数据传输
* @Author: DongJiaHao
*/
public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {

        //创建输入流与输出流
        FileInputStream fileInputStream = new FileInputStream("J:\\pic1.png");
        FileOutputStream fileOutputStream = new FileOutputStream("J:\\pic2.png");

        //获取通道
        FileChannel channel01 = fileInputStream.getChannel();
        FileChannel channel02 = fileOutputStream.getChannel();

        //复制文件
        channel01.transferTo(0, channel01.size(), channel02);

        //关闭流
        fileOutputStream.close();
        fileInputStream.close();
    }
}
