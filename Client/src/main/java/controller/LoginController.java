package controller;

import controller.token.TokenManager;
import dto.requests.LoginRequest;
import dto.responses.LoginResponse;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Model;
import network.NetworkFactory;
import org.example.client.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {

    @FXML
    public TextField phoneNumberField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;
    @FXML
    public Button registerButton;

    @FXML
    AnchorPane loginXml;

    @FXML
    public void initialize() {
        phoneNumberField.setOnAction(event -> {
            try {
                handleLoginButton(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        passwordField.setOnAction(event -> {
            try {
                handleLoginButton(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        loginButton.setOnAction(event -> {
            try {
                handleLoginButton(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        registerButton.setOnAction(this::handleRegisterButton);

    }

    private void handleRegisterButton(ActionEvent event){
        Stage currentStage = (Stage) registerButton.getScene().getWindow();
        AnchorPane registerPane = (AnchorPane) Model.getInstance().getViewFactory().getRegister();
        registerPane.setTranslateX(currentStage.getWidth());
        currentStage.getScene().setRoot(registerPane);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), registerPane);
        tt.setToX(0);
        tt.play();
    }


    private void handleLoginButton(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (validateFields()) {
            LoginRequest loginRequest = new LoginRequest(phoneNumberField.getText(), passwordField.getText());
            LoginResponse loginResponse= NetworkFactory.getInstance().login(loginRequest);
            String token = loginResponse.getToken();
            TokenManager.getInstance().setToken(token);
            System.out.println(loginResponse.toString());
        } else {
            System.err.println("Invalid fields");
        }
    }
    private boolean validateFields() throws SQLException, ClassNotFoundException {
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