package TJ_ChattingProgram;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class RootController implements Initializable {
    @FXML Button btnStartEnd;
    @FXML TextArea textArea;
    @FXML TextField textField;

    ExecutorService executorService;
    ServerSocket serverSocket;
    List<Client> connections = new Vector<>(); // 스레드동시성을 보장하는 Vector

    class Client{
        private final Socket socket;
        public Client(Socket socket){
            this.socket = socket;
            this.receive();
        }

        private void receive() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            byte[] byteArr = new byte[100];
                            InputStream is = socket.getInputStream();
                            int readByteCount = is.read(byteArr);

                            // 만약 정상적으로 소켓이 close 되었다면
                            if(readByteCount == -1){
                                    Platform.runLater(() -> {
                                        displayText("[" + socket.getRemoteSocketAddress() + " 로부터의 연결이 끊어졌습니다]");
                                        connections.remove(Client.this);
                                        if(!socket.isClosed())
                                            try{socket.close();}catch (IOException e){}
                                    });
                            }

                            String data = new String(byteArr,0,readByteCount, StandardCharsets.UTF_8);

                            Platform.runLater(()->displayText(
                                    "[요청 처리 : " + socket.getRemoteSocketAddress()+"] " + Thread.currentThread().getName()
                            ));
                            Platform.runLater(()->displayText(
                                    "[내용 : " + data + "]"
                            ));

                            String message = "[" + socket.getRemoteSocketAddress() +"] : " + data;
                            for(Client c : connections){
                                c.send(message);
                            }
                        }
                    }catch(IOException e){
                        try {
                            // 클라이언트가 비정상적으로 종료시, read 메서드에서 예외 발생
                            // 클라이언트가 정상적으로 종료시에도 readByteCount == -1이므로 예외 발생
                            connections.remove(Client.this);
                            String message = "[클라이언트 통신 오류 : " + socket.getRemoteSocketAddress() + "]" + Thread.currentThread().getName();
                            Platform.runLater(() -> displayText(message));
                            socket.close();
                            e.printStackTrace();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            };
            executorService.submit(runnable);
        }

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
                    btnStartEnd.setText("Close Server");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });

            while(true){
                try {
                    Socket socket = serverSocket.accept();
                    String message = Thread.currentThread().getName() +
                            " : [연결 수락 : " + socket.getRemoteSocketAddress() +"] " + new Date();
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
    }


    void displayText(String text){
        this.textArea.appendText(text);
        this.textArea.appendText(System.lineSeparator());
    }

    void closeServer(){
        try {
//            Iterator<Client> iterator = connections.iterator();
//            while (iterator.hasNext()) {
//                Client client = iterator.next();
//                client.socket.close();
//                iterator.remove();
//            }

            for(var c : connections){
                c.socket.close();
                connections.remove(c);
            }

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

}
