package model.entities;

public class Chat {
    private int chatId;
    private String name;
    private String chatImage;
    private int adminId;
    private String creationDate;
    private String lastModified;

    public Chat(int chatId, String name, int adminId, String chatImage, String creationDate, String lastModified) {
        this.chatId = chatId;
        this.name = name;
        this.adminId = adminId;
        this.creationDate = creationDate;
        this.chatImage = chatImage;
        this.lastModified = lastModified;
    }

    public Chat(String name, int adminId) {
        this.name = name;
        this.adminId = adminId;
    }

    public int getChatId() {
        return chatId;
    }

    public int getChatIdId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
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

    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    @Override
    public String toString() {

            return "ChatGroup{" +
                    "id=" + chatId +
                    ", name='" + name + '\'' +
                    ", adminId=" + adminId +
                    ", creationDate='" + creationDate + '\'' +
                    ", lastModified='" + lastModified + '\'' +
                    '}';
    }
}
