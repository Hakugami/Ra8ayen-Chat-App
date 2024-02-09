package dto.responses;

import java.io.Serializable;

public class CheckIfFriendBlockedResponse implements Serializable {

    private boolean isBlocked;

    private String blockMessage;

    public CheckIfFriendBlockedResponse(boolean isBlocked, String blockMessage) {
        this.isBlocked = isBlocked;
        this.blockMessage = blockMessage;
    }
    public CheckIfFriendBlockedResponse(){

    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getBlockMessage() {
        return blockMessage;
    }

    public void setBlockMessage(String blockMessage) {
        this.blockMessage = blockMessage;
    }
}
