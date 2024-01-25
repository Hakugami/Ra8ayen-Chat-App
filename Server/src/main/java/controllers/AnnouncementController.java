package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
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
        for (String username : OnlineControllerImpl.clients.keySet()) {
            try {
                OnlineControllerImpl.clients.get(username).receiveAnnouncement(announcement);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
