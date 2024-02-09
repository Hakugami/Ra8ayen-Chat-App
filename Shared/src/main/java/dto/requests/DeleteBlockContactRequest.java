package dto.requests;

import java.io.Serializable;

public class DeleteBlockContactRequest implements Serializable {
    private int UserID;

    private int FriendUserID;

    private String phoneNumberUser;

    private String PhoneNumberFriend;

    public DeleteBlockContactRequest(){

    }

    public DeleteBlockContactRequest(int userID, int friendUserID, String phoneNumberUser, String phoneNumberFriend) {
        UserID = userID;
        FriendUserID = friendUserID;
        this.phoneNumberUser = phoneNumberUser;
        PhoneNumberFriend = phoneNumberFriend;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getFriendUserID() {
        return FriendUserID;
    }

    public void setFriendUserID(int friendUserID) {
        FriendUserID = friendUserID;
    }

    public String getPhoneNumberUser() {
        return phoneNumberUser;
    }

    public void setPhoneNumberUser(String phoneNumberUser) {
        this.phoneNumberUser = phoneNumberUser;
    }

    public String getPhoneNumberFriend() {
        return PhoneNumberFriend;
    }

    public void setPhoneNumberFriend(String phoneNumberFriend) {
        PhoneNumberFriend = phoneNumberFriend;
    }
}
