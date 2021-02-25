package TJ_ChattingProgram.Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private TextField textField;
    @FXML private TextArea textArea;
    @FXML private Button btnSendMessage;
    @FXML private Button btnConnect;

    Socket socket;

    // connect , read , write 등 모두 UI 스레드가 아닌 작업스레드가 담당하는게 좋다.
    public void logIn(){
        Thread thread = new Thread(()->{
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.10.104",5001));
                Platform.runLater(()-> {
                    printTextArea("서버에 접속했습니다. " + new Date());
                    printTextArea("Server IP : " + socket.getRemoteSocketAddress());
                    btnConnect.setText("Disconnect");
                });

                btnSendMessage.setDisable(false);
                receive();
            } catch (IOException exception) {
                exception.printStackTrace();
                Platform.runLater(()->printTextArea(Thread.currentThread().getName() + " : [서버에 접속할 수 없습니다.]"));
                if(!socket.isClosed()){exitServer();}
            }
        });
        thread.setDaemon(true);
        thread.setName("LogIn Thread");
        thread.start();
    }

    /**
    * @class : RootController.java
    * @modifyed : 2021-02-26 오전 1:56
    * @usage : 루프를 돌며 서버로부터 오는 데이터를 계속 받는다.
    **/
    public void receive() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        InputStream is = socket.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                        char[] data = new char[100];

                        // 소켓이 서버에 의해 정상적으로 close 되었을 경우.
                        if (isr.read(data) == -1) {
                            Platform.runLater(() -> {
                                printTextArea(Thread.currentThread().getName() + " : 서버에 의해 연결이 끊어졌습니다.");
                                if (socket != null && !socket.isClosed()) {
                                    exitServer();
                                }
                            });
                            break;
                        }

                        Platform.runLater(() -> {
                            printTextArea(new String(data));
                        });
                    } catch (Exception e) {
                        // 클라이언트쪽에서 Disconnect 했을 경우
                        if (socket.isClosed()) {
                            Platform.runLater(() -> printTextArea(
                                    Thread.currentThread().getName() + " : 연결이 끊어졌습니다.")
                            );
                            break;
                        } else { // 그 밖에 예외가 발생한 경우( 에러 )
                            System.out.println("예외 발생");
                            System.out.println(e.getMessage());
                            exitServer();
                            break;
                        }
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
        thread.setName("MessageReceiving Thread");
    }

    // UI 관련작업 외에는 작업스레드에게 일을 시키자.
    public void exitServer() {
        Thread thread = new Thread(()->{
            try {
                Platform.runLater(() -> {
                    printTextArea("[연결 종료..]");
                    btnSendMessage.setDisable(true);
                    btnConnect.setText("Connect");
                });
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }catch (Exception e){

            }
        });
        thread.setDaemon(true);
        thread.setName("ExitServer Thread");
        thread.start();
    }

    public void printTextArea(String text){
        this.textArea.appendText(text);
        this.textArea.appendText(System.lineSeparator());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSendMessage.setOnAction(this::handleBtnSendMessageAction);
        btnSendMessage.setDisable(true);
        btnConnect.setText("Connect");
        btnConnect.setOnAction(this::handleBtnConnectAction);
    }

    public void handleBtnConnectAction(ActionEvent event){
        switch (btnConnect.getText()){
            case "Connect" :
                logIn();
                break;
            case "Disconnect":
                exitServer();
                break;
            default:
                printTextArea("Wrong ConnectBtn Name");
                break;
        }
    }
    public void handleBtnSendMessageAction(ActionEvent event){
       send(textField.getText());
       textField.setText("");
    }

    void send(String message){
        Thread thread = new Thread(()->{
            try{
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
                osw.write(message);
                osw.flush();
            }catch (Exception e){
                e.printStackTrace();
                Platform.runLater(()->printTextArea(Thread.currentThread().getName() + " : [서버 통신 오류]"));
                if(!socket.isClosed()) { exitServer(); }
            }
        });
        thread.setDaemon(true);
        thread.setName("MessageSending Thread");
        thread.start();
    }
}
