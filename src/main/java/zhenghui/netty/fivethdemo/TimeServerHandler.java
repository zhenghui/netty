package zhenghui.netty.fivethdemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * User: zhenghui
 * Date: 2014年11月6日21:28:25
 * Email:jingbo2759@163.com
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private int count;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(bytes);
//        System.out.println(new String(bytes));
        //这里可以直接转换成String了。不需要再自己转换
        String msg_str = (String) msg;
        System.out.println(msg_str);
        String str = "hello client"+ TimeServer.SEMICOLON;
        System.out.println("receive one msg.count="+ ++count);
        ByteBuf whriteByteBuf = Unpooled.copiedBuffer(str.getBytes());
        ctx.write(whriteByteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
//        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
