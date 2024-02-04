package dto.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

public class MessageModel implements Serializable {
    private int MessageId;
    private  UserModel sender;
    private UserModel receiver;
    private String messageContent;
    private LocalDateTime time;
    private boolean isAttachment;
    private byte[] Attachment;
    private int chatId;
    private boolean isGroupMessage;

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
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

    private int senderId;

    private int receiverId;
    public MessageModel() {

    }

    public MessageModel(UserModel senderId, UserModel receiverId, String messageContent, LocalDateTime time, boolean isAttachment) {
        this.sender= senderId;
        this.receiver= receiverId;
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

    public boolean isAttachment() {
        return isAttachment;
    }

    public void setAttachment(boolean attachment) {
        isAttachment = attachment;
    }

    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    public UserModel getReceiver() {
        return receiver;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }

    public byte[] getAttachment() {
        return Attachment;
    }

    public void setAttachment(byte[] attachment) {
        Attachment = attachment;
    }

    public boolean isGroupMessage() {
        return isGroupMessage;
    }

    public void setGroupMessage(boolean groupMessage) {
        isGroupMessage = groupMessage;
    }

    public byte[] getAttachmentData() {
        return Attachment;
    }

    public void setAttachmentData(byte[] attachment) {
        Attachment = attachment;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "MessageId=" + MessageId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", messageContent='" + messageContent + '\'' +
                ", time=" + time +
                ", getIsAttachment=" + isAttachment +
                ", Attachment=" + Arrays.toString(Attachment) +
                ", chatId=" + chatId +
                ", isGroupMessage=" + isGroupMessage +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
