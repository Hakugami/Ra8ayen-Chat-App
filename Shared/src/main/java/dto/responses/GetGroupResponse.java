package dto.responses;

import java.io.Serializable;

public class GetGroupResponse implements Serializable {
    private int groupId;
    private String groupName;
    private String groupPicture;
    private boolean isReturned;
    private String errorMessage;
    public GetGroupResponse() {

    }
    public GetGroupResponse(int groupId, String groupName, String groupPicture, boolean isReturned, String errorMessage) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupPicture = groupPicture;
        this.isReturned = isReturned;
        this.errorMessage = errorMessage;
    }
    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public String getGroupPicture() {
        return groupPicture;
    }

    public void setGroupPicture(String groupPicture) {
        this.groupPicture = groupPicture;
    }

    @Override
    public String toString() {
        return "GetGroupResponse{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", groupPicture='" + groupPicture + '\'' +
                ", isReturned=" + isReturned +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
