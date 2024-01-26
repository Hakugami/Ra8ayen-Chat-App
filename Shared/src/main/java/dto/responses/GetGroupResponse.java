package dto.responses;

import java.io.Serializable;
import java.util.Arrays;

public class GetGroupResponse implements Serializable {
    private int groupId;
    private String groupName;
    private byte[] groupPicture;
    private int groupAdminId;
    public GetGroupResponse() {
    }
    public GetGroupResponse(int groupId, String groupName, byte[] groupPicture, int groupAdminId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupPicture = groupPicture;
        this.groupAdminId = groupAdminId;
    }
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public byte[] getGroupPicture() {
        return groupPicture;
    }

    public void setGroupPicture(byte[] groupPicture) {
        this.groupPicture = groupPicture;
    }

    public int getGroupAdminId() {
        return groupAdminId;
    }

    public void setGroupAdminId(int groupAdminId) {
        this.groupAdminId = groupAdminId;
    }

    @Override
    public String toString() {
        return "GetGroupResponse{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", groupPicture='" + Arrays.toString(groupPicture) + '\'' +
                ", groupAdminID='" + groupAdminId + '\'' +
                '}';
    }
}
