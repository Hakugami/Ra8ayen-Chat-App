package controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.requests.RegisterRequest;
import dto.responses.RegisterResponse;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Country;
import model.Model;
import network.NetworkFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

class CustomDatePickerCellFactory implements Callback<DatePicker, DateCell> {
    @Override
    public DateCell call(final DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setStyle("-fx-background-color: rgb(246, 241, 238); " + // Background color
                            "-fx-text-fill: rgb(79, 74, 69); " + // Text color
                            "-fx-border-color: rgb(108, 95, 91); " + // Border color
                            "-fx-border-radius: 5; " +
                            "-fx-font-size: 1.1em; " +
                            "-fx-padding: 10px; " );
                    setOnMouseEntered(event -> setStyle("-fx-background-color: rgb(237, 125, 49);"
                            + "-fx-text-fill: rgb(255, 255, 255);"
                            + "-fx-border-color: rgb(108, 95, 91);"
                            + "-fx-border-radius: 5;"
                            + "-fx-font-size: 1.1em;"
                            + "-fx-padding: 10px;")); // Hover color
                    setOnMouseExited(event -> setStyle("-fx-background-color: rgb(246, 241, 238);"
                            + "-fx-text-fill: rgb(79, 74, 69);"
                            + "-fx-border-color: rgb(108, 95, 91);"
                            + "-fx-border-radius: 5;"
                            + "-fx-font-size: 1.1em;"
                            + "-fx-padding: 10px;"
                    )); // Normal color
                }
            }
        };
    }
}

class CustomGenderComboBoxCellFactory implements Callback<ListView<String>, ListCell<String>> {
    @Override
    public ListCell<String> call(ListView<String> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item);
                    setStyle("-fx-background-color: rgb(246, 241, 238); " + // Background color
                            "-fx-text-fill: rgb(79, 74, 69); " + // Text color
                            "-fx-border-color: rgb(108, 95, 91); " + // Border color
                            "-fx-border-radius: 5; " +
                            "-fx-font-size: 1.1em; " +
                            "-fx-padding: 10px; " +
                            "-fx-border-width: 2px;");
                    setOnMouseEntered(event -> setStyle("-fx-background-color: rgb(237, 125, 49);"
                            + "-fx-text-fill: rgb(255, 255, 255);"
                            + "-fx-border-color: rgb(108, 95, 91);"
                            + "-fx-border-radius: 5;"
                            + "-fx-font-size: 1.1em;"
                            + "-fx-padding: 10px;"
                            + "-fx-border-width: 2px;")); // Hover color
                    setOnMouseExited(event -> setStyle("-fx-background-color: rgb(246, 241, 238);"
                            + "-fx-text-fill: rgb(79, 74, 69);"
                            + "-fx-border-color: rgb(108, 95, 91);"
                            + "-fx-border-radius: 5;"
                            + "-fx-font-size: 1.1em;"
                            + "-fx-padding: 10px;"
                            + "-fx-border-width: 2px;"
                    )); // Normal color
                }
            }
        };
    }
}
class CustomCountryComboBoxCellFactory implements Callback<ListView<Country>, ListCell<Country>> {
    @Override
    public ListCell<Country> call(ListView<Country> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.getName());
                    setStyle("-fx-background-color: rgb(246, 241, 238); " + // Background color
                            "-fx-text-fill: rgb(79, 74, 69); " + // Text color
                            "-fx-border-color: rgb(108, 95, 91); " + // Border color
                            "-fx-border-radius: 5; " +
                            "-fx-font-size: 1.1em; " +
                            "-fx-padding: 10px; " +
                            "-fx-border-width: 2px;");
                    setOnMouseEntered(event -> setStyle("-fx-background-color: rgb(237, 125, 49);"
                            + "-fx-text-fill: rgb(255, 255, 255);"
                            + "-fx-border-color: rgb(108, 95, 91);"
                            + "-fx-border-radius: 5;"
                            + "-fx-font-size: 1.1em;"
                            + "-fx-padding: 10px;"
                            + "-fx-border-width: 2px;")); // Hover color
                    setOnMouseExited(event -> setStyle("-fx-background-color: rgb(246, 241, 238);"
                            + "-fx-text-fill: rgb(79, 74, 69);"
                            + "-fx-border-color: rgb(108, 95, 91);"
                            + "-fx-border-radius: 5;"
                            + "-fx-font-size: 1.1em;"
                            + "-fx-padding: 10px;"
                            + "-fx-border-width: 2px;"
                    )); // Normal color
                }
            }
        };
    }
}

