package com.shengsiyuan.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

public class OldClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8899);

        String fileName = "/Users/agan/Downloads/CentOS-7-x86_64-Minimal-1810.iso";
        InputStream inputStream = new FileInputStream(fileName);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4 * 1024];

        long readCount;
        long total = 0;

        long startTime = System.currentTimeMillis();

        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }

        System.out.println("发送的总字节数：" + total + "，耗时：" + (System.currentTimeMillis() - startTime));

        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
