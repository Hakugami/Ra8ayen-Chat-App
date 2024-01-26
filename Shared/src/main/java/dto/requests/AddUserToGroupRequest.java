package dto.requests;

import java.io.Serializable;

public class AddUserToGroupRequest implements Serializable {
    private int chatID;
    private int userID;
    public AddUserToGroupRequest(int chatID, int userID) {
        this.chatID = chatID;
        this.userID = userID;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    @Override
    public String toString() {
        return "AddUserToGroupRequest{" +
                "chatID=" + chatID +
                ", userID=" + userID +
                '}';
    }
}
