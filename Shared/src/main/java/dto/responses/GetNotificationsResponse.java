package dto.responses;

import dto.Model.NotificationModel;
import dto.Model.UserModel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class GetNotificationsResponse implements Serializable {
    List<NotificationModel> notifications;
    List<UserModel> users;
    boolean isDone;

    public GetNotificationsResponse(List<NotificationModel> notifications, List<UserModel> users, boolean isDone) {
        this.notifications = notifications;
        this.users = users;
        this.isDone = isDone;
    }

    public GetNotificationsResponse() {
    }

    public List<NotificationModel> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationModel> notifications) {
        this.notifications = notifications;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "GetNotificationsResponse{" +
                "notifications=" + notifications +
                ", users=" + users +
                ", isDone=" + isDone +
                '}';
    }
}
