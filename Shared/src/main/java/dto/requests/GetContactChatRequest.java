package dto.requests;

import java.io.Serializable;

public class GetContactChatRequest implements Serializable {
    int userID;
    int friendID;
    public GetContactChatRequest(int userID, int friendID) {
        this.userID = userID;
        this.friendID = friendID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getFriendID() {
        return friendID;
    }
    public void setFriendID(int friendID) {
        this.friendID = friendID;
    }
    @Override
    public String toString() {
        return "GetContactChatRequest{" +
                "userID=" + userID +
                ", friendID=" + friendID +
                '}';
    }
}
