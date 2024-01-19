package dto.requests;

public class DeleteUserContactRequest {
    int id;

    int friendId;

    public DeleteUserContactRequest(){

    }
    public DeleteUserContactRequest(int id, int friendId) {
        this.id = id;
        this.friendId = friendId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
}
