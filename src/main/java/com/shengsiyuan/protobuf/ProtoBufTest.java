package com.shengsiyuan.protobuf;

public class ProtoBufTest {
    public static void main(String[] args) throws Exception {
        DataInfo.Student student = DataInfo.Student.newBuilder()
                .setName("张三").setAge(20).setAddress("北京").build();

        byte[] bytes = student.toByteArray();

        DataInfo.Student clone = DataInfo.Student.parseFrom(bytes);
        System.out.println(clone);

        System.out.println(clone.getName());
        System.out.println(clone.getAge());
        System.out.println(clone.getAddress());
    }
}
