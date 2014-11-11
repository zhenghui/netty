package zhenghui.netty.jdk.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * User: zhenghui
 * Date:2014/10/27
 * Time:21:45
 * Email:jingbo2759@163.com
 *
 * BIO 服务端处理器
 */
public class TimeServerHandler implements Runnable{

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            byte[] buffer = new byte[128];
            while (inputStream.read(buffer) !=-1){
                String msg = new String(buffer);
                System.out.println(new String(buffer));
                if (msg.startsWith("hello")){
                    outputStream.write("hello client.".getBytes());
                }
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
