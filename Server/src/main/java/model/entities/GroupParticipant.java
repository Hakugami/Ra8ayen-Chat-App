package model.entities;

public class GroupParticipant {
    private int groupId;
    private int participantUserId;
    private String participantStartDate;

    public GroupParticipant(int groupId, int participantid, String participantStartDate) {
        this.groupId = groupId;
        this.participantUserId = participantid;
        this.participantStartDate = participantStartDate;
    }

    public GroupParticipant(int groupId, int participantid) {
        this.groupId = groupId;
        this.participantUserId = participantid;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getParticipantUserId() {
        return participantUserId;
    }

    public void setParticipantUserId(int participantUserId) {
        this.participantUserId = participantUserId;
    }

    public String getParticipantStartDate() {
        return participantStartDate;
    }

    public void setParticipantStartDate(String participantStartDate) {
        this.participantStartDate = participantStartDate;
    }

    @Override
    public String toString() {
        return "GroupParticipant{" +
                "groupId=" + groupId +
                ", participantId=" + participantUserId +
                ", participantStartDate='" + participantStartDate + '\'' +
                '}';
    }
}
