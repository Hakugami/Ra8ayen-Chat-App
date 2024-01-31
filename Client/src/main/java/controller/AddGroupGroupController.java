package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AddGroupGroupController implements Initializable {
    public TextField phoneField;
    public Label serverReply;
    public ImageView groupImage;
    public TextField groupName;
    public Button addButton;
    public static Popup popup;

    public static void setPopup(Popup pop) {
        popup = pop;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        groupImage.setOnMouseClicked(mouseEvent -> handleProfilePicSelection());

    }

    private void handleProfilePicSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        // Disable autoHide before showing the FileChooser
        popup.setAutoHide(false);
        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            groupImage.setImage(new javafx.scene.image.Image(file.toURI().toString()));
        }
        // Enable autoHide after the FileChooser is closed
        popup.setAutoHide(true);
    }
}

