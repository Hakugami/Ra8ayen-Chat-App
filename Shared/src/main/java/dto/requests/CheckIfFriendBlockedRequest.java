package dto.requests;

import java.io.Serializable;

public class CheckIfFriendBlockedRequest implements Serializable {

    private String phoneNumberUser;

    private String FriendPhoneNumber;

    public CheckIfFriendBlockedRequest(){

    }
    public CheckIfFriendBlockedRequest(String phoneNumberUser, String friendPhoneNumber) {
        this.phoneNumberUser = phoneNumberUser;
        FriendPhoneNumber = friendPhoneNumber;
    }

    public String getPhoneNumberUser() {
        return phoneNumberUser;
    }

    public void setPhoneNumberUser(String phoneNumberUser) {
        this.phoneNumberUser = phoneNumberUser;
    }

    public String getFriendPhoneNumber() {
        return FriendPhoneNumber;
    }

    public void setFriendPhoneNumber(String friendPhoneNumber) {
        FriendPhoneNumber = friendPhoneNumber;
    }
}
