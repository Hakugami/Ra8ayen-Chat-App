package model.entities;

public enum MessageTable {
    MessageID("MessageID"),
    SenderID("SenderID"),
    ReceiverID("ReceiverID"),
    MessageContent("MessageContent"),
    MessageTimestamp("MessageTimestamp"),
    MessageFontStyle("FontStyle"),
    MessageFontColor("FontColor"),
    TextBackground("TextBackground"),
    FontSize("FontSize"),
    Bold("Bold"),
    Italic("Italic"),
    Underline("Underline"),
    Emoji("Emoji");
    public String name;
    MessageTable(String name){
        this.name= name;
    }
}
