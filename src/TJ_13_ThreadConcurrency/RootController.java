package TJ_13_ThreadConcurrency;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private Label lblTime;
    @FXML private Button btnStart;
    @FXML private Button btnStop;

    private boolean stop;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStart.setOnAction(this::handlebtnStartAction);
        btnStop.setOnAction(this::handlebtnStopAction);
    }

    public void handlebtnStartAction(ActionEvent event){
        this.stop = false;
        Thread thread = new Thread(()->{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            while(!stop){
                String time = simpleDateFormat.format(new Date());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        lblTime.setText(time);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void handlebtnStopAction(ActionEvent event){
        this.stop = true;
    }

}
