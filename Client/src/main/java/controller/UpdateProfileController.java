package controller;

import dto.Model.UserModel;
import dto.requests.UpdateUserRequest;
import dto.responses.UpdateUserResponse;
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
import utils.ImageUtls;
import view.ViewFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;

public class UpdateProfileController {

    public ImageView profilePic;
    public Circle profilePicCircle;
    public TextArea bioArea;
    public Label User_Name_lbl;
    public TextField nameField;
    public Label Country_lbl;
    public ComboBox countryComboBox;
    public Label DateOfBirth_lbl;
    public DatePicker datePicker;
    public Button backToLoginButton;
    public Button signUpButton;

    public Chat previousChat;

    @FXML
    public void initialize() throws RemoteException {
        previousChat = Model.getInstance().getViewFactory().getSelectedContact().get();
        profilePic = new ImageView();
        nameField.setPromptText(CurrentUser.getInstance().getUserName());
        bioArea.setPromptText(CurrentUser.getInstance().getBio());
        countryComboBox.setPromptText(CurrentUser.getInstance().getCountry());
        datePicker.setPromptText(String.valueOf(CurrentUser.getInstance().getDateOfBirth()));


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
            if(Model.getInstance().getViewFactory().getSelectedContact().get() instanceof Group){
                Model.getInstance().getViewFactory().getSelectedContact().set(previousChat);
            } else {
                Model.getInstance().getViewFactory().getSelectedContact().set(previousChat);
            }

        });
    }

private void handleUpdateProfile() throws RemoteException {
    String name = (nameField.getText() != null && !nameField.getText().isEmpty()) ? nameField.getText() : CurrentUser.getInstance().getUserName();
    String bio = bioArea.getText()!=null && !bioArea.getText().isEmpty() ? bioArea.getText() : CurrentUser.getInstance().getBio();
    String country = (countryComboBox.getValue() != null) ? countryComboBox.getValue().toString() : "Default Country";
    LocalDate localDateOfBirth = datePicker.getValue();
    Date dateOfBirth = (localDateOfBirth != null) ? java.sql.Date.valueOf(localDateOfBirth) : java.sql.Date.valueOf(LocalDate.now());
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
    }else {
        userModel.setProfilePicture(CurrentUser.getInstance().getProfilePicture());
    }

    userModel.setUserName(name);
    userModel.setBio(bio);
    userModel.setCountry(country);
    userModel.setDateOfBirth(dateOfBirth);
    userModel.setUserID(CurrentUser.getInstance().getUserID());
    userModel.setPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
    userModel.setEmailAddress(CurrentUser.getInstance().getEmailAddress());
    userModel.setGender(CurrentUser.getInstance().getGender());
    userModel.setUserStatus(CurrentUser.getInstance().getUserStatus());
    userModel.setUsermode(CurrentUser.getInstance().getUsermode());
    userModel.setLastLogin(CurrentUser.getInstance().getLastLogin());

    UpdateUserRequest updateUserRequest = new UpdateUserRequest(userModel);

    try {
        UpdateUserResponse updateUserResponse = NetworkFactory.getInstance().updateUser(updateUserRequest);
        if (updateUserResponse.isUpdated()) {
            System.out.println("User updated successfully");
        } else {
            System.out.println("User update failed");
        }
    } catch (NotBoundException | SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
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