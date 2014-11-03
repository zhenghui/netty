package zhenghui.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * User: zhenghui
 * Date: 03/11/14
 * Time: 19:55
 * netty first demo timeclient
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
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("localhost",port).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            workGroup.shutdownGracefully();
        }
    }

}
