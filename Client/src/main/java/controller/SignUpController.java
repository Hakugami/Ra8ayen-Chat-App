package controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.requests.RegisterRequest;
import dto.responses.RegisterResponse;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import org.controlsfx.control.Notifications;

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
                    getStyleClass().clear();
                    getStyleClass().add("cell-default");


                    setOnMouseEntered(event -> getStyleClass().setAll("cell-hover"));
                    setOnMouseExited(event -> getStyleClass().setAll("cell-default"));
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
                    getStyleClass().add("cell-default");


                    setOnMouseEntered(event -> getStyleClass().setAll("cell-hover"));
                    setOnMouseExited(event -> getStyleClass().setAll("cell-default"));
                }
            }
        };
    }
}

 class customComboBoxCellFactory implements Callback<ListView<Country>, ListCell<Country>> {
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
                    getStyleClass().add("cell-default");


                    setOnMouseEntered(event -> getStyleClass().setAll("cell-hover"));
                    setOnMouseExited(event -> getStyleClass().setAll("cell-default"));
                }
            }
        };
    }
}


public class SignUpController implements Initializable {
    @FXML
    public ImageView profilePic;
    public TextArea bioArea;
    public Button minimizeButton;
    public Button exitButton;
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
    double xOffset =0;
    double yOffset =0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gender.setItems(FXCollections.observableArrayList("Female", "Male"));
        ObservableList<Country> countries = loadCountriesFromJSON("/Countries/CountryCodes.json");
        countryComboBox.setItems(countries);
        signUpButton.setOnAction(this::handleSignUpButton);
        backToLoginButton.setOnAction(this::handleBackToLoginButton);
        profilePic.setOnMouseClicked(event -> handleProfilePicSelection());
        countryComboBox.setCellFactory(new customComboBoxCellFactory());
        gender.setCellFactory(new CustomGenderComboBoxCellFactory());
        datePicker.setDayCellFactory(new CustomDatePickerCellFactory());

        Platform.runLater(() -> {
            Node root = signUpXml; // Assuming signUpXml is the root node of your scene

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
        });

        exitButton.setOnAction(event -> {
            Platform.exit();
        });
        minimizeButton.setOnAction(event -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });



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
                    Notifications.create().title("Duplicate Entry").text(registerResponse.getError()).showError();
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
        List<String> invalidFields = new ArrayList<>();

        if (nameField.getText().isEmpty()) {
            invalidFields.add("Name can't be empty");
        }

        if (!isValidEmail(emailField.getText())) {
            invalidFields.add("Email must be valid email address");
        }

        if (!isValidPhoneNumber(phoneNumberField.getText())) {
            invalidFields.add("Phone Number must be valid phone number");
        }

        if (!isValidPassword(passwordField.getText(), confirmPasswordField.getText())) {
            invalidFields.add("Password must be 8 or more and match Confirm Password");
        }

        if (countryComboBox.getSelectionModel().isEmpty()) {
            invalidFields.add("Country can't be empty");
        }

        if (gender.getSelectionModel().isEmpty()) {
            invalidFields.add("Gender can't be empty");
        }

        if (datePicker.getValue() == null || datePicker.getValue().isAfter(LocalDate.now())) {
            invalidFields.add("Date of Birth can't be empty or in the future");
        }

        if (!invalidFields.isEmpty()) {
            String invalidFieldsString = String.join("\n", invalidFields);
            Notifications.create().title("Validation Error").text("Invalid fields: \n" + invalidFieldsString).showError();
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

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() >= 8 && password.equals(confirmPassword))
            return true;
        else {
            System.out.println("Password must be 8 or more.");
            return false;
        }

    }
}
