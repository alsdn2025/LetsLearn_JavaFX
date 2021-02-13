package Chatting;

import java.net.Socket;

// 한 명의 클라와 통신해주기 위한 클래스
public class Client {

    Socket socket;
    // 생성자로 클라의 소켓을 받는다.
    public Client(Socket socket){
        this.socket = socket;
        receiveMessage();
    }

    // 클라이언트로부터 메세지를 전달받는 메서드
    public void receiveMessage(){
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                // 스레드가 실행할 코드;
            }
        };
    }

    // 클라이언트에게 메세지를 전송하는 메서드
    public void sendMessage(String msg){

    }

}
