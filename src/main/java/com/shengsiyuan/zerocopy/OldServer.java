package com.shengsiyuan.zerocopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OldServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8899);
        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            byte[] byteArray = new byte[4 * 1024];

            while (true) {
                int readCount = dataInputStream.read(byteArray, 0, byteArray.length);
                if (readCount == -1) {
                    break;
                }
            }
            dataInputStream.close();
            socket.close();
        }
    }
}
