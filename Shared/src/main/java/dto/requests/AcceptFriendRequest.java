package dto.requests;

import dto.Model.NotificationModel;
import dto.Model.UserModel;

import java.io.Serializable;

public class AcceptFriendRequest extends NotificationModel implements Serializable {

    private String userPhoneNumber;
    private String friendPhoneNumber;
    private UserModel userModel;

    public AcceptFriendRequest(String userPhoneNumber, String friendPhoneNumber, UserModel userModel) {
        super();
        this.userPhoneNumber = userPhoneNumber;
        this.friendPhoneNumber = friendPhoneNumber;
        this.userModel = userModel;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
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
                "userPhoneNumber='" + userPhoneNumber + '\'' +
                ", friendPhoneNumber='" + friendPhoneNumber + '\'' +
                '}';
    }
}
