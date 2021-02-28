package TJ_ChattingProgram;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;


/**
* @class : RootController.java
* @modifyed : 2021-02-27 오후 7:39
* @usage : 서버쪽 컨트롤러
**/
public class RootController implements Initializable {
    @FXML Button btnStartEnd;
    @FXML TextFlow textFlow;
    @FXML TextField textField;
    @FXML Button btnSendMessage;

    ExecutorService executorService;
    ServerSocket serverSocket;
    List<Client> connections = new Vector<>(); // 스레드동시성을 보장하는 Vector


    class Client{
        private final Socket socket;
        public Client(Socket socket){
            this.socket = socket;
//            this.receive();
        }

//        private void receive() {
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        while (true) {
//                            byte[] byteArr = new byte[100];
//                            InputStream is = socket.getInputStream();
//                            int readByteCount = is.read(byteArr);
//
//                            // 만약 정상적으로 소켓이 close 되었다면
//                            if (readByteCount == -1) {
//                                Platform.runLater(() -> {
//                                    displayText("[" + socket.getRemoteSocketAddress() + " 로부터의 연결이 끊어졌습니다]");
//                                });
//                                connections.remove(Client.this);
//                                if (!socket.isClosed()) try { socket.close(); } catch (IOException e) { }
//                                break;
//                            }
//
//                            String data = new String(byteArr, 0, readByteCount, StandardCharsets.UTF_8);
//
//                            Platform.runLater(()->displayText("[요청 처리 : " + socket.getRemoteSocketAddress()+"]"));
//                            Platform.runLater(()->displayText("[내용 : " + data + "]"));
//
//                            String message = "[" + socket.getRemoteSocketAddress() +"] : " + data;
//                            for(Client c : connections){
//                                c.send(message);
//                            }
//                        }
//                    }catch(IOException e){
//                        try {
//                            if(socket.isClosed()){
//                                // 서버가 닫혀서 예외 발생시
//                                Platform.runLater(()->displayText("[" + socket.getRemoteSocketAddress() + "과의 연결이 끊어졌습니다.]"));
//                            }
//                            else {
//                                // 비정상적인 예외 발생시
//                                String message = "[클라이언트 통신 오류 : " + socket.getRemoteSocketAddress() + "]" + Thread.currentThread().getName();
//                                Platform.runLater(() -> displayText(message));
//                                socket.close();
//                                e.printStackTrace();
//                            }
//                            connections.remove(Client.this);
//                        } catch (IOException exception) {
//                            exception.printStackTrace();
//                        }
//                    }
//                }
//            };
//            executorService.submit(runnable);
//        }

        private void send(String data){
            Runnable runnable = ()->{
                try{
                    byte[] byteArr = data.getBytes(StandardCharsets.UTF_8);
                    OutputStream os = socket.getOutputStream();
                    os.write(byteArr);
                    os.flush();
//                    os.close(); 스트림을 닫으면 연결도 끊긴다.

                }catch (Exception e){
                    try {
                        String message = "[클라이언트 통신 오류 : " + socket.getRemoteSocketAddress() + "]" + Thread.currentThread().getName();
                        Platform.runLater(() -> displayText(message));
                        connections.remove(Client.this);
                        socket.close();
                    } catch (IOException exception) { }
                }
            };
            executorService.submit(runnable);
        }
    }

    void receive(){
        Runnable confirmRequestTask = ()->{
            while(true) {
                try {
                    Iterator<Client> iterator = connections.iterator();
                    // Client 컬렉션을 하나씩 돌면서 작업 요청이 있었는지를 확인한다.
                    // read 시 일정시간이 지나면 다음 Client 로 넘어간다.
                    while (iterator.hasNext()) {
                        Socket socket = null;
                        Client client = iterator.next(); // 여기서 동시성 예외 발생 가능
                        socket = client.socket;
                        socket.setSoTimeout(100);
                        InputStream is = client.socket.getInputStream();
                        byte[] byteArr = new byte[100];

                        // 이렇게 만들면, 만약 클라이언트쪽에서 작업요청시
                        // 이 스레드가 해당 클라이언트를 소켓을 read 하고있지 않았다면
                        // 그 작업은 무시된다.
                        int readByteCount = 0;
                        try {
                            readByteCount = is.read(byteArr);
                        } catch (SocketTimeoutException e) {
                            continue; // 일정시간이 지나면 루프의 처음으로 돌아가 다음 client 의 요청이 있었는지 확인한다.
                        } catch (Exception e) {
                            if (client.socket.isClosed()) {
                                Platform.runLater(() -> displayText("[" + client.socket.getRemoteSocketAddress() + "과의 연결이 끊어졌습니다.]"));
                            } else {
                                String message = "[클라이언트 통신 오류 : " + client.socket.getRemoteSocketAddress() + "]" + Thread.currentThread().getName();
                                Platform.runLater(() -> displayText(message));
                                client.socket.close();
                                e.printStackTrace();
                            }
                            connections.remove(client);
                            continue;
                        }

                        // 만약 정상적으로 소켓이 close 되어 -1을 리턴했다면
                        if (readByteCount == -1) {
                            Platform.runLater(() -> {
                                displayText("[" + client.socket.getRemoteSocketAddress() + " 로부터의 연결이 끊어졌습니다]");
                            });
                            if (!client.socket.isClosed()) try {
                                client.socket.close();
                            } catch (IOException e) { }
                            connections.remove(client);
                            continue; // 루프의 처음으로 돌아간다.
                        }

                        // 실제로 받은 작업( 받은 문자열을 개체로 만들고 출력 + 각 클라이언트들에게 send )
                        // 은 스레드풀에 submit 하여 다른 스레드에게 맡긴다.
                        final int readByteCntImmutable = readByteCount;
                        Runnable requestedTask = () -> {
                            String data = new String(byteArr, 0, readByteCntImmutable, StandardCharsets.UTF_8);
                            Platform.runLater(() -> displayText("[요청 처리 : " + client.socket.getRemoteSocketAddress() + "]"));
                            Platform.runLater(() -> displayText("[내용 : " + data + "]"));

                            String message = "[" + client.socket.getRemoteSocketAddress() + "] : " + data;
                            for (Client c : connections) {
                                c.send(message);
                            }
                        };
                        executorService.submit(requestedTask);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch(ConcurrentModificationException e){
                    // iterator 가 내부 반복중( client = iterator.next )에 connections 에 조작이 있을 경우
                    // 다시 루프의 처음으로 돌아간다.
                    System.out.println("ConcurrentModificationException Occur");
                }
            }
        };
        executorService.submit(confirmRequestTask);
    }

    void startServer(){
//        this.executorService = Executors.newFixedThreadPool(
//                Runtime.getRuntime().availableProcessors()
//        );

        // 스레드풀의 모든 스레드들을 데몬스레드로 만들어, application 이 종료되면 스레드풀도 종료
        executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = Executors.defaultThreadFactory().newThread(r);
                        thread.setDaemon(true);
                        return thread;
                    }
                }
        );

