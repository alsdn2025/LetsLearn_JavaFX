package TJ_ChattingProgram;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
* @class : TestClient.java
* @modifyed : 2021-02-28 오전 3:21
* @usage : Test Write/Read method between Client/Server
**/
public class TestClient {
    public static void main(String[] args) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(5001));
            System.out.println(System.currentTimeMillis() + "연결 완료");
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw =  new OutputStreamWriter(os);
            osw.write("안녕 서버!");
            osw.flush();
            System.out.println(System.currentTimeMillis());

            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
