package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class AnnouncementController {
    private @FXML VBox vbRoot;
    private @FXML TextArea announcementTextArea;
    private @FXML Button announcementButton;

    VBox getVBoxRoot()
    {
        return vbRoot;
    }

}
