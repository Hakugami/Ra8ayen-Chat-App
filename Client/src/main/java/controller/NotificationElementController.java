package controller;

import dto.Model.NotificationModel;
import dto.Model.UserModel;
import dto.requests.AcceptFriendRequest;
import dto.requests.FriendRequest;
import dto.requests.RejectContactRequest;
import dto.responses.AcceptFriendResponse;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.CurrentUser;
import model.Model;
import network.NetworkFactory;
import notification.NotificationManager;
import org.controlsfx.control.Notifications;
import utils.ImageUtls;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NotificationElementController implements Initializable {
    public Button acceptButton;
    public Button refuseButton;
    public Label Name;
    public Circle imageClip;
    public Circle profileCircle;
    public ImageView ImagId;
    public UserModel friendModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acceptButton.setOnAction(actionEvent -> {
            try {
                acceptButtonAction();
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        refuseButton.setOnAction(actionEvent -> refuseButtonAction());
    }
    public void setData(UserModel userModel) {
        Name.setText(userModel.getUserName());
        Image image = SwingFXUtils.toFXImage(ImageUtls.convertByteToImage(userModel.getProfilePicture()), null);
        profileCircle.setFill(new ImagePattern(image));
        this.friendModel = userModel;
    }

    public void refuseButtonAction() {
        for(NotificationModel notificationModel : NotificationManager.getInstance().getNotifactionsList()){
            if(((FriendRequest)notificationModel).getUserModel().getPhoneNumber().equals(friendModel.getPhoneNumber())){
                try {
                    NetworkFactory.getInstance().rejectFriendRequest(new RejectContactRequest(notificationModel.getId()));
                } catch (RemoteException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
                NotificationManager.getInstance().getNotifactionsList().remove(notificationModel);
                Model.getInstance().getControllerFactory().getNotificationContextMenuController().removeUserFromList(friendModel);
                break;
            }
        }
    }

    public void acceptButtonAction() throws RemoteException, NotBoundException, SQLException, ClassNotFoundException {
        UserModel model = loadCurrentUserToModel();

        AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest(CurrentUser.getInstance().getUserID(),CurrentUser.getInstance().getPhoneNumber(),friendModel.getPhoneNumber(),model);
        AcceptFriendResponse acceptFriendResponse= NetworkFactory.getInstance().acceptFriendRequest(acceptFriendRequest);
        if(acceptFriendResponse.isDone()){
            CurrentUser.getInstance().getCallBackController().userIsOnline(friendModel.getUserName());
            for(NotificationModel notificationModel : NotificationManager.getInstance().getNotifactionsList()){
                if(((FriendRequest)notificationModel).getUserModel().getPhoneNumber().equals(friendModel.getPhoneNumber())){
                    NotificationManager.getInstance().getNotifactionsList().remove(notificationModel);
                    Model.getInstance().getControllerFactory().getNotificationContextMenuController().removeUserFromList(friendModel);
                    break;
                }
            }
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