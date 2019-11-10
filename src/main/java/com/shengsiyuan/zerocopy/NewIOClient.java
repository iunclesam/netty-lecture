package com.shengsiyuan.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));
        socketChannel.configureBlocking(true);

        String fileName = "/Users/agan/Downloads/CentOS-7-x86_64-Minimal-1810.iso";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();

        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总字节数：" + transferCount + "，耗时：" + (System.currentTimeMillis() - startTime));

        fileChannel.close();
        socketChannel.close();
    }
}
