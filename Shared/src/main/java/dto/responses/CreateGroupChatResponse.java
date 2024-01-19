package dto.responses;

import java.io.Serializable;

public class CreateGroupChatResponse implements Serializable {
    private String userPhoneNumber;
    private String groupName;
    private boolean isCreated;
    private String errorMessage;


    public CreateGroupChatResponse(String userPhoneNumber, String groupName, boolean isCreated) {
        this.userPhoneNumber = userPhoneNumber;
        this.groupName = groupName;
        this.isCreated = isCreated;
        this.errorMessage = "";
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isCreated() {
        return isCreated;
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
                "userPhoneNumber='" + userPhoneNumber + '\'' +
                ", groupName='" + groupName + '\'' +
                ", isCreated=" + isCreated +
                '}';
    }
}
