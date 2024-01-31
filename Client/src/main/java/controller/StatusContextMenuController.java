package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusContextMenuController implements Initializable {
    public VBox statusContextMenu;
    public AnchorPane anchorPane;
    public Label available;
    public Label busy;
    public Label away;

    private ContactsController contactsController;
    private Popup popup;

    public void setContactsController(ContactsController contactsController) {
        this.contactsController = contactsController;
    }

    public void setPopup(Popup popup) {
        this.popup = popup;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        available.setOnMouseClicked(mouseEvent -> {
            contactsController.changeStatusColor(Color.GREEN);
            popup.hide();
        });
        busy.setOnMouseClicked(mouseEvent -> {
            contactsController.changeStatusColor(Color.RED);
            popup.hide();
        });
        away.setOnMouseClicked(mouseEvent -> {
            contactsController.changeStatusColor(Color.YELLOW);
            popup.hide();
        });
    }
}