package prototyping.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UpdateProfileController {
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