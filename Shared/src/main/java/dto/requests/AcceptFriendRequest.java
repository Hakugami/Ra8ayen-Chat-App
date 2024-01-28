package dto.requests;

import dto.Model.NotificationModel;
import dto.Model.UserModel;

import java.io.Serializable;

public class AcceptFriendRequest extends NotificationModel implements Serializable {

    private int userID;
    private String friendPhoneNumber;
    private UserModel userModel;

    public AcceptFriendRequest(int userID, String friendPhoneNumber, UserModel userModel) {
        super();
        this.userID = userID;
        this.friendPhoneNumber = friendPhoneNumber;
        this.userModel = userModel;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFriendPhoneNumber() {
        return friendPhoneNumber;
    }

    public void setFriendPhoneNumber(String friendPhoneNumber) {
        this.friendPhoneNumber = friendPhoneNumber;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public String toString() {
        return "AcceptFriendRequest{" +
                "userID='" + userID + '\'' +
                ", friendPhoneNumber='" + friendPhoneNumber + '\'' +
                '}';
    }
}
