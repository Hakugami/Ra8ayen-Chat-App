package dto.requests;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class CreateGroupChatRequest implements Serializable {
    private int adminID;
    private String groupName;
    private byte[] groupImage;
    public CreateGroupChatRequest(int adminID, String groupName, byte[] groupImage, List<Integer> participants) {
        this.adminID = adminID;
        this.groupName = groupName;
        this.groupImage = groupImage;
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
                '}';
    }
}
