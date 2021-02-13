package Practice_tutorial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Label label = new Label("This is JavaFX!");
        Label leftLabel = new Label("Left");
        Label RightLabel = new Label("Right");

        BorderPane pane = new BorderPane();
        pane.setCenter(label);
        pane.setLeft(leftLabel);
        pane.setRight(RightLabel);

        Scene scene = new Scene(pane, 200, 200);

        primaryStage.setScene(scene);
        primaryStage.show();

        FlowPane flowPane = new FlowPane();
        Label flowlabel = new Label("Testing Flow Pane!");
        flowPane.getChildren().add(flowlabel);
        scene = new Scene(flowPane,300,200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
