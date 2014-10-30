package zhenghui.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * User: zhenghui
 * Date:2014/10/28
 * Time:20:51
 * Email:jingbo2759@163.com
 * NIO 服务端
 */
public class TimeServer {

    private Selector selector;

    private volatile boolean stop = false;

    public TimeServer(int port) {
        try {

            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            //serverSocketChannel初始化完成以后，把自己注册到selector上去，监听 SelectionKey.OP_ACCEPT 事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        while (!stop) {
            try {
                //一秒钟轮询一次
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeySet) {
                    try {
                        handleSelectionKey(selectionKey);
                    } catch (Exception e) {
                        if(selectionKey != null){
                            selectionKey.cancel();
                        }
                        e.printStackTrace();
                    }
                }
                //处理完了记得清除
                selectionKeySet.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //停止以后，关闭selector
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSelectionKey(SelectionKey selectionKey) throws Exception{
        //先判断selectionKey是否有效
        if(selectionKey.isValid()){
            //如果是新的连接请求,拿到对应的SocketChannel，然后把自己注册上去，并监听赌赢的SelectionKey.OP_READ 事件
            if(selectionKey.isAcceptable()){
                ServerSocketChannel serverSocketChannel2 = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = serverSocketChannel2.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector,SelectionKey.OP_READ);
            } else if(selectionKey.isReadable()){
                //如果是新的read请求,读取对应的消息，然后给客户端发送一个消息
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int size = socketChannel.read(byteBuffer);
                if(size > 0){
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    System.out.println(new String(bytes));
                    //给客户端发送一个消息
                    String msg = "hello client";
                    ByteBuffer writeByteBuffer = ByteBuffer.allocate(msg.length());
                    writeByteBuffer.put(msg.getBytes());
                    writeByteBuffer.flip();
                    socketChannel.write(writeByteBuffer);
                } else if(size < 0) {
                    selectionKey.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    public static void main(String[] args) {
        int port = 8001;
        TimeServer timeServer = new TimeServer(port);
        timeServer.listen();
    }
}
