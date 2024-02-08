package dto.responses;

import java.io.Serializable;

public class ChatBotResponse implements Serializable {
    private String chatBotResponse;
    private boolean success;

    public ChatBotResponse() {
    }
    public String getChatBotResponse() {
        return chatBotResponse;
    }

    public void setChatBotResponse(String chatBotResponse) {
        this.chatBotResponse = chatBotResponse;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    @Override
    public String toString() {
        return "ChatBotResponse{" +
                "chatBotResponse='" + chatBotResponse + '\'' +
                ", success=" + success +
                '}';
    }
}
