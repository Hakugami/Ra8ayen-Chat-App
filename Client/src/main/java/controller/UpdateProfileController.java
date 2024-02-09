package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import concurrency.manager.ConcurrencyManager;
import dto.Model.UserModel;
import dto.requests.UpdateUserRequest;
import dto.responses.UpdateUserResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import model.*;
import network.NetworkFactory;
import org.controlsfx.control.Notifications;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class UpdateProfileController {

    public ImageView profilePic;
    public Circle profilePicCircle;
    public TextArea bioArea;
    public Label User_Name_lbl;
    public TextField nameField;
    public Label Country_lbl;
    public ComboBox<Country> countryComboBox;
    public Label DateOfBirth_lbl;
    public DatePicker datePicker;
    public Button backToLoginButton;
    public Button signUpButton;

    public Chat previousChat;
    public TextField phoneNumberField;
    public TextField emailField;

    @FXML
    public void initialize() throws RemoteException {
        previousChat = Model.getInstance().getViewFactory().getSelectedContact().get();
        profilePic = new ImageView();
        nameField.setPromptText(CurrentUser.getInstance().getUserName());
        bioArea.setPromptText(CurrentUser.getInstance().getBio());
        countryComboBox.setPromptText(CurrentUser.getInstance().getCountry());
        datePicker.setPromptText(String.valueOf(CurrentUser.getInstance().getDateOfBirth()));
        phoneNumberField.setPromptText(CurrentUser.getInstance().getPhoneNumber());
        emailField.setPromptText(CurrentUser.getInstance().getEmailAddress());
        ObservableList<Country> countries = loadCountriesFromJSON("/Countries/CountryCodes.json");
        countryComboBox.setItems(countries);


        Image profileImage = CurrentUser.getInstance().getProfilePictureImage();
        if (profileImage != null) {
            profilePic.setImage(profileImage);
            profilePicCircle.setFill(new ImagePattern(profileImage));
        } else {
            System.out.println("Profile image is null");
        }

        profilePicCircle.setOnMouseClicked(event -> {
            handleProfilePicSelection();
        });

        signUpButton.setOnAction(event -> {
            try {
                handleUpdateProfile();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        backToLoginButton.setOnAction(event -> {
            Model.getInstance().getViewFactory().getSelectedContact().setValue(null);
            if (Model.getInstance().getViewFactory().getSelectedContact().get() instanceof Group) {
                Model.getInstance().getViewFactory().getSelectedContact().set(previousChat);
            } else {
                Model.getInstance().getViewFactory().getSelectedContact().set(previousChat);
            }

        });
    }

    private void handleUpdateProfile() throws RemoteException {
        String name = (nameField.getText() != null && !nameField.getText().isEmpty()) ? nameField.getText() : CurrentUser.getInstance().getUserName();
        String bio = bioArea.getText() != null && !bioArea.getText().isEmpty() ? bioArea.getText() : CurrentUser.getInstance().getBio();
        String country = (countryComboBox.getValue() != null) ? countryComboBox.getValue().toString() : CurrentUser.getInstance().getCountry();
        String phoneNumber = phoneNumberField.getText() != null && !phoneNumberField.getText().isEmpty() ? phoneNumberField.getText() : CurrentUser.getInstance().getPhoneNumber();
        String email = emailField.getText() != null && !emailField.getText().isEmpty() ? emailField.getText() : CurrentUser.getInstance().getEmailAddress();
        LocalDate localDateOfBirth = datePicker.getValue();
        Date dateOfBirth = (localDateOfBirth != null) ? java.sql.Date.valueOf(localDateOfBirth) : CurrentUser.getInstance().getDateOfBirth();
        UserModel userModel = new UserModel();

        if (profilePic.getImage() != null) {
            BufferedImage bImage = SwingFXUtils.fromFXImage(profilePic.getImage(), null);
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            try {
                ImageIO.write(bImage, "png", s);
                byte[] res = s.toByteArray();
                userModel.setProfilePicture(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            userModel.setProfilePicture(CurrentUser.getInstance().getProfilePicture());
        }

        userModel.setUserName(name);
        userModel.setBio(bio);
        userModel.setCountry(country);
        userModel.setDateOfBirth(dateOfBirth);
        userModel.setUserID(CurrentUser.getInstance().getUserID());
        userModel.setPhoneNumber(phoneNumber);
        userModel.setEmailAddress(email);
        userModel.setGender(CurrentUser.getInstance().getGender());
        userModel.setUserStatus(CurrentUser.getInstance().getUserStatus());
        userModel.setUsermode(CurrentUser.getInstance().getUsermode());
        userModel.setLastLogin(CurrentUser.getInstance().getLastLogin());

        CurrentUser.getInstance().loadUser(userModel);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(userModel);

        try {
            UpdateUserResponse updateUserResponse = NetworkFactory.getInstance().updateUser(updateUserRequest);
            if (updateUserResponse.isUpdated()) {
                Model.getInstance().getControllerFactory().getContactsController().setTreeViewData();
                Model.getInstance().getControllerFactory().getContactsController().setImageProfileData();
            } else {
                Notifications.create().title("Update Error").text(updateUserResponse.getErrorMessage()).showError();
            }
        } catch (NotBoundException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            profilePicCircle.setFill(new ImagePattern(image));
        }
    }

}