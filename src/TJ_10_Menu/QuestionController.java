package TJ_10_Menu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleAction;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class QuestionController implements Initializable {
    private Stage Dialog;
    @FXML private Button btnQuestionOk;
    @FXML private Label lblQuestionText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblQuestionText.setText("Sorry." + System.lineSeparator() + "You Can't Question Now");
        btnQuestionOk.setOnAction(this::handleBtnOkAction);

    }

    public void handleBtnOkAction(ActionEvent event){
        Dialog.close();
    }

    public void setDialog(Stage Dialog) {
        this.Dialog = Dialog;
    }
}
