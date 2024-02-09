package dto.requests;

import java.io.Serializable;

public class GetBlockedContactRequest implements Serializable {
    private String phoneNumber ;

    private int userID;

    public GetBlockedContactRequest(String phoneNumber, int userID) {
        this.phoneNumber = phoneNumber;
        this.userID = userID;
    }
    public GetBlockedContactRequest(){

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
