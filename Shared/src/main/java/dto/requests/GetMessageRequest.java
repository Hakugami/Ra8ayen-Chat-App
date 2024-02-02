package dto.requests;

import java.io.Serializable;

public class GetMessageRequest implements Serializable{
    private String phoneNumber;
    private int chatId;

    public GetMessageRequest(){}


    public GetMessageRequest(String phoneNumber, int chatId) {
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
}
