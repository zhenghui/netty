package zhenghui.netty.basedemo.seventhdemo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * User: zhenghui
 * Date: 2014年11月12日22:10:46
 * Email:jingbo2759@163.com
 */
public class MessageClientHandler extends ChannelHandlerAdapter{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0;i<10;i++){
            Message.MsgInfo.Builder builder = Message.MsgInfo.newBuilder();
            builder.setMsg("hello client");
            builder.setMsgId(i);
            ctx.write(builder.build());
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message.MsgInfo msgInfo = (Message.MsgInfo) msg;
        System.out.println(msgInfo);
//        System.out.println(msg);
//        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
//        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
