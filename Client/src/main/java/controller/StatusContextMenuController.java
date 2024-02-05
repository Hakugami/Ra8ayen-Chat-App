package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import model.CurrentUser;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StatusContextMenuController implements Initializable {
    public VBox statusContextMenu;
    public AnchorPane anchorPane;
    public Label available;
    public Label busy;
    public Label away;
    public Label offline;

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
            try {
                contactsController.changeStatusColor(Color.GREEN);
            } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            popup.hide();
        });
        busy.setOnMouseClicked(mouseEvent -> {
            try {
                contactsController.changeStatusColor(Color.RED);
            } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            popup.hide();
        });
        away.setOnMouseClicked(mouseEvent -> {
            try {
                contactsController.changeStatusColor(Color.YELLOW);
            } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            popup.hide();
        });
        offline.setOnMouseClicked(mouseEvent -> {
            try {
                contactsController.changeStatusColor(Color.GRAY);

            } catch (RemoteException | SQLException | NotBoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            popup.hide();
        });
    }
}