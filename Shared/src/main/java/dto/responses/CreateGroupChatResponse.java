package dto.responses;

import java.io.Serializable;
import java.util.Arrays;

public class CreateGroupChatResponse implements Serializable {
    private int groupId;
    private String groupName;
    private byte[] groupPicture;
    private int groupAdminId;
    private boolean isCreated;
    private String errorMessage;

    public CreateGroupChatResponse(int groupId, String groupName, byte[] groupPicture, int groupAdminId, boolean isCreated, String errorMessage) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupPicture = groupPicture;
        this.groupAdminId = groupAdminId;
        this.isCreated = isCreated;
        this.errorMessage = errorMessage;
    }

    public CreateGroupChatResponse() {
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

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    @Override
    public String toString() {
        return "CreateGroupChatResponse{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", groupPicture=" + Arrays.toString(groupPicture) +
                ", groupAdminId=" + groupAdminId +
                ", isCreated=" + isCreated +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
