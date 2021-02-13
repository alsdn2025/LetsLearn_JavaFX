package TJ_6_Bindings;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private AnchorPane anchorPane;
    @FXML private Circle circle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        circle.centerXProperty()
                .bind(Bindings.divide(anchorPane.widthProperty(),2));
        circle.centerYProperty()
                .bind(Bindings.divide(anchorPane.heightProperty(),2));

    }
}
