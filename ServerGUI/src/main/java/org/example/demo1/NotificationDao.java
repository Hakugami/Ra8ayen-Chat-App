package org.example.demo1;

import java.util.List;

public interface NotificationDao extends Dao<Notification> {
    void save(Notification notification);
    Notification get(int id);
    List<Notification> getAll();
    void update(Notification notification);
    void delete(Notification notification);

}
