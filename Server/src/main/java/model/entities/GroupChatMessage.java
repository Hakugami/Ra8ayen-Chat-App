package model.entities;

public class GroupChatMessage {
    private int GroupMessageId;
    private int groupId;
    private int senderId;
    private String messageContent;
    private String messageDate;

    public GroupChatMessage(int GroupMessageId, int groupId, int senderId, String messageContent, String messageDate) {
        this.GroupMessageId = GroupMessageId;
        this.groupId = groupId;
        this.senderId = senderId;
        this.messageContent = messageContent;
        this.messageDate = messageDate;
    }

    public int getGroupMessageId() {
        return GroupMessageId;
    }

    public void setGroupMessageId(int groupMessageId) {
        GroupMessageId = groupMessageId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }
}
