package model.entities;

public enum MessageTable {
    MessageID("MessageID"),
    SenderID("SenderID"),
    ReceiverID("ReceiverID"),
    MessageContent("MessageContent"),
    MessageTimestamp("MessageTimestamp"),
    IsAttachment("IsAttachment");

    public String name;
    MessageTable(String name){
        this.name= name;
    }
}
