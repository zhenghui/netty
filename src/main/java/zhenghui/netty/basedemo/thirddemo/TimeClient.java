package zhenghui.netty.basedemo.thirddemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * User: zhenghui
 * Date: 2014年11月5日20:45:34
 * netty second demo timeclient
 *
 * 使用LineBasedDecoder处理半包问题
 */
public class TimeClient {

    public static void main(String[] args) throws Exception {
        int port = 8001;
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });//这里就是用ChannelInitializer来处理的。
            ChannelFuture channelFuture = bootstrap.connect("localhost",port).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            workGroup.shutdownGracefully();
        }
    }

}
