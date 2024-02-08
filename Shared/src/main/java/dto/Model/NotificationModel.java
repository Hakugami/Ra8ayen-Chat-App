package dto.Model;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    protected int id;
    protected String title;

    public NotificationModel() {
        this.id = -1;
        this.title = "";
    }

    public NotificationModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


}
