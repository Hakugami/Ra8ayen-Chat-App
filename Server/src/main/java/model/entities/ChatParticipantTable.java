package model.entities;

public enum ChatParticipantTable {
    ParticipantStartDate("ParticipantStartDate"),
    ChatID("ChatID"),
    ParticipantUserID("ParticipantUserID");
    public String name;
    ChatParticipantTable(String name){
        this.name= name;
    }
}
