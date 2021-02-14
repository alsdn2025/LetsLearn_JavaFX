package TJ_7_ViewCtrl;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.Arrays;


public class AppMain_ViewCtrl extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("root.fxml"));
        Scene scene = new Scene(root);



        primaryStage.setScene(scene);
        primaryStage.setTitle("AppMain");
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);

    }
}
