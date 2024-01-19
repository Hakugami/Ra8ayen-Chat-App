package model.entities;

public enum ChatTable {
    CHATID("ChatID"),
    CHATNAME("ChatName"),
    CHATIMAGE("ChatImage"),
    ADMINID("AdminID"),
    CREATIONDATE("CreationDate"),
    LASTMODIFIED("LastModified");
    public String name;
    ChatTable(String name){
        this.name= name;
    }
}
