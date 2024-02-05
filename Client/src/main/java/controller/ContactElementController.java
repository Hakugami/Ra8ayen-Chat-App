package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;

public class ContactElementController {
    public Circle profilePicCircle;
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
        if (ImagId != null && ImagId.getImage() != null) {
            profilePicCircle.setFill(new ImagePattern(ImagId.getImage()));
        }
    }
    public void setName(String name) {
        Name.setText(name);
    }

    public void setStatus(Color color) {
        if(color == Color.GREEN){
            status.getStyleClass().add("online-status");
        }
        else if(color == Color.RED){
            status.getStyleClass().add("busy-status");
        }
        else if(color == Color.YELLOW){
            status.getStyleClass().add("away-status");
        }
        else if(color == Color.GRAY){
            status.getStyleClass().add("offline-status");
        }
        else if(color == Color.PURPLE){
            status.setVisible(false);
        }
    }
    public void setImagId(Image image) {
        profilePicCircle.setFill(new ImagePattern(image));
        ImagId.setImage(image);
    }

    public int getChatID() {
        return Integer.parseInt(chatID.getText());
    }

    public void setChatID(int chatID) {
        this.chatID.setText(String.valueOf(chatID));
    }
}
