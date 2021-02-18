package TJ_10_Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private TextArea textArea;
    private Stage primaryStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void handleNew(ActionEvent event){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDir = directoryChooser.showDialog(primaryStage);
        if(selectedDir != null) System.out.println(selectedDir.getPath());

        try{popup();}catch (Exception e){};
        textArea.appendText("New " +
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E a hh:mm x")) +
                System.lineSeparator());
    }

    public void handleOpen(ActionEvent event){
        textArea.appendText("Open " +
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E a hh:mm x")) +
                System.lineSeparator());

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files","*.txt"),
                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.gif"),
                new FileChooser.ExtensionFilter("Audio Files","*.wav","*.mp3","*.aac"),
                new FileChooser.ExtensionFilter("All Files","*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(textArea.getScene().getWindow());
        if(selectedFile != null) System.out.println(selectedFile.getPath());

    }

    public void handleSave(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files","*.txt")
        );
        File selectedFile = fileChooser.showSaveDialog(primaryStage);
        if(selectedFile != null) System.out.println(selectedFile.getPath());


        textArea.appendText("Save " +
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E a hh:mm x")) +
                System.lineSeparator());
    }

    public void handleExit(ActionEvent event){
        textArea.appendText("Exit " +
                OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E a hh:mm x")) +
                System.lineSeparator());
    }

    public void handleQuestion(){
        try {
            customDialogUp();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void popup() throws IOException {
        Popup popup = new Popup();
        Parent parent = FXMLLoader.load(getClass().getResource("popup.fxml"));

        ImageView imageview = (ImageView) parent.lookup("#imgViewPopup");
        imageview.setImage(new Image(
                getClass().getResource("Icons/imgPopup_Message.jpg").toString()));
        imageview.setOnMouseClicked(event -> popup.hide());

        Label lblPopupMessage = (Label)parent.lookup("#lblPopup");
        lblPopupMessage.setText("메세지가 왔습니다.");

        popup.getContent().add(parent);
        popup.setAutoHide(true);
        popup.show(primaryStage);
    }

    public void customDialogUp() throws IOException {
        Stage customDialog = new Stage(StageStyle.UTILITY);
        customDialog.initModality(Modality.WINDOW_MODAL); // 모달형 다이얼로그.
        customDialog.initOwner(primaryStage);
        customDialog.setTitle("Question?");

/*
         다른 루트컨테이너의 컨트롤은, 해당 루트컨테이너의 컨트롤러에게 맡겨라.
*/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Question.fxml"));
        Parent parent = loader.load();
        QuestionController questionController = loader.getController();
        questionController.setDialog(customDialog);

//        Label lblQuestionText = (Label) parent.lookup("#lblQuestionText");
//        lblQuestionText.setText("Sorry." + System.lineSeparator() + "You Can't Question Now");
//        Button btnQuestionOk = (Button) parent.lookup("#btnQuestionOk");
//        btnQuestionOk.setOnAction(event -> customDialog.close());

        Scene scene = new Scene(parent);
        customDialog.setScene(scene);
        customDialog.setResizable(false);
        customDialog.show();
    }

}
