package model.entities;

public class Notification {
    int notificationId;
    int receiverId;
    int senderId;
    String notificationSendDate;
    String notificationMessage;

    public Notification(int notificationId, int receiverId, int senderId, String notificationSendDate, String notificationMessage) {
        this.notificationId = notificationId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.notificationSendDate = notificationSendDate;
        this.notificationMessage = notificationMessage;
    }

    public Notification() {
    }

    public Notification(int notificationId, int receiverId, int senderId, String notificationMessageContent) {
        this.notificationId = notificationId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.notificationMessage = notificationMessageContent;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getNotificationId() {
        return notificationId;
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

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", receiverId=" + receiverId +
                ", senderId=" + senderId +
                ", notificationSendDate='" + notificationSendDate + '\'' +
                ", notificationMessage='" + notificationMessage + '\'' +
                '}';
    }
}
