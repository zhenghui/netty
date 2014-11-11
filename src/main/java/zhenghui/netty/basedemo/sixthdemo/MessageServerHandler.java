package zhenghui.netty.basedemo.sixthdemo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * User: zhenghui
 * Date:2014/11/11
 * Time:22:32
 * Email:jingbo2759@163.com
 */
public class MessageServerHandler extends ChannelHandlerAdapter {

    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        System.out.println(msg);
        Message tmp = new Message();
        message.setMsg("hello client");
        message.setMsgId((long) ++count);
        ctx.writeAndFlush(ctx);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
