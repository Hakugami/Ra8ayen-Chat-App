package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;

import java.net.URL;
import java.util.ResourceBundle;

public class VoiceChatWaitController implements Initializable {
    public Circle profilePic;
    public Label nameLabel;
    public Button cancelCall;

    public Popup popup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancelCall.setOnAction(actionEvent -> {
            System.out.println("Cancel call button clicked");
        });

    }

    public void setPopup(Popup popup) {
        this.popup = popup;
    }
}
