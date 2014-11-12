package zhenghui.netty.basedemo.seventhdemo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * User: zhenghui
 * Date: 2014年11月12日22:10:51
 * Email:jingbo2759@163.com
 */
public class MessageServerHandler extends ChannelHandlerAdapter {

    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message.MsgInfo msgInfo = (Message.MsgInfo) msg;
        System.out.println(msgInfo);
        Message.MsgInfo.Builder builder = Message.MsgInfo.newBuilder();
        builder.setMsg("hello client");
        builder.setMsgId(++count);
        ctx.writeAndFlush(builder.build());
//        Message message = (Message) msg;
//        System.out.println(msg);
//        Message tmp = new Message();
//        tmp.setMsg("hello client");
//        tmp.setMsgId((long) ++count);
//        ctx.writeAndFlush(tmp);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
