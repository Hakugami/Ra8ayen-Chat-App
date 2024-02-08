package dto.responses;

import java.io.Serializable;

public class BlockUserResponse implements Serializable {

   private boolean blocked;

    private String blockedMessage;

    private String phoneNumberOfFriend;

    private String Name;

    private int IDofFriend;

    private int chatID;

    public BlockUserResponse(){

    }
    public BlockUserResponse(boolean blocked, String blockedMessage, String phoneNumberOfFriend, String Name, int IDofFriend , int chatID) {
        this.blocked = blocked;
        this.blockedMessage = blockedMessage;
        this.phoneNumberOfFriend = phoneNumberOfFriend;
        this.Name = Name;
        this.IDofFriend = IDofFriend;
        this.chatID = chatID;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getBlockedMessage() {
        return blockedMessage;
    }

    public void setBlockedMessage(String blockedMessage) {
        this.blockedMessage = blockedMessage;
    }

    public String getPhoneNumberOfFriend() {
        return phoneNumberOfFriend;
    }

    public void setPhoneNumberOfFriend(String phoneNumberOfFriend) {
        this.phoneNumberOfFriend = phoneNumberOfFriend;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIDofFriend() {
        return IDofFriend;
    }

    public void setIDofFriend(int IDofFriend) {
        this.IDofFriend = IDofFriend;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }
}
