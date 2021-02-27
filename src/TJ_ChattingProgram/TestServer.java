package TJ_ChattingProgram;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
* @class : TestServer.java
* @modifyed : 2021-02-28 오전 3:08
* @usage : Test Write/Read method between Client/Server
**/
public class TestServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            Socket socket = serverSocket.accept();
            System.out.println(System.currentTimeMillis() + " 연결 완료");

            Thread.sleep(3000);

            System.out.println(System.currentTimeMillis());

            InputStream is = socket.getInputStream();
            byte[] data = new byte[100];
            int readCount = is.read(data);

            System.out.println("return cnt : " + readCount );
            System.out.println(new String(data));

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }

    }
}
