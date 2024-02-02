package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ThemesController {
    public Label usernameLabel;

    public Button Confirm;
    @FXML
    private Label themeLabel;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Set the theme label
        themeLabel.setText("Current Theme");

        // Add an action to the back button
        Confirm.setOnAction(event -> {
            // Handle back button click
            System.out.println("Confirm button clicked");
            // Here you can add the logic for when the back button is clicked
            // For example, you can go back to the previous view
        });
    }
}