package dto.requests;

import java.io.Serializable;

public class FriendRequest implements Serializable {
    private String senderPhoneNumber;
    private String receiverPhoneNumber;
    private boolean success;
    private String error;

    public FriendRequest() {
    }

    public FriendRequest(String senderPhoneNumber, String receiverPhoneNumber, boolean success, String error) {
        this.senderPhoneNumber = senderPhoneNumber;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.success = success;
        this.error = error;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        senderPhoneNumber = senderPhoneNumber;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        receiverPhoneNumber = receiverPhoneNumber;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "senderPhoneNumber='" + senderPhoneNumber + '\'' +
                ", receiverPhoneNumber='" + receiverPhoneNumber + '\'' +
                ", success=" + success +
                ", error='" + error + '\'' +
                '}';
    }
}
