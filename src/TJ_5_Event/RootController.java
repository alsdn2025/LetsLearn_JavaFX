package TJ_5_Event;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private Button btn1;
    @FXML private Button btn2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn1.setOnMouseClicked((event)->System.out.println("버튼1 클릭!"));
        btn2.setOnMouseClicked((event)-> handleBtn2Clicked());

    }

    public void handleBtn2Clicked(){
        System.out.println("버튼2 클릮!");
    }

    public void handleBtn3Clicked(){
        System.out.println("버튼3 클릮!");
    }
}
