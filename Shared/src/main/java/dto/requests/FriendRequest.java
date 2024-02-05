package dto.requests;

import dto.Model.NotificationModel;
import dto.Model.UserModel;

import java.io.Serializable;

public class FriendRequest extends NotificationModel implements Serializable {
    private String senderPhoneNumber;
    private String receiverPhoneNumber;
    UserModel userModel;
    public FriendRequest() {
    }

    public FriendRequest(String senderPhoneNumber, String receiverPhoneNumber, UserModel userModel) {
        this.senderPhoneNumber = senderPhoneNumber;
        this.receiverPhoneNumber = receiverPhoneNumber;

    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        senderPhoneNumber = senderPhoneNumber;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        receiverPhoneNumber = receiverPhoneNumber;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "senderPhoneNumber='" + senderPhoneNumber + '\'' +
                ", receiverPhoneNumber='" + receiverPhoneNumber + '\'' +
                ", userModel=" + userModel +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
