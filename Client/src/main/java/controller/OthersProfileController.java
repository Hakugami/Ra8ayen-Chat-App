package controller;

import dto.Model.UserModel;
import dto.requests.AddContactRequest;
import dto.requests.FriendRequest;
import dto.responses.AddContactResponse;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import org.controlsfx.control.Notifications;
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OthersProfileController implements Initializable {
    public Button exitButton;
    public Circle profileCircle;
    public Circle statusCircle;

    public Label name;
    public Label lastLogin;

    public Label phoneNumber;
    public Label email;
    public Label gender;
    public Label country;
    public Button addButton;
    public Button blockButton;
    public Label dateOfBirth;
    public  UserModel otherUserModel;

    public static Popup otherProfilePopup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getControllerFactory().setOthersProfileController(this);
        exitButton.setOnAction(event -> {
            exitButtonClicked();
        });

        addButton.setOnAction(event -> {
            try {
                AddContactResponse response=  handleAddButton();
                if(response.isDone()){
                    Notifications.create().title("Success").text("User Added Successfully").showInformation();
                }
                else{
                    Notifications.create().title("Failed").text("User Not Added").showError();
                }
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });
        blockButton.setOnAction(event -> {
            try {
                handleBlockButton();
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void handleBlockButton() throws RemoteException, NotBoundException {
        System.out.println("Block Button Clicked");
    }

    private AddContactResponse handleAddButton() throws RemoteException, NotBoundException {
        AddContactRequest addContactRequest = new AddContactRequest();
        addContactRequest.setUserId(CurrentUser.getInstance().getUserID());
        ArrayList<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(otherUserModel.getPhoneNumber());
        addContactRequest.setFriendsPhoneNumbers(phoneNumbers);
        return NetworkFactory.getInstance().addContact(addContactRequest);
    }


    public void setPopup(Popup popup , UserModel userModel) {
        otherUserModel = userModel;
        otherProfilePopup= popup;

    }

    public void setData(){
        System.out.println("---------------------"+otherUserModel.getUserName()+"--------------------");
        name.setText(otherUserModel.getUserName());
        lastLogin.setText(otherUserModel.getLastLogin());
        phoneNumber.setText(otherUserModel.getPhoneNumber());
        email.setText(otherUserModel.getEmailAddress());
        gender.setText(otherUserModel.getGender().name());
        country.setText(otherUserModel.getCountry());
        dateOfBirth.setText(String.valueOf(otherUserModel.getDateOfBirth()));
        BufferedImage bufferedImage = ImageUtls.convertByteToImage(otherUserModel.getProfilePicture());
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        profileCircle.setFill(new ImagePattern(image));
    }

    public void exitButtonClicked() {
        otherProfilePopup.hide();
    }
}
