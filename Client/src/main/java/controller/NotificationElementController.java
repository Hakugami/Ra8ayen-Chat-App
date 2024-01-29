package controller;

import dto.Model.UserModel;
import dto.requests.AcceptFriendRequest;
import dto.responses.AcceptFriendResponse;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import model.CurrentUser;
import network.NetworkFactory;
import utils.ImageUtls;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class NotificationElementController implements Initializable {
    public Button acceptButton;
    public Button refuseButton;
    public Label Name;
    public Circle imageClip;
    public ImageView ImagId;
    public UserModel friendModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acceptButton.setOnAction(actionEvent -> {
            try {
                acceptButtonAction();
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        });
    }
    public void setData(UserModel userModel) {
        Name.setText(userModel.getUserName());
        Image image = SwingFXUtils.toFXImage(ImageUtls.convertByteToImage(userModel.getProfilePicture()), null);
        ImagId.setImage(image);
        imageClip.setClip(new Circle(50, 50, 50));
        this.friendModel = userModel;
    }

    public void acceptButtonAction() throws RemoteException, NotBoundException {
        UserModel model = loadCurrentUserToModel();

        AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest(CurrentUser.getInstance().getUserID(),friendModel.getPhoneNumber(),model);
        AcceptFriendResponse acceptFriendResponse= NetworkFactory.getInstance().acceptFriendRequest(acceptFriendRequest);
        if(acceptFriendResponse.isDone()){
            acceptButton.setDisable(true);
            refuseButton.setDisable(true);
        }
        System.out.println(acceptFriendResponse);
    }

    private static UserModel loadCurrentUserToModel() throws RemoteException {
        UserModel model = new UserModel();
        model.setUserID(CurrentUser.getInstance().getUserID());
        model.setUserName(CurrentUser.getInstance().getUserName());
        model.setPhoneNumber(CurrentUser.getInstance().getPhoneNumber());
        model.setProfilePicture(CurrentUser.getInstance().getProfilePicture());
        model.setUserStatus(CurrentUser.getInstance().getUserStatus());
        model.setCountry(CurrentUser.getInstance().getCountry());
        model.setBio(CurrentUser.getInstance().getBio());
        model.setDateOfBirth(CurrentUser.getInstance().getDateOfBirth());
        model.setGender(CurrentUser.getInstance().getGender());
        return model;
    }

}