public class SignUpController implements Initializable {
    @FXML
    public ImageView profilePic;
    public TextArea bioArea;
    @FXML
    TextField nameField;
    @FXML
    TextField emailField;
    @FXML
    TextField phoneNumberField;
    @FXML
    PasswordField passwordField;
    @FXML
    PasswordField confirmPasswordField;
    @FXML
    DatePicker datePicker;
    @FXML
    AnchorPane signUpXml;
    @FXML
    ComboBox<Country> countryComboBox;
    @FXML
    ComboBox<String> gender;
    @FXML
    Button signUpButton;
    @FXML
    Button backToLoginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gender.setItems(FXCollections.observableArrayList("Female", "Male"));
        ObservableList<Country> countries = loadCountriesFromJSON("/Countries/CountryCodes.json");
        countryComboBox.setItems(countries);
        signUpButton.setOnAction(this::handleSignUpButton);
        backToLoginButton.setOnAction(this::handleBackToLoginButton);
        profilePic.setOnMouseClicked(event -> handleProfilePicSelection());
        countryComboBox.setCellFactory(new CustomCountryComboBoxCellFactory());
        gender.setCellFactory(new CustomGenderComboBoxCellFactory());
        datePicker.setDayCellFactory(new CustomDatePickerCellFactory());


    }

    private void handleBackToLoginButton(ActionEvent event) {
        backToLoginScreen(backToLoginButton);
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

    private void handleProfilePicSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            profilePic.setImage(image);
        }
    }

    @FXML
    private void handleSignUpButton(ActionEvent event) {
        if (validateFields()) {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setPhoneNumber(phoneNumberField.getText());
            registerRequest.setUserName(nameField.getText());
            registerRequest.setEmailAddress(emailField.getText());
            registerRequest.setPasswordHash(passwordField.getText());
            registerRequest.setGender(RegisterRequest.Gender.valueOf(gender.getValue()));
            registerRequest.setCountry(countryComboBox.getValue().getName());
            registerRequest.setDateOfBirth(java.sql.Date.valueOf(datePicker.getValue()));
            registerRequest.setBio(bioArea.getText());
            // If profile picture is selected
            if (profilePic.getImage() != null) {
                BufferedImage bImage = SwingFXUtils.fromFXImage(profilePic.getImage(), null);
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                try {
                    ImageIO.write(bImage, "png", s);
                    byte[] res = s.toByteArray();
                    registerRequest.setProfilePicture(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                RegisterResponse registerResponse = NetworkFactory.getInstance().register(registerRequest);
                if (registerResponse.isSuccess()) {
                    System.out.println("User registered successfully");
                    backToLoginScreen(signUpButton);
                } else {
                    System.err.println("User registration failed");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Invalid fields");
        }
    }

    private void backToLoginScreen(Button signUpButton) {
        Stage currentStage = (Stage) signUpButton.getScene().getWindow();
        AnchorPane loginPane = (AnchorPane) Model.getInstance().getViewFactory().getLogin();
        currentStage.getScene().setRoot(loginPane);
        loginPane.setTranslateX(-currentStage.getWidth());

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), loginPane);
        tt.setToX(0);
        tt.play();
    }

    private boolean validateFields() {
        if (nameField.getText().isEmpty() ||
                !isValidEmail(emailField.getText()) ||
                !isValidPhoneNumber(phoneNumberField.getText()) ||
                !isValidPassword(passwordField.getText()) ||
                countryComboBox.getSelectionModel().isEmpty() ||
                gender.getSelectionModel().isEmpty() ||
                datePicker.getValue() == null) {
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        if (email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$"))
            return true;
        else {
            System.out.println("Please enter a valid e-mail.");
            return false;
        }

    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\d{11}"))
            return true;
        else {
            System.out.println("Please enter a valid number , 11 digits.");
            return false;
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() >= 8)
            return true;
        else {
            System.out.println("Password must be 8 or more.");
            return false;
        }

    }
}
