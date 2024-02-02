package service;

import dao.impl.NotificationDaoImpl;
import model.entities.Notification;
import java.util.List;

public class InvitationService {
    private final NotificationDaoImpl notificationDao;
    public InvitationService() {
        notificationDao = new NotificationDaoImpl();
    }

    public boolean inviteContact(Notification notification) {
        if (notification.getSenderId() == notification.getReceiverId()) {
            return false;
        }
        return notificationDao.save(notification);
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
}
