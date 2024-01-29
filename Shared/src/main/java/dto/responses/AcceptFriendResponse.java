package dto.responses;

import java.io.Serializable;

public class AcceptFriendResponse implements Serializable {
    boolean isDone;
    String errorMessage;

    public AcceptFriendResponse(boolean isDone, String errorMessage) {
        this.isDone = isDone;
        this.errorMessage = errorMessage;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "AcceptFriendResponse{" +
                "isDone=" + isDone +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
