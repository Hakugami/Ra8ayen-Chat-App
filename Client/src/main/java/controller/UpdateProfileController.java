package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UpdateProfileController {
    public Button updateButton;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField displayNameField;
    @FXML
    private TextField emailAddressField;
    // Add more fields for other TextFields

    @FXML
    public void initialize() {
        // Initialize the TextFields with the current values from the database
    }

    @FXML
    private void updateProfile() {
        // Update the fields in the UserAccounts table with the values from the TextFields
    }
}