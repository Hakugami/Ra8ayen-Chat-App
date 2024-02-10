package controllers;

import concurrency.manager.ConcurrencyManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AnnouncementController implements Initializable {
    @FXML
    private TextArea announcementTextArea;
    @FXML
    private Button announcementButton;
    @FXML
    private TextField announcementTitleTextField;

    private void handleAnnouncementButtonAction() {
        String announcement = announcementTextArea.getText();
        String announcementTitle = announcementTitleTextField.getText();

        ConcurrencyManager.getInstance().submitTask(() ->
        {
            for (String username : OnlineControllerImpl.clients.keySet()) {
                try {
                    OnlineControllerImpl.clients.get(username).receiveAnnouncement(announcement, announcementTitle);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        announcementTextArea.setText("");
        announcementTitleTextField.setText("");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        announcementButton.setOnAction(actionEvent -> handleAnnouncementButtonAction());
    }
}
