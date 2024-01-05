package model.entities;

public class Notification {
    int notificationId;
    int receiverId;
    int senderId;
    String notificationSendDate;
    String messageContent;

    public Notification(int notificationId, int receiverId, int senderId, String notificationSendDate, String messageContent) {
        this.notificationId = notificationId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.notificationSendDate = notificationSendDate;
        this.messageContent = messageContent;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getNotificationSendDate() {
        return notificationSendDate;
    }

    public void setNotificationSendDate(String notificationSendDate) {
        this.notificationSendDate = notificationSendDate;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", receiverId=" + receiverId +
                ", senderId=" + senderId +
                ", notificationSendDate='" + notificationSendDate + '\'' +
                ", messageContent='" + messageContent + '\'' +
                '}';
    }
}
