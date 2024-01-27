package dto.requests;

import java.io.Serializable;

public class AcceptFriendRequest  implements Serializable {

    private String userPhoneNumber;
    private String friendPhoneNumber;

    public AcceptFriendRequest(String userPhoneNumber, String friendPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
        this.friendPhoneNumber = friendPhoneNumber;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    @Override
    public String toString() {
        return "AcceptFriendRequest{" +
                "userPhoneNumber='" + userPhoneNumber + '\'' +
                ", friendPhoneNumber='" + friendPhoneNumber + '\'' +
                '}';
    }
}
