package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.client.HelloApplication;

import java.io.IOException;

public class LoginController {

    public TextField phoneNumberField;
    public PasswordField passwordField;
    @FXML
    AnchorPane loginXml;


    public void go_to_sign_up_xml() throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(HelloApplication.class.getResource("/Fxml/SignUp.fxml"));
        loginXml.getChildren().setAll(anchorPane);
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        if (validateFields()) {
            System.out.println("SignUp successful!");
            System.out.println(phoneNumberField.getText()+"\n"+
                    passwordField.getText()+"\n");
        } else {
            System.out.println("Validation failed. Please check your input.");
        }
    }
    private boolean validateFields() {
        if (!isValidPhoneNumber(phoneNumberField.getText()) ||
                !isValidPassword(passwordField.getText())
                ) {
            return false;
        }

        return true;
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        if( phoneNumber.matches("\\d{11}"))
            return true;
        else {
            System.out.println("Please enter a valid number , 11 digits.");
            return false;
        }
    }

    private boolean isValidPassword(String password) {
        if(password.length() >= 8)
            return true;
        else {
            System.out.println("Password must be 8 or more.");
            return false;
        }

    }
}
