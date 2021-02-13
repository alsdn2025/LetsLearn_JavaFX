package TJ_7_InputCtrl;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private TextField txtTitle;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> comboPublic;
    @FXML private DatePicker dateExit;
    @FXML private TextArea txtContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void handleBtnEnrollAction(ActionEvent e){
        String title = txtTitle.getText();
        String password = txtPassword.getText();
        String strIsPublic = comboPublic.getValue();
        LocalDate localDate = dateExit.getValue();
        String content = txtContent.getText();

        StringBuilder Builder = new StringBuilder();
        Builder.append("Title : ").append(title).append(System.lineSeparator())
                .append("PassWord : ").append(password).append(System.lineSeparator())
                .append(strIsPublic).append(System.lineSeparator())
                .append(localDate.toString()).append(System.lineSeparator())
                .append("Content------------------------").append(System.lineSeparator())
                .append(content);

        try {
            Path filePath = Paths.get("C:\\Users\\alsdn\\Desktop\\Text\\"+title +".txt");
            BufferedWriter fw = new BufferedWriter(
                    new FileWriter(filePath.toString(), true));
            fw.write(Builder.toString());
            fw.flush(); // 버퍼에 있는 내용을 내보내는 메서드
            fw.close(); // 버퍼 닫기
        }catch (IOException exception){
            exception.printStackTrace();
        }

    }

    public void handleBtnCancelAction(ActionEvent e){
        Platform.exit();
    }

}
