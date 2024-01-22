package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import server.ServerApplication;

public class AnnouncementController {
    @FXML
    private VBox vbRoot;
    @FXML
    private TextArea announcementTextArea;

    VBox getVBoxRoot()
    {
        return vbRoot;
    }

    public void handleAnnouncementButtonAction() {
        String announcement = announcementTextArea.getText();
        for (String username : ServerApplication.clients.keySet()) {
            try {
                ServerApplication.clients.get(username).receiveAnnouncement(announcement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
