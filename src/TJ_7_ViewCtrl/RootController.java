package TJ_7_ViewCtrl;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private ListView<String> listView;
    @FXML private TableView<KeyBoard> tableView;
    @FXML private ImageView imageView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.setItems(FXCollections.observableArrayList(
                "Cherry",
                "Hansung",
                "Leopold",
                "Cox"
        ));

        // 테이블뷰에 아이템들을 넣어준다.
        tableView.setItems(FXCollections.observableArrayList(
                new KeyBoard("MX Board 3.0s","Image0.jpg"),
                new KeyBoard("GK888b","Image1.jpg"),
                new KeyBoard("FC660c", "Image2.jpg"),
                new KeyBoard("Endeavour 50g","Image3.jpg")
        ));

        // 테이블뷰의 Column과, KeyBoard의 속성을 매핑해준다
        TableColumn tcName = tableView.getColumns().get(0);
        tcName.setCellValueFactory( new PropertyValueFactory("name"));
        TableColumn tcImage = tableView.getColumns().get(1);
        tcImage.setCellValueFactory( new PropertyValueFactory("image"));
        tcName.setStyle("-fx-alignment: CENTER"); // css Style


        // 리스트뷰와 테이블뷰를 연동시켜주자. ( 속성감시 )
        listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number oldValue, Number newValue) {
                tableView.getSelectionModel().select(newValue.intValue());
                tableView.scrollTo(newValue.intValue());
            }
        });
        tableView.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
                        listView.getSelectionModel().select(newValue.intValue());
        });

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if(newValue != null){
                        String imagePath = "Images/" +
                                newValue.getImage();
                        imageView.setImage(new Image(getClass().getResource(imagePath).toString()));
                    }
                }
        );
    }

    public void handleBtnOkAction(ActionEvent e){
        if(tableView.getSelectionModel().getSelectedItem() == null) return;
        String lineSeparator = System.lineSeparator();
        ReadOnlyIntegerProperty lvSelectedIndexProp
                = listView.getSelectionModel().selectedIndexProperty();
        ReadOnlyObjectProperty<String> lvSelectedItemProp
                = listView.getSelectionModel().selectedItemProperty();
        ReadOnlyObjectProperty<KeyBoard> tvSelectedItemProp
                = tableView.getSelectionModel().selectedItemProperty();


        StringBuilder sb = new StringBuilder();
        sb.append("Selected product index : ").append(lvSelectedIndexProp.getValue())
                .append(lineSeparator)
                .append("Manufacturer Name : ").append(lvSelectedItemProp.getValue())
                .append(lineSeparator)
                .append("Product Name : ").append(tvSelectedItemProp.getValue().getName())
                .append(lineSeparator)
                .append("Save DateTime : ").append(OffsetDateTime.now(ZoneOffset.of("+9"))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd E a hh:mm x").withLocale(Locale.ENGLISH)));

        System.out.println(sb.toString());

        try{
            String strFilepath =  "C:\\Users\\alsdn\\Desktop\\"
                    + lvSelectedItemProp.getValue() + ".txt";
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(strFilepath,true)
            );
            bw.write(sb.toString());
            bw.flush();
            bw.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
        System.out.println("--Save complete--");
    }

    public void handleBtnCancelAction(ActionEvent e){
        Platform.exit();
    }

}
