package model;

import controller.ContactData;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group extends Chat {
    private int groupId;
    private String groupName;
    private ImageView groupImage;
    List<ContactData> groupMembers;

    public Group(){
        groupMembers = new CopyOnWriteArrayList<>();
    }
    public Group(int groupId, String groupName, ImageView groupImage) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupImage = groupImage;
        groupMembers = new CopyOnWriteArrayList<>();
    }

    public Group(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        groupMembers = new CopyOnWriteArrayList<>();
    }

    public void addMember(ContactData contactData){
        groupMembers.add(contactData);
    }
    public void removeMember(ContactData contactData){
        groupMembers.remove(contactData);
    }

    public List<ContactData> getGroupMembers() {
        return groupMembers;
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

    public ImageView getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(ImageView groupImage) {
        this.groupImage = groupImage;
    }

    public void setGroupMembers(List<ContactData> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
