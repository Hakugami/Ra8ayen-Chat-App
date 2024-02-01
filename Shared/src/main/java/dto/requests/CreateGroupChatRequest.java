package dto.requests;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class CreateGroupChatRequest implements Serializable {
    private int adminID;
    private String groupName;
    private byte[] groupImage;
    private List<String> friendsPhoneNumbers;
    public CreateGroupChatRequest(int adminID, String groupName, byte[] groupImage, List<String> friendsPhoneNumbers) {
        this.adminID = adminID;
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.friendsPhoneNumbers = friendsPhoneNumbers;
    }
    public CreateGroupChatRequest() {
    }

    public List<String> getFriendsPhoneNumbers() {
        return friendsPhoneNumbers;
    }

    public void setFriendsPhoneNumbers(List<String> friendsPhoneNumbers) {
        this.friendsPhoneNumbers = friendsPhoneNumbers;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public byte[] getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(byte[] groupImage) {
        this.groupImage = groupImage;
    }

    @Override
    public String toString() {
        return "CreateGroupChatRequest{" +
                "adminID=" + adminID +
                ", groupName='" + groupName + '\'' +
                ", groupImage=" + Arrays.toString(groupImage) +
                ", friendsPhoneNumbers=" + friendsPhoneNumbers +
                '}';
    }
}
