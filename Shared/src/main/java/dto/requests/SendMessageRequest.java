package dto.requests;

import dto.Model.StyleMessage;
import dto.Model.UserModel;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SendMessageRequest implements Serializable {
    private int MessageId;
    private int senderId;
    private int receiverId; //chat ID
    private String messageContent;

    private LocalDateTime time;
    private boolean isAttachment;

    private byte[] attachment;
    private UserModel sender;

    private StyleMessage styleMessage;

    private boolean isGroupMessage;
    public int getMessageId() {
        return MessageId;
    }

    public void setMessageId(int messageId) {
        MessageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean getIsAttachment() {
        return isAttachment;
    }

    public void setIsAttachment(boolean attachment) {
        isAttachment = attachment;
    }

    @Override
    public String toString() {
        return "SendMessageRequest{" +
                "MessageId=" + MessageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", messageContent='" + messageContent + '\'' +
                ", time=" + time +
                ", getIsAttachment=" + isAttachment +
                '}';
    }
    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public byte[] getAttachmentData() {
        return attachment;
    }

    public void setAttachmentData(byte[] attachment) {
        this.attachment = attachment;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setIsAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public boolean isGroupMessage() {
        return isGroupMessage;
    }

    public void setGroupMessage(boolean groupMessage) {
        isGroupMessage = groupMessage;
    }

    public StyleMessage getStyleMessage() {
        return styleMessage;
    }

    public void setStyleMessage(StyleMessage styleMessage) {
        this.styleMessage = styleMessage;
    }
}
