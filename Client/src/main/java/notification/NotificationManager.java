package notification;

import dto.Model.NotificationModel;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {

    private static NotificationManager instance;
    private final List<NotificationModel> notificationModelMap;

    private NotificationManager() {
        notificationModelMap = new CopyOnWriteArrayList<>();
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void addNotification(NotificationModel notificationModel) {
        notificationModelMap.add(notificationModel);
    }

    public void removeNotification(NotificationModel notificationModel) {
        notificationModelMap.remove(notificationModel);
    }

    public List<NotificationModel> getNotifactionsList() {
        return notificationModelMap;
    }


}
