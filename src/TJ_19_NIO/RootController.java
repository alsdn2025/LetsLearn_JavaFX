package TJ_19_NIO;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.ResourceBundle;

/**
* @class : RootController.java
* @modifyed : 2021-03-02 오전 3:22
* @usage : WatchService로 디렉토리를 감시하는 방법을 학습하자~~!
**/
public class RootController implements Initializable {
    @FXML private TextFlow textFlow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        watchStart();
    }

    public void watchStart(){
        Runnable watchRunnable = ()->{
            try{
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path tempPath = Paths.get("C:/Temp");
                tempPath.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY
                );

                while(true){
                    WatchKey watchKey = watchService.take();
                    Platform.runLater(()->appendText(watchKey.toString() + "-------------"));
                    List<WatchEvent<?>> list = watchKey.pollEvents();
                    System.out.println("WatchEvent list size : " + list.size()); // 한꺼번에 삭제해도 이벤트는 하나씩 poll 된다.
                    for(WatchEvent watchEvent : list){
                        // 이벤트 종류 얻기
                        WatchEvent.Kind kind = watchEvent.kind();

                        // 이벤트가 발생한 경로 얻기
                        Path path = (Path)watchEvent.context();
                        if(kind == StandardWatchEventKinds.ENTRY_CREATE){
                            Platform.runLater(()->appendText("파일 생성됨"));
                            Platform.runLater(()->appendText("경로 : " + path));
                        }
                        else if(kind == StandardWatchEventKinds.ENTRY_DELETE){
                            Platform.runLater(()->appendText("파일 삭제됨"));
                            Platform.runLater(()->appendText("경로 : " + path));
                        }
                        else if(kind == StandardWatchEventKinds.ENTRY_MODIFY){
                            Platform.runLater(()->appendText("파일 수정됨"));
                            Platform.runLater(()->appendText("경로 : " + path));
                        }else if(kind == StandardWatchEventKinds.OVERFLOW){
                            Platform.runLater(()->appendText("오버플로우"));
                        }
                        Platform.runLater(()->appendText(""));
                    }
                    if(!watchKey.reset()){ break; }
                }
            }catch (Exception e){ e.printStackTrace(); }
        };

        Thread watchServiceThread = new Thread(watchRunnable);
        watchServiceThread.setDaemon(true);
        watchServiceThread.start();
    }

    public void appendText(String string){
        Text text = new Text(string + System.lineSeparator());
        this.textFlow.getChildren().add(text);
    }
}
