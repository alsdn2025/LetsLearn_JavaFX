package TJ_10_Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void handleNew(ActionEvent event){
        textArea.appendText("New " +
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E a hh:mm x")) +
                System.lineSeparator());

    }

    public void handleOpen(ActionEvent event){
        textArea.appendText("Open " +
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E a hh:mm x")) +
                System.lineSeparator());
    }

    public void handleSave(ActionEvent event){

        textArea.appendText("Save " +
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E a hh:mm x")) +
                System.lineSeparator());
    }

    public void handleExit(ActionEvent event){
        textArea.appendText("Exit " +
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E a hh:mm x")) +
                System.lineSeparator());
    }
}
