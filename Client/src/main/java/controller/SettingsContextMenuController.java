package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Model;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsContextMenuController implements Initializable {
    public VBox vbox;
    @FXML
    private Button profileButton;
    @FXML
    private Button themesButton;
    @FXML
    private HBox hbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileButton.setOnAction(event -> {
            vbox.getChildren().clear();
            vbox.getChildren().add(Model.getInstance().getViewFactory().getProfile());
        });

        themesButton.setOnAction(event -> {
            vbox.getChildren().clear();
            vbox.getChildren().add(Model.getInstance().getViewFactory().getThemes());
        });
    }
}