package model.entities;

import dao.Dao;

import java.util.Date;

public class ChatGroup {
    private int groupId;
    private String name;
    private int adminId;
    private String creationDate;
    private String lastModified;
    private boolean isGroup;


    public ChatGroup(int groupId, String name, int adminId, String creationDate) {
        this.groupId = groupId;
        this.name = name;
        this.adminId = adminId;
        this.creationDate = creationDate;
    }

    public ChatGroup(String name, int adminId) {
        this.name = name;
        this.adminId = adminId;
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

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
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
