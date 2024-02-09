package dto.responses;

import dto.requests.GetBlockedContactRequest;

import java.io.Serializable;

public class GetBlockedContactResponse implements Serializable {
    private int FriendID;

    private String FriendPhoneNumber;

    private String Name;

    public GetBlockedContactResponse(){

    }
    public GetBlockedContactResponse(int friendID, String friendPhoneNumber, String name) {
        FriendID = friendID;
        FriendPhoneNumber = friendPhoneNumber;
        Name = name;
    }

    public int getFriendID() {
        return FriendID;
    }

    public void setFriendID(int friendID) {
        FriendID = friendID;
    }

    public String getFriendPhoneNumber() {
        return FriendPhoneNumber;
    }

    public void setFriendPhoneNumber(String friendPhoneNumber) {
        FriendPhoneNumber = friendPhoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "GetBlockedContactResponse{" +
                "FriendID=" + FriendID +
                ", FriendPhoneNumber='" + FriendPhoneNumber + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}
