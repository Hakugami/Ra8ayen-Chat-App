package dao;

import model.entities.Notification;

import java.util.List;

public interface NotificationDao extends Dao<Notification> {
    boolean save(Notification notification);
    Notification get(int id);
    List<Notification> getAll();
    boolean update(Notification notification);
    boolean delete(Notification notification);

}
