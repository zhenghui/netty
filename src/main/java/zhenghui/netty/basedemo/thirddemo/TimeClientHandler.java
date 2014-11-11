package zhenghui.netty.basedemo.thirddemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * User: zhenghui
 * Date: 2014年11月5日20:45:49
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = "hello server\r\n";
        for(int i = 0; i < 100; i++){
            //注意这里每次byteBuf都要重新生成。
            ByteBuf byteBuf = Unpooled.buffer(msg.length());
            byteBuf.writeBytes(msg.getBytes());
            ctx.writeAndFlush(byteBuf);
        }
//        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        System.out.println(new String(bytes));
//            super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
