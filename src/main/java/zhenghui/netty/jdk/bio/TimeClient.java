package zhenghui.netty.jdk.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * User: zhenghui
 * Date:2014/10/27
 * Time:22:02
 * Email:jingbo2759@163.com
 */
public class TimeClient {

    public static void main(String[] args) throws Exception {
        int port = 9000;
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            socket = new Socket("localhost",port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            String msg = "hello server";
            outputStream.write(msg.getBytes());
            outputStream.flush();
            byte[] buffer = new byte[128];
            if (inputStream.read(buffer) !=-1){
                System.out.println(new String(buffer));
            }
            msg = "nihao server";
            outputStream.write(msg.getBytes());
            outputStream.flush();
            if (inputStream.read(buffer) !=-1){
                System.out.println(new String(buffer));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
            }
            if(inputStream != null){
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
        }
    }
}
