package model.entities;

public enum GroupChatMessageTable {
    GroupMessageID("GroupMessageID"),
    GroupID("GroupID"),
    SenderID("SenderID"),
    MessageContent("MessageContent"),
    MessageTimestamp("MessageTimestamp");
    public String name;
    GroupChatMessageTable(String name){
        this.name= name;
    }


}
