package model.entities;

public class Attachment {
    private int attachmentId;
    private int messageId;
    private byte[] attachment;

    public Attachment() {
    }

    public Attachment(int attachmentId, int messageId, byte[] attachment) {
        this.attachmentId = attachmentId;
        this.messageId = messageId;
        this.attachment = attachment;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "attachmentId=" + attachmentId +
                ", messageId=" + messageId +
                ", attachment=" + (attachment != null ? "Exists" : "Does not exist") +
                '}';
    }
}