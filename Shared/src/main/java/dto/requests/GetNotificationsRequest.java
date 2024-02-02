package dto.requests;

public class GetNotificationsRequest {
    int userID;
    public GetNotificationsRequest(int userID) {
        this.userID = userID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    @Override
    public String toString() {
        return "GetNotificationsRequest{" +
                "userID=" + userID +
                '}';
    }
}