        try{
            this.serverSocket = new ServerSocket(5001);
        }catch (Exception e){
            displayText(Thread.currentThread().getName() + " : ServerSocket을 만드는 도중 예외가 발생했습니다.");
            if(!serverSocket.isClosed()){ closeServer(); }
            return;
        }

        Runnable runnable = () -> {
            // UI 변경작업은 무조건 javaApplication Thread 가 담당!!
            Platform.runLater(() -> {
                try {
                    displayText("[서버가 열렸습니다] " + new Date());
                    displayText("[서버 IP : " + InetAddress.getLocalHost().getHostAddress() + ":" + serverSocket.getLocalPort() + "]");
                    displayText("--------------------------------------------------------------------");
                    btnStartEnd.setText("Close Server");
                    btnSendMessage.setDisable(false);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });

            while(true){
                try {
                    Socket socket = serverSocket.accept();
                    String message = " : [연결 수락 : " + socket.getRemoteSocketAddress() +"] " + new Date();
                    Platform.runLater(()->{
                        displayText(message);
                    });

                    Client client = new Client(socket);
                    connections.add(client);
                    Platform.runLater(()->{
                        displayText("[연결된 클라이언트 수 : " + connections.size() + " ]");
                    });

                } catch (IOException exception) {
                    if(!serverSocket.isClosed()){
                        this.closeServer();
                    }
                    break;
                }
            }
        };
        executorService.submit(runnable);
        receive();
    }

    void displayText(String message){
        Text text = new Text(message + System.lineSeparator());
        this.textFlow.getChildren().add(text);
    }

    void clearTextBoard(){
        this.textFlow.getChildren().clear();
    }

    void closeServer(){
        try {
            Iterator<Client> iterator = connections.iterator();
            while (iterator.hasNext()) {
                Client client = iterator.next();
                client.socket.close();
                iterator.remove();
                System.out.println(Thread.currentThread().getName() + " : CloseServer ");
            }

            // 아래 처럼 향상된 for 루프 사용시 동시성예외가 발생한다.
//            for(var c : connections){
//                c.socket.close();
//                connections.remove(c);
//                System.out.println(Thread.currentThread().getName() + " : CloseServer ");
//            }

            if(serverSocket!=null && !serverSocket.isClosed()){
                serverSocket.close();
            }
            if(executorService != null && !executorService.isShutdown()){
                executorService.shutdown();
            }
            Platform.runLater(()->{
                displayText("[서버가 닫혔습니다]" + new Date());
                btnStartEnd.setText("Start Server");
            });

        } catch (IOException exception) {
            System.out.println("[서버를 닫는 중 예외가 발생했습니다]");
            exception.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStartEnd.setText("Start Server");
        btnStartEnd.setOnAction(this::handleBtnStartEndClicked);
        btnSendMessage.setOnAction(this::handleBtnSendAction);
        btnSendMessage.setDisable(true);
    }

    public void handleBtnStartEndClicked(ActionEvent event){
        switch (this.btnStartEnd.getText()){
            case "Start Server" :
                this.startServer();
                break;
            case "Close Server" :
                this.closeServer();
                break;
            default:
                displayText("Wrong Btn Name.");
                break;
        }
    }

    void handleBtnSendAction(ActionEvent event){
        Runnable runnable = ()->{
          try{
              if(connections.size() != 0) {
                  String message = "[서버] : " + textField.getText();
                  Platform.runLater(() -> {
                      displayText(message);
                      displayText("[메세지를 보냈습니다]");
                      textField.setText("");
                  });
                  for (Client c : connections) {
                      c.send(message);
                  }
              }
              else{
                  Platform.runLater(()->{
                      displayText("[서버에 접속한 클라이언트가 없습니다.]");
                      textField.setText("");
                  });
              }
          }catch (Exception e){
              e.printStackTrace();
          }
        };
        executorService.submit(runnable);

    }
}
