package dto.requests;

import java.io.Serializable;

public class GetGroupRequest implements Serializable {
    private int userId;

    public GetGroupRequest(int userId) {
        this.userId = userId;
    }
    public GetGroupRequest() {

    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GetGroupRequest{" +
                "userId=" + userId +
                '}';
    }
}
