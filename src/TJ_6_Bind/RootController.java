package TJ_6_Bind;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private TextArea textArea1;
    @FXML private TextArea textArea2;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea1.textProperty().bind(textArea2.textProperty());
//        Bindings.bindBidirectional(textArea1.textProperty(),textArea2.textProperty());
        Bindings.unbindBidirectional(textArea1.textProperty(), textArea2.textProperty());
    }
}
