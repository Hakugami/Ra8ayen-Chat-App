package dto.responses;

import dto.Model.MessageModel;

import java.io.Serializable;
import java.util.List;

public class GetMessageResponse implements Serializable {
    private String phoneNumber;
    private String chatId;
    private List<MessageModel> messageList;
    private boolean success;
    private String error;

    public GetMessageResponse(){}

    public GetMessageResponse(String phoneNumber, String chatId){
        this.phoneNumber = phoneNumber;
        this.chatId = chatId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getChatId() {
        return chatId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<MessageModel> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageModel> messageList) {
        this.messageList = messageList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "GetMessageResponse{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", chatId='" + chatId + '\'' +
                ", success=" + success +
                ", error='" + error + '\'' +
                '}';
    }
}
