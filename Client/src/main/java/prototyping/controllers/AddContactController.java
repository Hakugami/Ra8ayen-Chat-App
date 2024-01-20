package prototyping.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

import java.net.URL;
import java.util.ResourceBundle;

public class AddContactController implements Initializable {

    @FXML
    private TextField phoneField;
    private Popup popup;

    public void setPopup(Popup popup) {
        this.popup = popup;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phoneField.setOnAction(event -> {
            String text = phoneField.getText();
            System.out.println(text);
            if(popup != null) {
                popup.hide();
            }
        });
    }
}