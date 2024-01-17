package dto.requests;

import java.io.Serializable;

public class AddUserToGroupRequest implements Serializable {
    private String userPhoneNumber;
    private String groupName;
    private String userToAddPhoneNumber;

    public AddUserToGroupRequest(String userPhoneNumber, String groupName, String userToAddPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
        this.groupName = groupName;
        this.userToAddPhoneNumber = userToAddPhoneNumber;
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

    @Override
    public String toString() {
        return "AddUserToGroupRequest{" +
                "userPhoneNumber='" + userPhoneNumber + '\'' +
                ", groupName='" + groupName + '\'' +
                ", userToAddPhoneNumber='" + userToAddPhoneNumber + '\'' +
                '}';
    }
}
