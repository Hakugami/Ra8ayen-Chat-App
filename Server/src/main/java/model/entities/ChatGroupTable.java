package model.entities;

public enum ChatGroupTable {
    GROUPID("GroupID"),
    NAME("Name"),
    ADMINID("AdminID"),
    CREATIONDATE("CreationDate");
    public String name;
    ChatGroupTable(String name){
        this.name= name;
    }
}
