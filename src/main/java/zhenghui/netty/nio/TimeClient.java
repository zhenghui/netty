package zhenghui.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

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
                sendMessage(socketChannel);
            } else {
                socketChannel.register(selector,SelectionKey.OP_CONNECT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(SocketChannel socketChannel) throws IOException {
        String msg = "hello server";
        ByteBuffer byteBuffer = ByteBuffer.allocate(msg.length());
        byteBuffer.put(msg.getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }

    private void proccess(){
        while (!stop){
            try {
                //一秒钟轮询一次
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                for(SelectionKey selectionKey : selectionKeySet){
                    handleSelectionKey(selectionKey);
                }
                selectionKeySet.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void handleSelectionKey(SelectionKey selectionKey) throws Exception {
        if(selectionKey.isValid()){
            SocketChannel socketChannel2 = (SocketChannel) selectionKey.channel();
            if (selectionKey.isConnectable()){
                //上面已经解释过，由于是异步的，所以在 connect的时候不一定马上能连接成功。
                if(socketChannel2.finishConnect()){
                    socketChannel2.register(selector,SelectionKey.OP_READ);
                    sendMessage(socketChannel2);
                } else {
                    //连接失败，直接结束
                    System.exit(1);
                }
            } else if(selectionKey.isReadable()){
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int size = socketChannel2.read(byteBuffer);
                if(size > 0){
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    System.out.println(new String(bytes));
//                    this.stop = true;
                } else if(size < 0){
                    selectionKey.cancel();
                    socketChannel2.close();
                }
            }
        }
    }

    public static void main(String[] args) {
        TimeClient timeClient = new TimeClient();
        timeClient.proccess();
    }
}
