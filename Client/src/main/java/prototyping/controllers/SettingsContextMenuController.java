package prototyping.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.javafx2.views.ViewFactory;

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

    private ViewFactory viewFactory;

    public SettingsContextMenuController() {
        this.viewFactory = new ViewFactory();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileButton.setOnAction(event -> {
            vbox.getChildren().clear();
            vbox.getChildren().add(viewFactory.getProfile());
        });

        themesButton.setOnAction(event -> {
            vbox.getChildren().clear();
            vbox.getChildren().add(viewFactory.getThemes());
        });
    }
}