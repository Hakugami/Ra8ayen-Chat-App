package dto.Model;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    protected final int id;
    protected final String title;

    public NotificationModel() {
        this.id = -1;
        this.title = "";
    }
    public NotificationModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
