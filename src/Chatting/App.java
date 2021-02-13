package Chatting;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

public class App extends Application {


    // 다양한 클라들이 접속했을때 쓰레드를 효과적으로 관리하기 위해 쓰레드풀 사용
    public static ExecutorService threadPool;
    // 벡터 : 일종의 배열, 더 쉽게 사용할 수 있음.
    public static Vector<Client> clinets = new Vector<Client>();

    ServerSocket serverSocket;

    //서버를 구동시켜서 클라이언트의 연결을 기다리는 메서드
    public void startServer(String IP, int port){

    }

    // 서버의 작동을 중지시키는 메서드
    public void stopServer(){

    }




    // UI를 생성하고, 실질적으로 프로그램을 동작시키는 메서드드
   @Override
    public void start(Stage primaryStage) {

    }

    // 프로그램의 진입점입니다.
    public static void main(String[] args) {
        launch(args);
    }

}
