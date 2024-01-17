package model.entities;

public class UserContacts {
    private int FriendID;
    private int UserID;
    private String creationDate;

    public UserContacts(int friendID, int userID, String creationDate) {
        FriendID = friendID;
        UserID = userID;
        this.creationDate = creationDate;
    }
    public UserContacts(int UserId, int friendID){
        this.UserID = UserId;
        this.FriendID = friendID;
    }

    public int getFriendID() {
        return FriendID;
    }

    public void setFriendID(int friendID) {
        FriendID = friendID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "UserContacts{" +
                "FriendID=" + FriendID +
                ", UserID=" + UserID +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }
}
