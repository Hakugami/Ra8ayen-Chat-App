package model.entities;

import java.time.LocalDateTime;
import java.util.Arrays;


public class Message {
    private int MessageId;
    private int senderId;
    private int receiverId;
    private String messageContent;
    private LocalDateTime time;
    private boolean isAttachment;

    private byte[] attachment;
    private String fontStyle;
    private String fontColor;
    private String textBackground;
    private int fontSize;
    private boolean isBold;
    private boolean isItalic;
    private boolean isUnderline;

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

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getTextBackground() {
        return textBackground;
    }

    public void setTextBackground(String textBackground) {
        this.textBackground = textBackground;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean italic) {
        isItalic = italic;
    }

    public boolean isUnderline() {
        return isUnderline;
    }

    public void setUnderline(boolean underline) {
        isUnderline = underline;
    }

    @Override
    public String toString() {
        return "Message{" +
                "MessageId=" + MessageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", messageContent='" + messageContent + '\'' +
                ", time=" + time +
                ", isAttachment=" + isAttachment +
                ", attachment=" + Arrays.toString(attachment) +
                ", fontStyle='" + fontStyle + '\'' +
                ", fontColor='" + fontColor + '\'' +
                ", textBackground='" + textBackground + '\'' +
                ", fontSize=" + fontSize +
                ", isBold=" + isBold +
                ", isItalic=" + isItalic +
                ", isUnderline=" + isUnderline +
                '}';
    }

    public byte[] getAttachmentData() {
        return attachment;
    }

    public void setAttachmentData(byte[] attachment) {
        this.attachment = attachment;
    }
}
