package model.entities;

public class ChatGroup {
    private int id;
    private String name;
    private int adminId;
    private String creationDate;

    public ChatGroup(int id, String name, int adminId, String creationDate) {
        this.id = id;
        this.name = name;
        this.adminId = adminId;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {

            return "ChatGroup{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", adminId=" + adminId +
                    ", creationDate='" + creationDate + '\'' +
                    '}';
    }
}
