package dto.requests;

import dto.Model.NotificationModel;
import dto.Model.UserModel;

import java.io.Serializable;

public class AcceptFriendRequest extends NotificationModel implements Serializable {

    private int userID;
    private String myPhoneNumber;
    private String friendPhoneNumber;
    private UserModel userModel;

    public AcceptFriendRequest(int userID,String myPhoneNumber, String friendPhoneNumber, UserModel userModel) {
        super();
        this.userID = userID;
        this.friendPhoneNumber = friendPhoneNumber;
        this.userModel = userModel;
        this.myPhoneNumber = myPhoneNumber;
    }

    //this constructor used only when user send invitation and found receiver sent before
    public AcceptFriendRequest(int userID, String friendPhoneNumber){
        super();
        this.userID = userID;
        this.friendPhoneNumber = friendPhoneNumber;
    }

    public AcceptFriendRequest() {
        super();
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

    public String getMyPhoneNumber() {
        return myPhoneNumber;
    }

    public void setMyPhoneNumber(String myPhoneNumber) {
        this.myPhoneNumber = myPhoneNumber;
    }

    @Override
    public String toString() {
        return "AcceptFriendRequest{" +
                "userID='" + userID + '\'' +
                ", friendPhoneNumber='" + friendPhoneNumber + '\'' +
                '}';
    }
}
