package dto.requests;

import java.io.Serializable;

public class ChatBotRequest implements Serializable {
    private String messageReceived;

    public ChatBotRequest(String messageReceived) {
        this.messageReceived = messageReceived;
    }
    public String getMessageReceived() {
        return messageReceived;
    }
    public void setMessageReceived(String messageReceived) {
        this.messageReceived = messageReceived;
    }
    @Override
    public String toString() {
        return "ChatBotRequest{" +
                "messageReceived='" + messageReceived + '\'' +
                '}';
    }
}
