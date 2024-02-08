package model.entities;

public enum MessageTable {
    MessageID("MessageID"),
    SenderID("SenderID"),
    ReceiverID("ReceiverID"),
    MessageContent("MessageContent"),
    MessageTimestamp("MessageTimestamp"),
    IsAttachment("IsAttachment"),
    FontStyle("FontStyle"),
    FontColor("FontColor"),
    TextBackground("TextBackground"),
    FontSize("FontSize"),
    IsBold("IsBold"),
    IsItalic("IsItalic"),
    IsUnderline("IsUnderline");

    public String name;

    MessageTable(String name){
        this.name= name;
    }
}