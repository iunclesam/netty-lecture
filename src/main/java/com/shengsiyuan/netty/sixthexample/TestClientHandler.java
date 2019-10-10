package com.shengsiyuan.netty.sixthexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class TestClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        int random = new Random().nextInt(3);

        MyDataInfo.MyMessage myMessage = null;
        if (random == 0) {
            MyDataInfo.Person person = MyDataInfo.Person.newBuilder()
                    .setName("张三").setAge(20).setAddress("北京").build();

            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.PersonType)
                    .setPerson(person).build();

        } else if (random == 1) {
            MyDataInfo.Dog dog = MyDataInfo.Dog.newBuilder()
                    .setName("一只狗").setAge(20).build();

            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.DogType)
                    .setDog(dog).build();
        } else {
            MyDataInfo.Cat cat = MyDataInfo.Cat.newBuilder()
                    .setName("一只猫").setCity("上海").build();

            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.CatType)
                    .setCat(cat).build();
        }



        ctx.channel().writeAndFlush(myMessage);
    }
}
