package dto.requests;

import java.io.Serializable;

public class GetMessageRequest implements Serializable{
    private String phoneNumber;
    private String chatId;

    public GetMessageRequest(){}

    public GetMessageRequest(String phoneNumber, String chatId){
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getChatId() {
        return chatId;
    }

    @Override
    public String toString() {
        return "GetMessageRequest{" +
                "chatId='" + chatId + '\'' +
                '}';
    }
}
