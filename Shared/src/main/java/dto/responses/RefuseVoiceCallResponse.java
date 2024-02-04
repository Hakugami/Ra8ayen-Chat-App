package dto.responses;

import java.io.Serializable;

public class RefuseVoiceCallResponse implements Serializable {
    private String receiverPhoneNumber;
    private String senderPhoneNumber;

    private boolean refused;
    private String error;

    public RefuseVoiceCallResponse(String receiverPhoneNumber, String senderPhoneNumber, boolean accepted, String error) {
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.senderPhoneNumber = senderPhoneNumber;
        this.refused = accepted;
        this.error = error;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public boolean isRefused() {
        return refused;
    }

    public void setRefused(boolean refused) {
        this.refused = refused;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "AcceptVoiceCallResponse{" +
                "receiverPhoneNumber='" + receiverPhoneNumber + '\'' +
                ", senderPhoneNumber='" + senderPhoneNumber + '\'' +
                ", accepted=" + refused +
                ", error='" + error + '\'' +
                '}';
    }
}
