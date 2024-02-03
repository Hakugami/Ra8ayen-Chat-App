package dto.requests;

import java.io.Serializable;

public class RejectContactRequest implements Serializable {
    int notificationId;

    public RejectContactRequest(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
    @Override
    public String toString() {
        return "RejectContactRequest{" +
                "notificationId=" + notificationId +
                '}';
    }
}
