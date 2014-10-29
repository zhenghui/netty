package zhenghui.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * User: zhenghui
 * Date:2014/10/28
 * Time:22:47
 * Email:jingbo2759@163.com
 * nio client
 */
public class TimeClient {

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean stop = false;

    public TimeClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            //连接上以后注册监听SelectionKey.OP_READ事件。
            //这里注意一下，由于我们设置的是非阻塞的，所以可能返回false.就算返回false也不代表连接失败，可以先注册对应
            //的SelectionKey.OP_CONNECT事件，后面再通过java.nio.channels.SocketChannel.finishConnect() 看是否真的连接失败
            //具体可以看 connect的doc
            if(socketChannel.connect(new InetSocketAddress(8001))){
                System.out.println("connect ok");
                socketChannel.register(selector, SelectionKey.OP_READ);
                //然后发送消息
                String msg = "hello server";
                ByteBuffer byteBuffer = ByteBuffer.allocate(msg.length());
                byteBuffer.put(msg.getBytes());
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
            } else {
                socketChannel.register(selector,SelectionKey.OP_CONNECT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proccess(){
        while (!stop){
            try {
                //一秒钟轮询一次
                selector.select(1000);
                for(SelectionKey selectionKey : selector.selectedKeys()){

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TimeClient timeClient = new TimeClient();
        timeClient.proccess();
    }
}
