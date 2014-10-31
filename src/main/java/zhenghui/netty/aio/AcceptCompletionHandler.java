package zhenghui.netty.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * User: zhenghui
 * Date:2014/10/30
 * Time:21:43
 * Email:jingbo2759@163.com
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,TimeServer> {
    @Override
    public void completed(final AsynchronousSocketChannel result, TimeServer attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment,this);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        result.read(byteBuffer,byteBuffer,new CompletionHandler<Integer,ByteBuffer>() {

            private AsynchronousSocketChannel asynchronousSocketChannel;

            {
                if(asynchronousSocketChannel == null){
                    asynchronousSocketChannel = result;
                }
            }

            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                attachment.flip();
                byte[] bytes = new byte[attachment.remaining()];
                attachment.get(bytes);
                System.out.println(new String(bytes));
                //发送消息
                doWrite();
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    asynchronousSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exc.printStackTrace();
            }

            private void doWrite(){
                String msg = "hello client";
                ByteBuffer whriteBuffer = ByteBuffer.allocate(msg.getBytes().length);
                whriteBuffer.put(msg.getBytes());
                whriteBuffer.flip();
                asynchronousSocketChannel.write(whriteBuffer,whriteBuffer,new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        //如果没发送完成，继续发送
                        if(attachment.hasRemaining()){
                            asynchronousSocketChannel.write(attachment);
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        try {
                            asynchronousSocketChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void failed(Throwable exc, TimeServer attachment) {
        attachment.countDownLatch.countDown();
        exc.printStackTrace();
    }
}
