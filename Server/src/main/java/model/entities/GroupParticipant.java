package model.entities;

public class GroupParticipant {
    private int groupMemberId;
    private int groupId;
    private int userId;

    public GroupParticipant(int groupMemberId, int groupId, int userId) {
        this.groupMemberId = groupMemberId;
        this.groupId = groupId;
        this.userId = userId;
    }

    public int getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(int groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
