package zhenghui.netty.basedemo.thirddemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * User: zhenghui
 * Date: 2014年11月5日20:45:42
 * Email:jingbo2759@163.com
 * netty second demo
 *
 * 使用LineBasedDecoder处理半包问题
 * 既然是line based 所以在消息体上肯定需要加上 \r\n 或者 \n
 * 这里可以发现，每次收到消息，我们都需要手动将ByteBuf 转换成String，这个我们可以通过StringDecoder的方式来改善。具体看第四个例子
 * 下一节结束下如何发送序列化对象
 */
public class TimeServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new TimeServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(8001).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
