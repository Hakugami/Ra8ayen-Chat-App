package service;

import dao.impl.NotificationDaoImpl;
import dao.impl.UserDaoImpl;
import model.entities.Notification;
import model.entities.User;
import java.util.ArrayList;
import java.util.List;

public class InvitationService {
    private final NotificationDaoImpl notificationDao;


    public InvitationService() {
        notificationDao = new NotificationDaoImpl();
    }

    public boolean inviteContact(Notification notification) {
        return notificationDao.save(notification);
    }
}
