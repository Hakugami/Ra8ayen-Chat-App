package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;

public class ContactElementController {
    @FXML
    Label Name;
    @FXML
    Circle status;
    @FXML
    ImageView ImagId;
    @FXML
    Circle imageClip;

    @FXML
    Label chatID;

    public void initialize() {
        // Set the initial size of the ImageView to match the circle
        ImagId.setFitWidth(imageClip.getRadius() * 2);
        ImagId.setFitHeight(imageClip.getRadius() * 2);
    }
    public void setName(String name) {
        Name.setText(name);
    }

    public void setStatus(Color color) {
        status.setFill(color);
    }
    public void setImagId(Image image) {
        ImagId.setImage(image);
    }

    public int getChatID() {
        return Integer.parseInt(chatID.getText());
    }

    public void setChatID(int chatID) {
        this.chatID.setText(String.valueOf(chatID));
    }
}
