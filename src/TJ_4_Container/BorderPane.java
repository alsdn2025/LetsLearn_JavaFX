package TJ_4_Container;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BorderPane extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("BorderFXML.fxml"));
        Scene scene = new Scene(parent);

        primaryStage.setTitle("AppMain");
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
