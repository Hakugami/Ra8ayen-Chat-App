package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;

public class ContactElementController {
    public Circle profilePicCircle;
    public Label lastMessage;
    public Circle notifyCircle;
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
    private final StringProperty lastMessageProperty = new SimpleStringProperty("");


    public void initialize() {
        if (ImagId != null && ImagId.getImage() != null) {
            profilePicCircle.setFill(new ImagePattern(ImagId.getImage()));
        }
        lastMessageProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                lastMessage.setText(newValue);
            }
        });

        double shiftAmount = 50; // Change this to the amount you want to shift to the left
        double currentLayoutX = profilePicCircle.getLayoutX();
        profilePicCircle.setLayoutX(currentLayoutX - shiftAmount);

        Name.setPadding(new Insets(0, 0, 0, -shiftAmount)); // Shift to the left by adding negative padding
        lastMessage.setPadding(new Insets(0, 0, 0, -shiftAmount-10)); // Shift to the left by adding negative padding

        double statusCircleLayoutX = status.getLayoutX();
        status.setLayoutX(statusCircleLayoutX - shiftAmount);
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

    public void setLastMessage(String message) {
        lastMessage.setText(message);
    }

    public void setNotifyCircle(boolean notify) {
        notifyCircle.setVisible(notify);
    }

    public int getChatID() {
        return Integer.parseInt(chatID.getText());
    }

    public void setChatID(int chatID) {
        this.chatID.setText(String.valueOf(chatID));
    }

    public StringProperty lastMessageProperty() {
        return lastMessageProperty;
    }

    public String getLastMessage() {
        return lastMessageProperty.get();
    }

    public void setLastMessageProperty(String lastMessage) {
        lastMessageProperty.set(lastMessage);
    }

    public void setLastMessageLabel(String lastMessage) {
        System.out.println("Setting last message to: " + lastMessage);
        Platform.runLater(()->lastMessageProperty.set(lastMessage));
    }

}
