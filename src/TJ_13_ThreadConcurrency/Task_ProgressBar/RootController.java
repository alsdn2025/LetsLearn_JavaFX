package TJ_13_ThreadConcurrency.Task_ProgressBar;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private Label lblProgress;
    @FXML private Label lblResult;
    @FXML private ProgressBar progressbar;
    @FXML private Button btnStart;
    @FXML private Button btnStop;

    private TimeService timeService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStart.setOnAction(this::handlebtnStartAction);
        btnStop.setOnAction(this::handlebtnStopAction);
        timeService = new TimeService();
        timeService.start();
    }

    public void handlebtnStartAction(ActionEvent event){
        lblResult.setText("진행 중..");

        // 단방향 바인드된 속성을 변경하려면 먼저 언바인드해줘야 한다.
        lblProgress.textProperty().unbind();
        lblProgress.setText("Ready..");

        timeService.restart();

    }


    public void handlebtnStopAction(ActionEvent event){
        timeService.cancel();
    }

    class TimeService extends Service<Integer> {

        @Override
        protected Task<Integer> createTask() {
            Task<Integer> task = new Task() {
                @Override
                protected Integer call(){
                    int result = 0;
                    for (int i =0; i<=100; i++) {
                        if(isCancelled()){break;}
                        result += i;
                        updateProgress(i,100);
                        updateMessage(String.valueOf(i));
                        try{
                            Thread.sleep(100);
                        }catch(InterruptedException e){break;}
                    }
                    return result;
                }
            };
            // 속성 바인딩
            progressbar.progressProperty().bind(task.progressProperty());
            lblProgress.textProperty().bind(task.messageProperty());
            lblResult.setText("작업 중..");
            return task;
        }

        @Override
        protected void succeeded() {
            lblProgress.textProperty().unbind();
            lblProgress.setText("작업 완료");
            lblResult.setText("결과 : " + getValue());
        }
        @Override
        protected void cancelled() {
            lblProgress.textProperty().unbind();
            lblProgress.setText("");
            lblResult.setText("취소됨");
        }
        @Override
        protected void failed() {
            lblProgress.textProperty().unbind();
            lblProgress.setText("");
            lblResult.setText("실패");
        }
    }
}
