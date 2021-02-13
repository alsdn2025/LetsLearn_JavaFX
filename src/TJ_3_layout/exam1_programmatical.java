package TJ_3_layout;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class exam1_programmatical extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(50,30,50,30));
            Button button1 = new Button();
            button1.setPrefSize(100,100); // 버튼 사이즈
            HBox.setMargin(button1,new Insets(30,30,30,30));

            Button button2 =  new Button();
            button2.setPrefSize(100,100);
            HBox.setMargin(button2,new Insets(30,30,30,30));

        hbox.getChildren().add(button1);
        hbox.getChildren().add(button2);

        Scene scene = new Scene(hbox);

        primaryStage.setTitle("AppMain");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}
