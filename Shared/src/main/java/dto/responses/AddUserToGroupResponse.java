package dto.responses;

import java.io.Serializable;

public class AddUserToGroupResponse implements Serializable {
    private String userPhoneNumber;
    private String groupName;
    private String userToAddPhoneNumber;
    private boolean isAdded;
    private String errorMessage;

    public AddUserToGroupResponse(String userPhoneNumber, String groupName, String userToAddPhoneNumber, boolean isAdded) {
        this.userPhoneNumber = userPhoneNumber;
        this.groupName = groupName;
        this.userToAddPhoneNumber = userToAddPhoneNumber;
        this.isAdded = isAdded;
    }

    public AddUserToGroupResponse() {

    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUserToAddPhoneNumber() {
        return userToAddPhoneNumber;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) { 
        isAdded = added;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "AddUserToGroupResponse{" +
                "userPhoneNumber='" + userPhoneNumber + '\'' +
                ", groupName='" + groupName + '\'' +
                ", userToAddPhoneNumber='" + userToAddPhoneNumber + '\'' +
                ", isAdded=" + isAdded +
                '}';
    }

}
