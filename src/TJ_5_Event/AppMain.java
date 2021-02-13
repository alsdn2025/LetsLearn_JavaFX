package TJ_5_Event;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class AppMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox root = new HBox();
        root.setPrefSize(200,50);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        Button btn1 = new Button("버튼1");
        btn1.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        System.out.println("버튼1 클릭");
                    }
                }
        );
        Button btn2 = new Button("버튼2");
        btn2.setOnMouseClicked(event->{System.out.println("버튼2 클릭");});


        root.getChildren().addAll(btn1,btn2);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("AppMain");
        primaryStage.setOnCloseRequest(event->System.out.println("종료 요청"));

        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);

    }
}
