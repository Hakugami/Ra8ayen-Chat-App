package model.entities;

import java.time.LocalDateTime;


public class Message {
    private int MessageId;
    private int senderId;
    private int receiverId;
    private String messageContent;
    private LocalDateTime time;
    private boolean isAttachment;

    private byte[] attachment;

    public Message(){

    }

    public Message(int senderId, int receiverId, String messageContent, LocalDateTime time, boolean isAttachment) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageContent = messageContent;
        this.time = time;
        this.isAttachment = isAttachment;
    }
    public int getMessageId() {
        return this.MessageId;
    }

    public void setMessageId(int MessageId) {
        this.MessageId = MessageId;
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
        return "Message{" +
                "MessageId=" + MessageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", messageContent='" + messageContent + '\'' +
                ", time=" + time +
                ", getIsAttachment=" + isAttachment +
                '}';
    }

    public byte[] getAttachmentData() {
        return attachment;
    }

    public void setAttachmentData(byte[] attachment) {
        this.attachment = attachment;
    }
}
