package prototyping.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class ContactListCell extends ListCell<String> {

    private FXMLLoader mLLoader;
    private ContactTileController controller;

    @Override
    protected void updateItem(String contact, boolean empty) {
        super.updateItem(contact, empty);

        if(empty || contact == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/FXML/ContactTile.fxml"));
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                controller = mLLoader.getController();
            }
            controller.setContactName(contact);
            controller.setStatusColor(getStatus(contact)); // getStatus is a method that returns the status of the contact
            setText(null);
            setGraphic(controller.getRoot());
        }
    }

    private String getStatus(String contact) {
        // This is a placeholder implementation. Replace it with your actual logic to get the status of the contact.
        return Math.random() < 0.5 ? "online" : "offline";
    }
}