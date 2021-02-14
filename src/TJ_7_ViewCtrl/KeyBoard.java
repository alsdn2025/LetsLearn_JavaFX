package TJ_7_ViewCtrl;

import javafx.beans.property.SimpleStringProperty;

public class KeyBoard {
    private SimpleStringProperty name;
    private SimpleStringProperty image;

    public KeyBoard(String name, String image){
        this.name = new SimpleStringProperty(name);
        this.image = new SimpleStringProperty(image);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getImage() {
        return image.get();
    }

    public SimpleStringProperty imageProperty() {
        return image;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setImage(String image) {
        this.image.set(image);
    }
}
