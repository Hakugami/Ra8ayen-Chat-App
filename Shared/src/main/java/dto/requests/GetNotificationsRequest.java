package dto.requests;

import java.io.Serializable;

public class GetNotificationsRequest implements Serializable {
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
