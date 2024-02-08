package service;

import dao.impl.NotificationDaoImpl;
import model.entities.Notification;
import java.util.List;

public class InvitationService {
    private final NotificationDaoImpl notificationDao;
    public InvitationService() {
        notificationDao = new NotificationDaoImpl();
    }

    public int inviteContact(Notification notification) {
        if (notification.getSenderId() == notification.getReceiverId()) {
            return -1;
        }
        return notificationDao.saveNotification(notification);
    }
    public boolean ReceiverMakeInviteBefore(Notification notification){
        return notificationDao.checkInvite(notification);
    }
    public List<Notification> getNotifications(){
        return notificationDao.getAll();
    }

    public boolean deleteNotification(Notification notification){
        return notificationDao.delete(notification);
    }
    public int deleteNotificationBySenderAndReceiver(Notification notification){
        return notificationDao.deleteBySenderAndReceiver(notification);
    }
}
