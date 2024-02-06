package dto.responses;

import java.io.Serializable;
import java.util.Arrays;

public class RetrieveAttachmentResponse implements Serializable {
    private int messageId;
    private int attachmentId;
    private byte[] attachmentData;
    private boolean success;
    private String error;

    public RetrieveAttachmentResponse(int messageId, int attachmentId, byte[] attachmentData, boolean success, String error) {
        this.messageId = messageId;
        this.attachmentId = attachmentId;
        this.attachmentData = attachmentData;
        this.success = success;
        this.error = error;
    }
    public RetrieveAttachmentResponse() {
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
        return "RetrieveAttachmentResponse{" +
                "messageId=" + messageId +
                ", attachmentId=" + attachmentId +
                ", success=" + success +
                ", error='" + error + '\'' +
                '}';
    }
}
