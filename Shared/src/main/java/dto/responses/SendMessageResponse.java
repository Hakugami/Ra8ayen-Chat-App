package dto.responses;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SendMessageResponse implements Serializable {
    private int MessageId;
    private int senderId;
    private int receiverId;
    private String messageContent;
    private LocalDateTime time;
    private boolean isAttachment;
    private boolean success;
    private String error;
    private byte[] attachment;

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

    public boolean isAttachment() {
        return isAttachment;
    }

    public void setAttachment(boolean attachment) {
        isAttachment = attachment;
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
        return "SendMessageResponse{" +
                "MessageId=" + MessageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", messageContent='" + messageContent + '\'' +
                ", time=" + time +
                ", isAttachment=" + isAttachment +
                ", success=" + success +
                ", error='" + error + '\'' +
                '}';
    }

    public byte[] getAttachmentData() {
        return attachment;
    }

    public void setAttachmentData(byte[] attachment) {
        this.attachment = attachment;
    }
}
