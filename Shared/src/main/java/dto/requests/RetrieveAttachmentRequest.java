package dto.requests;

import java.io.Serializable;
import java.util.Arrays;

public class RetrieveAttachmentRequest implements Serializable {
    private int messageId;
    private int attachmentId;
    private byte[] attachmentData;

    public RetrieveAttachmentRequest(int messageId) {
        this.messageId = messageId;
    }


    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public byte[] getAttachmentData() {
        return attachmentData;
    }

    public void setAttachmentData(byte[] attachmentData) {
        this.attachmentData = attachmentData;
    }

    @Override
    public String toString() {
        return "RetrieveAttachmentRequest{" +
                "messageId=" + messageId +
                ", attachmentId=" + attachmentId +
                '}';
    }
}
