package controller;

import dto.Model.UserModel;
import dto.requests.*;
import dto.responses.AddContactResponse;
import dto.responses.BlockUserResponse;
import dto.responses.CheckIfFriendBlockedResponse;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import model.ContactData;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import org.controlsfx.control.Notifications;
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
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
    public Label bio;

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
               BlockUserResponse blockUserResponse=handleBlockButton();
               if(blockUserResponse.isBlocked()){
                   Notifications.create().title("Success").text(blockUserResponse.getBlockedMessage()).showInformation();
                   setStatusForBlockButton("Blocked", true);
               }else{
                   Notifications.create().title("Failed").text(blockUserResponse.getBlockedMessage()).showInformation();
                   setStatusForBlockButton("Block",false);
               }

            } catch (RemoteException | NotBoundException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private BlockUserResponse handleBlockButton() throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        System.out.println("Block Button Clicked");
        BlockUserRequest blockUserRequest = new BlockUserRequest();

        blockUserRequest.setUserPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
        blockUserRequest.setFriendPhoneNumber(otherUserModel.getPhoneNumber());
        blockUserRequest.setLocalDate(LocalDate.from(LocalDateTime.now()));

        BlockUserResponse blockUserResponse = NetworkFactory.getInstance().blockUser(blockUserRequest);
        return blockUserResponse;
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
        bio.setText(otherUserModel.getBio());
        BufferedImage bufferedImage = ImageUtls.convertByteToImage(otherUserModel.getProfilePicture());
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        profileCircle.setFill(new ImagePattern(image));
    }

    public void exitButtonClicked() {
        otherProfilePopup.hide();
    }

    public void checkIfBlocked()  {
        CheckIfFriendBlockedRequest request = new CheckIfFriendBlockedRequest();
        try {
            request.setPhoneNumberUser(CurrentUser.getInstance().getPhoneNumber());
            request.setFriendPhoneNumber(otherUserModel.getPhoneNumber());
            CheckIfFriendBlockedResponse checkIfFriendBlockedResponse= NetworkFactory.getInstance().checkIfUserBlocked(request);
            if(checkIfFriendBlockedResponse.isBlocked()){
                setStatusForBlockButton("Blocked",true);
            }else{
                setStatusForBlockButton("Block",false);
            }
        } catch (RemoteException e) {
           // throw new RuntimeException(e);
        } catch (SQLException e) {
           // throw new RuntimeException(e);
        } catch (NotBoundException e) {
            //throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            //throw new RuntimeException(e);
        }

    }
    void setStatusForBlockButton(String text, boolean disable){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                blockButton.setText(text);
                blockButton.setDisable(disable);
            }
        });

    }
    public boolean checkIfUserFriend(){
            for(ContactData contactData: CurrentUser.getCurrentUser().getContactDataList()){
                if(Objects.equals(CurrentUser.getCurrentUser().getPhoneNumber(), otherUserModel.getPhoneNumber())) {
                    setStatusForAddButton(" ",true, true);
                    return true;
                }
                else if(contactData.getPhoneNumber().equals(otherUserModel.getPhoneNumber())){
                    System.out.println(contactData.getPhoneNumber()+" "+otherUserModel.getPhoneNumber());
                    setStatusForAddButton(" ",false, false);
                    return true;
                }
            }

        setStatusForAddButton("Add",true, false);
        return false;
    }
    void setStatusForAddButton(String text, boolean visible, boolean isMe){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(isMe) {
                    addButton.setVisible(false);
                    blockButton.setVisible(false);
                    return;
                }
                addButton.setText(text);
                addButton.setVisible(visible);
                if(visible){ //user is not Friend
                    blockButton.setVisible(false);
                }
            }
        });

    }


}
