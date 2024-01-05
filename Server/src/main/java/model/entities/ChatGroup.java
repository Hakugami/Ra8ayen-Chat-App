package model.entities;

public class ChatGroup {
    private int groupId;
    private String name;
    private int adminId;
    private String creationDate;

    public ChatGroup(int groupId, String name, int adminId, String creationDate) {
        this.groupId = groupId;
        this.name = name;
        this.adminId = adminId;
        this.creationDate = creationDate;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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
                    "id=" + groupId +
                    ", name='" + name + '\'' +
                    ", adminId=" + adminId +
                    ", creationDate='" + creationDate + '\'' +
                    '}';
    }
}
