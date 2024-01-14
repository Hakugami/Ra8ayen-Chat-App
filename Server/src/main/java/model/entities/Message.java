package model.entities;

import java.time.LocalDateTime;


public class Message {
   private int MessageId;
    private int senderId;
    private int receiverId;
    private String messageContent;
    private LocalDateTime time;
    private String fontStyle;
    private String fontColor;
    private String textBackground;
    private int fontSize;
    private boolean bold;
    private boolean italic ;
    private boolean underline;
    private String emoji;
    private flag flag;




    public enum flag{
        user, client;;
    }
    public Message(){

    }

    public Message(int senderId, int receiverId, String messageContent, LocalDateTime time, String fontStyle, String fontColor, String textBackground, int fontSize, boolean bold, boolean italic, boolean underline, String emoji, flag flag) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageContent = messageContent;
        this.time = time;
        this.fontStyle = fontStyle;
        this.fontColor = fontColor;
        this.textBackground = textBackground;
        this.fontSize = fontSize;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.emoji = emoji;
        this.flag = flag;
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

    public boolean getBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean getItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean getUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public int getMessageId() {
        return MessageId;
    }

    public void setMessageId(int messageId) {
        MessageId = messageId;
    }
    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public Message.flag getFlag() {
        return flag;
    }

    public void setFlag(Message.flag flag) {
        this.flag = flag;
    }

}
