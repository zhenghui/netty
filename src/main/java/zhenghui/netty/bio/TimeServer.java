package zhenghui.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * User: zhenghui
 * Date:2014/10/27
 * Time:21:43
 * Email:jingbo2759@163.com
 * BIO 服务端
 */
public class TimeServer {

    public static void main(String[] args) throws Exception{
        int port = 9000;
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                socket = serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null){
                serverSocket.close();
            }
            if(socket != null){
                socket.close();
            }
        }
    }
}
