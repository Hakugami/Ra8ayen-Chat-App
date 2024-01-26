package model;

import controller.CallBackControllerImpl;
import dto.Model.UserModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.Date;

public class CurrentUser extends UserModel {
    private static CurrentUser currentUser;
    private Image profilePictureImage;
    private CallBackControllerImpl callBackController = CallBackControllerImpl.getInstance();

    private CurrentUser() throws RemoteException {
    }

    public static CurrentUser getInstance() throws RemoteException {
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

    public CallBackControllerImpl getCallBackController() {
        return callBackController;
    }


    public Image getProfilePictureImage() {
        return profilePictureImage;
    }

    public void loadUser(UserModel user) {
        this.setUserID(user.getUserID());
        this.setUserName(user.getUserName());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setProfilePicture(user.getProfilePicture());
        this.setUserStatus(user.getUserStatus());
        this.setCountry(user.getCountry());
        this.setBio(user.getBio());
        this.setDateOfBirth(user.getDateOfBirth());
        this.setGender(user.getGender());
        //set last login to the current time
        this.setLastLogin(String.valueOf(new Date()));
        BufferedImage bufferedImage = ImageUtls.convertByteToImage(user.getProfilePicture());
        Image fxImage= SwingFXUtils.toFXImage(bufferedImage, null);
        this.profilePictureImage = fxImage;
    }


}
