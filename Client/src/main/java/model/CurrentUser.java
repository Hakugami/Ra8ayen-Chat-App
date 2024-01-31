package model;

import controller.CallBackControllerImpl;
import controller.ContactData;
import dto.Model.UserModel;
import dto.responses.GetContactsResponse;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import utils.ImageUtls;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CurrentUser extends UserModel {
    private static CurrentUser currentUser;
    private Image profilePictureImage;
    private CallBackControllerImpl callBackController = CallBackControllerImpl.getInstance();

    private List<ContactData> contactDataList;

    private CurrentUser() throws RemoteException {
        contactDataList = new CopyOnWriteArrayList<>();
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

    public List<ContactData> getContactDataList() {
        return contactDataList;
    }

    public void setContactDataList(List<ContactData> contactDataList) {
        this.contactDataList = contactDataList;
    }

    public void loadContactsList(List<GetContactsResponse> contactDataList) {
        this.contactDataList.clear();
        for (GetContactsResponse userModel : contactDataList) {
            ContactData contactData = new ContactData();
            contactData.setName(userModel.getName());
            contactData.setPhoneNumber(userModel.getPhoneNumber());
            contactData.setId(userModel.getIdOfFriend());
            Color color = null;
            if(userModel.getUserMode().equals(GetContactsResponse.UserMode.Available)){
                color = Color.GREEN;
            }
            else if(userModel.getUserMode().equals(GetContactsResponse.UserMode.Busy)){
                color = Color.RED;
            }
            else if(userModel.getUserMode().equals(GetContactsResponse.UserMode.Away)){
                color = Color.YELLOW;
            }
            else if(userModel.getUserStatus().equals(GetContactsResponse.UserStatus.Offline)){
                color = Color.GRAY;
            }
            contactData.setColor(color);
            BufferedImage bufferedImage = ImageUtls.convertByteToImage(userModel.getProfilePicture());
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            contactData.setImage(new ImageView(fxImage));
            this.contactDataList.add(contactData);
        }
    }
    public void loadUser(UserModel user) {
        this.setUserID(user.getUserID());
        this.setUserName(user.getUserName());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setProfilePicture(user.getProfilePicture());
        this.setUserStatus(user.getUserStatus());
        this.setCountry(user.getCountry());
        this.setEmailAddress(user.getEmailAddress());
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
