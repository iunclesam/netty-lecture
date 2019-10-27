package com.shengsiyuan.nio;

import java.nio.ByteBuffer;

/**
 * ByteBuffer类型化的put和get方法
 */
public class NioTest5 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(Integer.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putDouble(Double.MAX_VALUE);
        buffer.putChar(Character.MAX_VALUE);
        buffer.putShort(Short.MAX_VALUE);
        buffer.putChar(Character.MIN_VALUE);

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());

    }
}
