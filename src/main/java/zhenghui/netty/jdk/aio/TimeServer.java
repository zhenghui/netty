package zhenghui.netty.jdk.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * User: zhenghui
 * Date:2014/10/30
 * Time:21:23
 * Email:jingbo2759@163.com
 * aio 服务端
 */
public class TimeServer {

    CountDownLatch countDownLatch = new CountDownLatch(1);

    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public TimeServer(){
        int port = 8001;

        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doAccept(){
        asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TimeServer timeServer = new TimeServer();

        timeServer.doAccept();
    }
}
