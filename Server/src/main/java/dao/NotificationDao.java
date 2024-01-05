package dao;

import model.entities.Notification;
import java.util.Optional;
import java.util.stream.Stream;

public interface NotificationDao {
    void addNotification(Notification notification);
    Optional<Notification> getNotificationBySenderId(int id);
    Stream<Notification> getAllNotifications();
    void updateNotification(Notification notification);
    void deleteNotification(Notification notification);

}
