package model.entities;

public enum GroupParticipantTable {
    GroupMemberID("GroupMemberID"),
    GroupID("GroupID"),
    ParticipantUserID("ParticipantUserID");
    public String name;
    GroupParticipantTable(String name){
        this.name= name;
    }
}
