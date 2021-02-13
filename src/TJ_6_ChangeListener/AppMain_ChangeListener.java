package TJ_6_ChangeListener;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AppMain_ChangeListener extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("root.fxml"));
        Scene scene = new Scene(root);



        primaryStage.setScene(scene);
        primaryStage.setTitle("AppMain2_fxmlCtr");
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);

    }
}
