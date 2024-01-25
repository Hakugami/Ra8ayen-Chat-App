package controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Country;
import org.example.client.HelloApplication;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

     @FXML
     TextField nameField;
    @FXML
     TextField emailField;
    @FXML
     TextField phoneNumberField;
    @FXML
     PasswordField passwordField;
    @FXML
     DatePicker datePicker;
    @FXML
    AnchorPane signUpXml;
    @FXML
    ComboBox<Country> comboBox;
    @FXML
    ComboBox<String> gender;
    String jsonFilePath = "D:\\jaavaa\\sdk\\xml_1\\ChatApp\\src\\main\\resources\\Countries\\CountryCodes.json";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //comboBox.setItems(FXCollections.observableArrayList("Hajar","Elen","Nour"));
        gender.setItems(FXCollections.observableArrayList("Female","Male"));
        ObservableList<Country> countries = loadCountriesFromJSON("/Countries/CountryCodes.json");
        comboBox.setItems(countries);

    }
    public void go_to_login_xml() throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(HelloApplication.class.getResource("/Fxml/Login.fxml"));
        signUpXml.getChildren().setAll(anchorPane);
    }

    private ObservableList<Country> loadCountriesFromJSON(String jsonFilePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(jsonFilePath)) {
            if (inputStream != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(inputStream);
                List<Country> countryList = new ArrayList<>();
                for (JsonNode node : jsonNode) {
                    String name = node.get("name").asText();
                    String dialCode = node.get("dial_code").asText();
                    String code = node.get("code").asText();
                    countryList.add(new Country(name, dialCode, code));
                }

                return FXCollections.observableArrayList(countryList);
            } else {
                System.err.println("Could not load JSON file.");
                return FXCollections.observableArrayList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }

    }

    @FXML
    private void handleSignUpButton(ActionEvent event) {
        if (validateFields()) {
            System.out.println("Sign-up successful!");
            System.out.println(nameField.getText()+"\n"+emailField.getText()+"\n"+phoneNumberField.getText()+"\n"+
                    passwordField.getText()+"\n"+comboBox.getValue()+"\n"+
                    gender.getValue()+"\n"+datePicker.getValue()+"\n");
        } else {
            System.out.println("Validation failed. Please check your input.");
        }
    }

    private boolean validateFields() {
        if (nameField.getText().isEmpty() ||
                !isValidEmail(emailField.getText()) ||
                !isValidPhoneNumber(phoneNumberField.getText()) ||
                !isValidPassword(passwordField.getText()) ||
                comboBox.getSelectionModel().isEmpty() ||
                gender.getSelectionModel().isEmpty() ||
                datePicker.getValue() == null) {
            return false;
        }

        return true;
    }
    private boolean isValidEmail(String email) {
        if( email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$"))
            return true;
        else {
            System.out.println("Please enter a valid e-mail.");
            return false;
        }

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
