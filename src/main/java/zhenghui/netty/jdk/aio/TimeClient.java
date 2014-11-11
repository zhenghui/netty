package zhenghui.netty.jdk.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * User: zhenghui
 * Date:2014/10/31
 * Time:21:45
 * Email:jingbo2759@163.com
 * aio client
 */
public class TimeClient {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public TimeClient() {
        try {
            asynchronousSocketChannel = AsynchronousSocketChannel.open();
            asynchronousSocketChannel.connect(new InetSocketAddress("127.0.0.1",8001),this,new CompletionHandler<Void, TimeClient>() {
                @Override
                public void completed(Void result,final TimeClient attachment) {
                    String msg = "hello server";
                    final ByteBuffer byteBuffer = ByteBuffer.allocate(msg.length());
                    byteBuffer.put(msg.getBytes());
                    byteBuffer.flip();
                    attachment.asynchronousSocketChannel.write(byteBuffer,byteBuffer,new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer byteBuffer2) {
                            //如果还没发送完，继续发送
                            if(byteBuffer2.hasRemaining()){
                                attachment.asynchronousSocketChannel.write(byteBuffer2);
                            } else {
                                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                                attachment.asynchronousSocketChannel.read(readBuffer,readBuffer,new CompletionHandler<Integer, ByteBuffer>() {
                                    @Override
                                    public void completed(Integer result, ByteBuffer readBuffer) {
                                        readBuffer.flip();
                                        byte[] bytes = new byte[readBuffer.remaining()];
                                        readBuffer.get(bytes);
                                        System.out.println(new String(bytes));
                                        attachment.countDownLatch.countDown();
                                    }

                                    @Override
                                    public void failed(Throwable exc, ByteBuffer byteBuffer1) {
                                        try {
                                            attachment.asynchronousSocketChannel.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        attachment.countDownLatch.countDown();
                                        exc.printStackTrace();
                                    }
                                });
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer byteBuffer3) {
                            try {
                                attachment.asynchronousSocketChannel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            attachment.countDownLatch.countDown();
                            exc.printStackTrace();
                        }
                    });
                }

                @Override
                public void failed(Throwable exc, TimeClient attachment) {
                    try {
                        attachment.asynchronousSocketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    attachment.countDownLatch.countDown();
                    exc.printStackTrace();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TimeClient();
    }
}
