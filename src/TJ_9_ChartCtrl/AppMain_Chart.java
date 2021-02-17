package TJ_9_ChartCtrl;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class AppMain_Chart extends Application {
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
