package dto.responses;

import java.io.Serializable;
import java.util.Arrays;

public class GetContactChatResponse implements Serializable {
    private int chatID;
    private int friendID;
    private String chatName;
    private byte[] chatImage;

    public GetContactChatResponse(int chatID, String chatName, byte[] chatImage) {
        this.chatID = chatID;
        this.chatName = chatName;
        this.chatImage = chatImage;
    }

    public GetContactChatResponse() {

    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public byte[] getChatImage() {
        return chatImage;
    }

    public void setFriendID(int friendID){
        this.friendID = friendID;
    }

    public void setChatImage(byte[] chatImage) {
        this.chatImage = chatImage;
    }
    @Override
    public String toString() {
        return "GetContactChatResponse{" +
                "chatID=" + chatID +
                ", chatName='" + chatName + '\'' +
                ", chatImage=" + Arrays.toString(chatImage) +
                '}';
    }
}
